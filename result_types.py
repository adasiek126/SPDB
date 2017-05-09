import collections


RawDelayGain = collections.namedtuple("RawDelayGain", ["diff", "origin_delay", "destination_delay", "hour",
                                                       "origin_point_in_course", "destination_point_in_course",
                                                       "day_course_id", "origin_lat", "origin_lng",
                                                       "destination_lat", "destination_lng",
                                                       "origin_stoppoint", "destination_stoppoint",
                                                       "line_id"])


class DelayGain(RawDelayGain):
    def __new__(cls, *args):
        self = super(RawDelayGain, cls).__new__(cls, *args)
        self.origin_point = (self.origin_lat, self.origin_lng)
        self.destination_point = (self.destination_lat, self.destination_lng)
        self.stoppoints = (self.origin_stoppoint, self.destination_stoppoint)
        return self


DelayRoute = collections.namedtuple("DelayRoute", ["delay", "route"])


Mean = collections.namedtuple("mean", ["value", "count"])


StretchAtTime = collections.namedtuple("StretchAtTime", ["stretch", "day", "hour"])