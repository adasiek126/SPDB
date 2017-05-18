from collections import namedtuple
from recordtype import recordtype


Line = namedtuple("Line", "number, endStop, stops")
Stop = namedtuple("Stop", "id, originalX, originalY, changedX, changedY, name")

Point = namedtuple("Point", "latitude, longitude")

TraceResult = namedtuple("TraceResult", "point, datetime")

TraceWithSpeeds = namedtuple("TraceWithSpeeds", "point_between, speed")

Variant = namedtuple("Variant", "id, list")

RealStopPoint = recordtype("RealStopPoints", "point, how_many, point_history")

StopPoint = namedtuple("StopPoint", "id, point, name")

AnalyzeResult = namedtuple("AnalyzeResult", "id, name, original_latitude, original_longitude, calculated_latitude, calculated_longitude, how_many, lines")

LinesWithStops = namedtuple("LinesWithStops", "id_stop, line_name")

PossibleBusStop = namedtuple("PossibleBusStop", "point, how_many, distance")