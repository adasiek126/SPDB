import numpy as np
from itertools import groupby
import pandas as pd
from result_types import *

histogram_bins = list(range(0, 300, 15)) + [float("inf")]


def get_mean_delay_gain(delays_data):
    delays = []
    grouped = groupby(sorted(delays_data, key=lambda delay_gain: delay_gain.stoppoints),
                      key=lambda delay_gain: delay_gain.stoppoints)
    for points, group in grouped:
        data = [(delay.diff, delay.origin_point, delay.destination_point) for delay in group]
        mean = np.mean([t[0] for t in data])
        # TODO: zmienic na namedtuple
        delays.append(((points[0], points[1]), Mean(mean, len(data))))
    return delays


def get_delay_gain_histogram(delays_data):
    df = pd.DataFrame(delays_data)
    return np.histogram(df["diff"], histogram_bins)


def find_lags(delay_gain_data, theta=120):
    lags = {}
    for delay in delay_gain_data:
        if delay.diff > theta:
            try:
                lags[delay.stoppoints].append(delay)
            except KeyError:
                lags[delay.stoppoints] = [delay]
    return lags


def norm_hour(hour):
    hour = hour % 24
    if hour == 0:
        hour = 24
    return hour


def get_time_ranges(list_of_hours):
    ranges = [(None, None)]
    for hour in list_of_hours:
        last_range = ranges[-1]
        if last_range[0] is None:
            ranges[-1] = (hour, norm_hour(hour+1))
        else:
            if last_range[1] == hour:
                ranges[-1] = (last_range[0], norm_hour(hour+1))
            else:
                ranges.append((hour, norm_hour(hour+1)))
    if ranges == [(None, None)]:
        print("warning: empty time range ", list_of_hours)
        return []
    else:
        return ranges


'''def get_time_ranges(list_of_hours):
    range_start = None
    range_stop = None
    ranges = []
    for hour in list_of_hours:
        if range_start is None:
            range_start = hour
            range_stop = hour
        else:
            if range_stop == hour - 1:
                range_stop = hour
            else:
                ranges.append((range_start, norm_hour(range_stop+1)))
                range_start = hour
                range_stop = hour
    if range_stop is None and range_start is not None:
        ranges.append((range_start, norm_hour(range_start + 1)))
    elif range_start is not None and ranges != [] and range_start != ranges[-1][0]:
        ranges.append((range_start, norm_hour(range_stop+1)))
    if range_start == range_stop:
        ranges.append((range_start, norm_hour(range_start + 1)))
    return ranges'''


def find_group_lags(delay_gain_data, theta=120, min_relative_support=0.4):
    day_names = {"mon": "poniedziałek", "tue": "wtorek", "wed": "środa", "thu": "czwartek", "fri": "piątek", "sat": "sobota", "sun": "niedziela"}
    hour = 0
    day = "mon"
    this_hour_lags = {}
    this_hour_stopping_counts = {}
    lagged_stretchs = {}
    for delay in delay_gain_data:
        if delay_gain_data.hour == hour and delay_gain_data.current_day == day:
            # kod odpowiedzialny za normalne zliczanie
            try:
                    this_hour_stopping_counts[delay.stoppoints] += 1
            except KeyError:
                    this_hour_stopping_counts[delay.stoppoints] = 1
            if delay.diff > theta:
                try:
                    this_hour_lags[delay.stoppoints].append(delay)
                except KeyError:
                    this_hour_lags[delay.stoppoints] = [delay]
        else:
            # podsumowanie danych z tej godziny i aktualizacja godziny i dnia
            for stopingpoints_pair in this_hour_lags.keys():
                relative_support = \
                    len(this_hour_lags[stopingpoints_pair]) / this_hour_stopping_counts[stopingpoints_pair]
                if relative_support >= min_relative_support:
                    try:
                        lagged_stretchs[stopingpoints_pair].append(StretchAtTime(stopingpoints_pair, day, hour))
                    except KeyError:
                        lagged_stretchs[stopingpoints_pair] = [StretchAtTime(stopingpoints_pair, day, hour)]
            this_hour_lags = {}
            this_hour_stopping_counts = {}
            hour = delay_gain_data.hour
            day = delay_gain_data.current_day
    results = []
    for stoppingpoints in lagged_stretchs.keys():
        times = {}
        for stretch in lagged_stretchs[stoppingpoints]:
            try:
                times[stretch.day].append(stretch.hour)
            except KeyError:
                times[stretch.day] = [stretch.hour]
        content = ""
        endl = ""
        for day in times.keys():
            content += endl + day_names[day] + ": "
            endl = R"</br>"
            sep = ""
            for hour_range in get_time_ranges(times[day]):
                content += "{}{}-{}".format(sep, str(hour_range[0]), str(hour_range[1]))
                sep = ", "
        results.append((stoppingpoints, content))
    return results

