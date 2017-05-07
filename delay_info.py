import sqlalchemy
from sqlalchemy.ext.automap import automap_base
from sqlalchemy.orm import Session
from sqlalchemy import create_engine
from sqlalchemy.sql import select
from itertools import groupby
import numpy as np
import collections
import pickle


rtest_update = False
Base = automap_base()

drivername = "psycopg2"
engine = create_engine("postgresql://postgres:1569ml@localhost/cesip_pw")
#sqlalchemy.engine.url.URL(drivername, username=None, password=None, host=None, port=None, database=None, query=None)
Base.prepare(engine, reflect=True)

#DayStopping = Base.classes.csipdaystopping
#StopPoint = Base.classes.csipstoppoint
conn = engine.connect()

session = Session(engine)

RawDelayGain = collections.namedtuple("RawDelayGain", ["diff", "origin_delay", "destination_delay", "hour",
                                                       "origin_point_in_course", "destination_point_in_course",
                                                       "day_course_id", "origin_lat", "origin_lng",
                                                       "destination_lat", "destination_lng",
                                                       "origin_stoppoint", "destination_stoppoint"])
DelayRoute = collections.namedtuple("DelayRoute", ["delay", "route"])

class DelayGain(RawDelayGain):
    def __new__(cls, *args):
        self = super(RawDelayGain, cls).__new__(cls, *args)
        self.origin_point = (self.origin_lat, self.origin_lng)
        self.destination_point = (self.destination_lat, self.destination_lng)
        self.stoppoints = (self.origin_stoppoint, self.destination_stoppoint)
        return self


def get_delays(engine, hour_from, hour_to, day):
    results = []
#    print(hour_from, hour_to, day)
    raw_results = engine.execute("""select b.delaysec-a.delaysec as diff, a.delaysec as origin_delay, b.delaysec as destination_delay, to_char(a.realarrival, 'HH24:MI') as hour,
a.orderincourse as origin_point, b.orderincourse as destination_point, a.daycourse_loid,
points_a.latitude as origin_lat, points_a.longitude as origin_lng, points_b.latitude as destination_lat, points_b.longitude destination_lng, a.stoppoint_loid, b.stoppoint_loid
from csipdaystopping a, csipdaystopping b,csipstoppoint points_a,csipstoppoint points_b
where a.delaysec is not null and b.delaysec is not null and points_a.loid = a.stoppoint_loid and points_b.loid = b.stoppoint_loid
and cast(to_char(a.realarrival, 'HH24') as integer) between {hour_from} and {hour_to}
and to_char(a.strapdate, 'dy') like  '{day}'
and to_char(a.strapdate,'YY-MM-DD')=to_char(b.strapdate,'YY-MM-DD')
and a.orderincourse+1 = b.orderincourse
and a.line_loid=b.line_loid and a.vehicle=b.vehicle
and a.daycourse_loid=b.daycourse_loid;""".format(hour_from=hour_from, hour_to=hour_to, day=day))
#    print(raw_results)
    for row in raw_results:
#        print(row)
        results.append(DelayGain(row))
#        print(results[-1])
    return results


def get_delays_line(engine, hour_from, hour_to, day, line_name):
    results = []
    raw_results = engine.execute("""select b.delaysec-a.delaysec as diff, a.delaysec as origin_delay, b.delaysec as destination_delay, to_char(a.realarrival, 'HH24:MI') as hour,
a.orderincourse as origin_point, b.orderincourse as destination_point, a.daycourse_loid,
points_a.latitude as origin_lat, points_a.longitude as origin_lng, points_b.latitude as destination_lat, points_b.longitude destination_lng, a.stoppoint_loid, b.stoppoint_loid
from csipdaystopping a left join csipline line on a.line_loid=line.loid, csipdaystopping b,csipstoppoint points_a,csipstoppoint points_b
where a.delaysec is not null and b.delaysec is not null and points_a.loid = a.stoppoint_loid and points_b.loid = b.stoppoint_loid
and cast(to_char(a.realarrival, 'HH24') as integer) between {hour_from} and {hour_to}
and to_char(a.strapdate, 'dy') like '{day}'
and to_char(a.strapdate,'YY-MM-DD')=to_char(b.strapdate,'YY-MM-DD')
and a.orderincourse+1 = b.orderincourse
and a.line_loid=b.line_loid and a.vehicle=b.vehicle
and a.daycourse_loid=b.daycourse_loid
and line.name='{line_name}';""".format(hour_from=hour_from, hour_to=hour_to, day=day, line_name=line_name))
    for row in raw_results:
        results.append(DelayGain(row))
    return results


def get_mean_delays(hour_from, hour_to, day, line=None):
    delays = []
    day = day.lower()
    print((hour_from, hour_to, day, line))
    if not line:
        res = get_delays(engine, hour_from, hour_to, day)
    else:
        res = get_delays_line(engine, hour_from, hour_to, day, line)
    res_grouped = groupby(sorted(res, key=lambda delay_gain: delay_gain.stoppoints), key=lambda delay_gain: delay_gain.stoppoints)
    for points, group in res_grouped:
        data = [(delay.diff, delay.origin_point, delay.destination_point) for delay in group]
        mean = np.mean([t[0] for t in data])
#        print((data[0][1], data[0][2], mean))
#        print(points[0], points[1], mean)
        # TODO: zmienic na namedtuple
        delays.append((points[0], points[1], mean))
    if rtest_update:
        rfile_name = "get_mean_delays__{}_{}_{}_{}.dump".format(hour_from, hour_to, day, line)
        with open(rfile_name, "wb") as rfile:
            pickle.dump(delays, rfile)
    return delays


def get_delay_routes(delays):
    results = []
    for delay in delays:
        raw_results = engine.execute("""select latitude, longitude from csipconnectionnode node left join csipconnection con on node.connection_loid=con.loid
where con.fromstoppoint_loid={from_point}
and con.tostoppoint_loid={to_point}
order by orderno asc;""".format(from_point=delay[0], to_point=delay[1]))
        points=[]
        for point in raw_results:
            points.append({"lat": float(point[0]), "lng": float(point[1])})
        results.append(DelayRoute(delay[2], points))
    return results

def get_route(point_pair):
    raw_results = engine.execute("""select latitude, longitude from csipconnectionnode node left join csipconnection con on node.connection_loid=con.loid
where con.fromstoppoint_loid={from_point}
and con.tostoppoint_loid={to_point}
order by orderno asc;""".format(from_point=point_pair[0], to_point=point_pair[1]))
    points = []
    for point in raw_results:
        points.append({"lat": float(point[0]), "lng": float(point[1])})
    return points

#delays=get_mean_delays(14, 15, "tue")

#print(get_delay_routes(delays))
#result = engine.execute()