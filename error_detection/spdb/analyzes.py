from math import cos, asin, sqrt
from datatypes import *
import datetime

def distance(p1, p2):
    p = 0.017453292519943295     #Pi/180
    a = 0.5 - cos((p2.latitude - p1.latitude) * p)/2 + cos(p1.latitude * p) * cos(p2.latitude * p) * (1 - cos((p2.longitude - p1.longitude) * p)) / 2
    return 12742 * asin(sqrt(a)) #2*R*asin...

def speed(p1, p2, time):
    if(time != 0):
        return 3600*distance(p1, p2) / time
    else:
        return 1000

def pointBetween(p1, p2):
    return Point((p1.latitude + p2.latitude)/2, (p1.longitude + p2.longitude)/2)

def averagePoint(list):
    sumx = 0
    sumy = 0
    for el in list:
        sumx += el.latitude/len(list)
        sumy += el.longitude/len(list)
    return Point(sumx, sumy)

class Core:
    def __init__(self, db):
        self.variants = []
        self.db = db
        self.linesforstop = []

    def getDayCourseTraceWithSpeedsList(self, day_course_id):
        one_course_trace_list = self.db.getTraceForDayCourse(day_course_id)
        one_course_trace_with_speeds_list = []
        for idx, val in enumerate(one_course_trace_list):
            if(idx < len(one_course_trace_list) - 1):
                sp = speed(val.point, one_course_trace_list[idx+1].point, (one_course_trace_list[idx+1].datetime-val.datetime).total_seconds())
                if(sp < 0.1):
                    one_course_trace_with_speeds = TraceWithSpeeds(pointBetween(val.point, one_course_trace_list[idx+1].point), sp)
                    one_course_trace_with_speeds_list.append(one_course_trace_with_speeds)
        return one_course_trace_with_speeds_list

    def getVariant(self, id):
        for var in self.variants:
            if(var.id == id):
                return var
        all_day_coursed_id_for_variant = self.db.getAllDayCoursesForVariant(id)
        list_with_day_course_trace_with_speeds_list_for_variant = []
        for one_day_coursed_id in all_day_coursed_id_for_variant:
            list = self.getDayCourseTraceWithSpeedsList(one_day_coursed_id)
            if(len(list)>0):
                list_with_day_course_trace_with_speeds_list_for_variant.append(list)
        varia = Variant(id, list_with_day_course_trace_with_speeds_list_for_variant)
        self.variants.append(varia)
        return varia

    def getVariantsForStop(self, id):
        list_variants_id = self.db.getVariantsForStop(id)
        list_variants = []
        for variant_id in list_variants_id:
            list_variants.append(self.getVariant(variant_id))
        return list_variants

    def getRealStopPoints(self, variants, stop_from_db):
        real_stop_points = []
        for variant in variants:
            for daycourse in variant.list:
                for stop in daycourse:
                    dis = distance(stop_from_db.point, stop.point_between)
                    if(dis < 1):
                        founded = False
                        for real_stop_point in real_stop_points:
                            if(distance(real_stop_point.point, stop.point_between) < 0.02):
                                founded = True
                                real_stop_point.how_many += 1
                                point_history.append(stop.point_between)
                        if(founded == False):
                            point_history = []
                            point_history.append(stop.point_between)
                            real_stop_points.append(RealStopPoint(stop.point_between, 1, point_history))
        return real_stop_points
                    

    def analyzeStop(self, stop):
        variants = self.getVariantsForStop(stop.id)
        real_stop_points = self.getRealStopPoints(variants, stop)
        count = 0
        best_point = None
        max = 0
        dis = 0
        for real_stop_point in real_stop_points:
            dis = distance(stop.point, real_stop_point.point)       
            if(real_stop_point.how_many/(dis*dis) > max):
                max = real_stop_point.how_many/(dis*dis)
                best_point = real_stop_point
        if(best_point is None):
            return None
        av_point = averagePoint(best_point.point_history)
        res = PossibleBusStop(av_point, best_point.how_many, dis)
        print("Latitude: " + str(av_point.latitude) + " Longitude: " + str(av_point.longitude) + " How many times: " + str(best_point.how_many))
        return res

    def getLinesForStopId(self, id):
        if(len(self.linesforstop) == 0):
            self.linesforstop = self.db.getLinesWithStops()
        was = False
        list = []
        for el in self.linesforstop:
            if(el.id_stop == id):
                list.append(el.line_name)
                was = True
            else:
                if(was == True):
                    return list
        

    def analyzeAllStops(self):
        analyzed_list = []
        all_stops = self.db.getAllStopPoints()
        count = 0
        for stop in all_stops:
            count += 1
            print(str(count) + "/" + str(len(all_stops)))
            print("Analyzing stop point id: " + str(stop.id) + " Latitude: " + str(stop.point.latitude) + " Longitude: " + str(stop.point.longitude))
            if(stop.name is not None):
                print("Stop name: " + stop.name)
            calculated_point = self.analyzeStop(stop)
            if(calculated_point is None):
                print("Didn't find traces")
            else:
                analyzed_list.append(AnalyzeResult(stop.id, stop.name, stop.point.latitude, stop.point.longitude, calculated_point.point.latitude, calculated_point.point.longitude, calculated_point.how_many, self.getLinesForStopId(stop.id)))
        return analyzed_list
            
        


