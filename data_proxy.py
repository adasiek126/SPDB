from sqlalchemy.ext.automap import automap_base
from sqlalchemy.orm import Session
from sqlalchemy import create_engine
from sqlalchemy import create_engine
from sqlalchemy.engine.url import URL
import numpy as np
import pickle
from itertools import groupby
import analyses
from result_types import *
import time

from user_config import *


class CachedData:
    def __init__(self, data):
        self.data = data
        self.creation_time = time.time()


class CachedResults:
    def __init__(self, result):
        self.result = result
        self.creation_time = time.time()

    def get_result(self):
        return self.result


class DelayGainDataIterator:
    def __init__(self, data_proxy):
        self.data_proxy = data_proxy
        self.hour = 0
        self.days = ['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun']
        self.day = iter(self.days)
        self.current_day = next(self.day)
        self.current_data = iter([])

    def __next__(self):
        try:
            return next(self.current_data)
        except StopIteration:
            if self.hour <=23:
                self.current_data = iter(self.data_proxy.get_data(self.hour, self.current_day, None))
                self.hour += 1
                return self.__next__()
            else:
                print(self.current_day, self.hour)
                try:
                    self.current_day = next(self.day)
                    self.hour = 0
                    self.current_data = iter(self.data_proxy.get_data(self.hour, self.current_day, None))
                    return self.__next__()
                except StopIteration:
                    raise StopIteration from None

    def __iter__(self):
        return self


class DataProxy:
    def __init__(self, driver_name="postgres", user_name=default_user, password=default_password, host="localhost", port=5432, database="cesip_pw"):
        self.engine = create_engine(
            URL(
                driver_name, username=user_name, password=password, host=host, port=port, database=database))
        self.data_cache = {}
        self.routes_cache = {}
        self.delay_gain_histogram_results = {}
        self.mean_delay_gain_results = {}
        self.line_ids = self.get_line_ids()

    def get_line_id_by_name(self, line_name):
        return self.line_ids[line_name]

    def get_line_ids(self):
        ids = {}
        raw_results = self.engine.execute("select loid, name from csipline")
        for (loid, name) in raw_results:
            ids[name] = loid
        return ids

    def query_for_data(self, hour, day, line):
        if line:
            results = []
            raw_results = self.engine.execute("""select b.delaysec-a.delaysec as diff, a.delaysec as origin_delay, b.delaysec as destination_delay, to_char(a.realarrival, 'HH24:MI') as hour,
            a.orderincourse as origin_point, b.orderincourse as destination_point, a.daycourse_loid,
            points_a.latitude as origin_lat, points_a.longitude as origin_lng, points_b.latitude as destination_lat, points_b.longitude destination_lng, a.stoppoint_loid, b.stoppoint_loid, a.line_loid
            from csipdaystopping a left join csipline line on a.line_loid=line.loid, csipdaystopping b,csipstoppoint points_a,csipstoppoint points_b
            where a.delaysec is not null and b.delaysec is not null and points_a.loid = a.stoppoint_loid and points_b.loid = b.stoppoint_loid
            and cast(to_char(a.realarrival, 'HH24') as integer) between {hour_from} and {hour_to}
            and to_char(a.strapdate, 'dy') like '{day}'
            and to_char(a.strapdate,'YY-MM-DD')=to_char(b.strapdate,'YY-MM-DD')
            and a.orderincourse+1 = b.orderincourse
            and a.line_loid=b.line_loid and a.vehicle=b.vehicle
            and a.daycourse_loid=b.daycourse_loid
            and line.name='{line_name}';""".format(hour_from=hour, hour_to=hour, day=day, line_name=line))
            for row in raw_results:
                results.append(DelayGain(row))
        else:
            results = []
            raw_results = self.engine.execute("""select b.delaysec-a.delaysec as diff, a.delaysec as origin_delay, b.delaysec as destination_delay, to_char(a.realarrival, 'HH24:MI') as hour,
            a.orderincourse as origin_point, b.orderincourse as destination_point, a.daycourse_loid,
            points_a.latitude as origin_lat, points_a.longitude as origin_lng, points_b.latitude as destination_lat, points_b.longitude destination_lng, a.stoppoint_loid, b.stoppoint_loid, a.line_loid
            from csipdaystopping a, csipdaystopping b,csipstoppoint points_a,csipstoppoint points_b
            where a.delaysec is not null and b.delaysec is not null and points_a.loid = a.stoppoint_loid and points_b.loid = b.stoppoint_loid
            and cast(to_char(a.realarrival, 'HH24') as integer) between {hour_from} and {hour_to}
            and to_char(a.strapdate, 'dy') like  '{day}'
            and to_char(a.strapdate,'YY-MM-DD')=to_char(b.strapdate,'YY-MM-DD')
            and a.orderincourse+1 = b.orderincourse
            and a.line_loid=b.line_loid and a.vehicle=b.vehicle
            and a.daycourse_loid=b.daycourse_loid;""".format(hour_from=hour, hour_to=hour, day=day))
            for row in raw_results:
                results.append(DelayGain(row))
        return results

    def get_data(self, hour, day, line):
        day = day.lower()
        print("get data")
        print((hour, day, line))
        if line:
            try:
                return self.data_cache[(hour, day, line)].data
            except KeyError:
                line_id = self.get_line_id_by_name(line)
                print("lineid")
                print(line_id)
                try:
                    return [row for row in self.data_cache[(hour, day, None)].data if row.line_id == line_id]
                except KeyError:
                    return self.query_for_data(hour, day, line)
        else:
            try:
                return self.data_cache[(hour, day, line)].data
            except KeyError:
                data = self.query_for_data(hour, day, line)
                self.data_cache[(hour, day, line)] = CachedData(data)
                return data

    def get_mean_delay_gain_hour(self, hour, day, line=None):
        """For one specific hour"""
        day = day.lower()
        try:
            return self.mean_delay_gain_results[(hour, day, line)].get_result()
        except KeyError:
            result = analyses.get_mean_delay_gain(self.get_data(hour, day, line))
            self.mean_delay_gain_results[(hour, day, line)] = CachedResults(result)
            return result

    def get_delay_gain_histogram_hour(self, hour, day, line=None):
        """For one specific hour"""
        day = day.lower()
        try:
            return self.delay_gain_histogram_results[(hour, day, line)].get_result()
        except KeyError:
            result = analyses.get_delay_gain_histogram(self.get_data(hour, day, line))
            self.delay_gain_histogram_results[(hour, day, line)] = CachedResults(result)
            return result

    def get_mean_delay_gain(self, hour_from, hour_to, day, line=None):
        day = day.lower()
        results = []
        for i, hour in enumerate(range(hour_from, hour_to + 1)):
            results.extend(self.get_mean_delay_gain_hour(hour, day, line))
        aggregated_results = []
        for points_ids, bipoint_mean in groupby(results, key=lambda result: result[0]):
            values = []
            count = 0
            for bipoint_hour in bipoint_mean:
                values.append(bipoint_hour[1].value * bipoint_hour[1].count)
                count += bipoint_hour[1].count
                points = bipoint_hour[0]
            aggregated_results.append((points[0], points[1], sum(values) / count))
        return aggregated_results

    def get_delay_gain_histogram(self, hour_from, hour_to, day, line=None):
        day = day.lower()
        results = np.ndarray(shape=(hour_from + hour_to + 1, len(analyses.histogram_bins) - 1)) * 0
        for i, hour in enumerate(range(hour_from, hour_to + 1)):
            results[i] = self.get_delay_gain_histogram_hour(hour, day, line)[0]
        return np.sum(results, axis=0), analyses.histogram_bins

    def get_delay_routes(self, delays):
        results = []
        for delay in delays:
            try:
                results.append(DelayRoute(delay[2], self.routes_cache[(delay[0], delay[1])].data))
            except KeyError:
                raw_results = self.engine.execute("""select latitude, longitude from csipconnectionnode node left join csipconnection con on node.connection_loid=con.loid
        where con.fromstoppoint_loid={from_point}
        and con.tostoppoint_loid={to_point}
        order by orderno asc;""".format(from_point=delay[0], to_point=delay[1]))
                points = []
                for point in raw_results:
                    points.append({"lat": float(point[0]), "lng": float(point[1])})
                self.routes_cache[(delay[0], delay[1])] = CachedData(points)
                results.append(DelayRoute(delay[2], points))
        return results

