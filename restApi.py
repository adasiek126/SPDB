#!flask/bin/python
from flask import Flask, jsonify, make_response
from delay_info import *
from data_proxy import DataProxy, DelayGainDataIterator
from results_visualization import *

app = Flask(__name__)
pageContent = open('mapPage.html', 'rb').read().decode('utf-8')
data_proxy = DataProxy()


def delay_to_color(delay):
    if delay<0:
        return '#0000FF'
    if delay> 60 * 5:
        return '#FF5400'
    if delay > 60 * 2:
        return '#FFa500'
    if delay > 60:
        return '#E1FF00'
    if delay > 30:
        return '#BBFF00'
    else:
        return '#00FF00'


def mark_delays(delays):
    routes=[]
    for origin_point, destination_point, mean in delays:
        routes.append({
            'origin': {'lat': float(origin_point[0]), 'lng': float(origin_point[1])},
            'destination': {'lat': float(destination_point[0]), 'lng': float(destination_point[1])},
            'color': delay_to_color(mean)
        })
    return routes


def mark_delay_routes(drs):
    result = []
    for route in drs:
        result.append({'points': route.route, 'color': delay_to_color(route.delay)})
    return result


@app.route('/')
def index():
    return pageContent


def line_ordering(line_name):
    try:
        i = int(line_name)
    except ValueError:
        i = float("inf")
    return i, line_name


@app.route('/api/getLines/', methods=['GET'])
def getLines():
    raw_results = engine.execute("select name from csipline")
    return jsonify(list(sorted([name for name, in raw_results], key=line_ordering)))


@app.route('/api/getLinesTraffic/<day>/<int:hourFrom>/<int:hourTo>/<string:lineName>', methods=['GET'])
def getLinesTraffic(day, hourFrom, hourTo, lineName):
    if hourFrom>hourTo:
        return make_response(jsonify({'error': 'Bad arguments! HourFrom param must be not greater than hourTo param!'}), 404)
    #delays = get_mean_delays(hourFrom, hourTo, day, lineName)
    delays = data_proxy.get_mean_delay_gain(hourFrom, hourTo, day, lineName)
    #delays = get_mean_delays(14, 15, "tue")
    print("Marking delays.")
    routes = mark_delay_routes(get_delay_routes(delays))
    print("Plotting histograms")
    plot_histogram(data_proxy.get_delay_gain_histogram(hourFrom, hourTo, day, lineName))
    print("Done.")
    return jsonify(routes)

from analyses import *
@app.route('/api/getRoutes/<day>/<int:hourFrom>/<int:hourTo>', methods=['GET'])
def getRoutes(day, hourFrom, hourTo):
    if hourFrom>hourTo:
        return make_response(jsonify({'error': 'Bad arguments! HourFrom param must be not greater than hourTo param!'}), 404)
    print("Preparing info about delays.")
    print(hourFrom, hourTo, day)
    #delays = get_mean_delays(hourFrom, hourTo, day)
    delays = data_proxy.get_mean_delay_gain(hourFrom, hourTo, day)
    print("Marking delays.")
    routes = mark_delay_routes(data_proxy.get_delay_routes(delays))
    print("Plotting histograms")
    plot_histogram(data_proxy.get_delay_gain_histogram(hourFrom, hourTo, day))
    print("Done.")
    return jsonify(routes)
    #jsonify(mark_delays(get_mean_delays(hourFrom, hourTo, day)))
    #return jsonify(routes)


@app.route('/api/getHistogram/<day>/<int:hourFrom>/<int:hourTo>', methods=['GET'])
def getHistogram(day, hourFrom, hourTo):
    if hourFrom > hourTo:
        return make_response(jsonify({'error': 'Bad arguments! HourFrom param must be not greater than hourTo param!'}),
                             404)
    img = plot_histogram(data_proxy.get_delay_gain_histogram(hourFrom, hourTo, day))
    return img.getvalue()


@app.route('/api/getHistogramLine/<day>/<int:hourFrom>/<int:hourTo>/<string:line>', methods=['GET'])
def getHistogramLine(day, hourFrom, hourTo, line):
    if hourFrom > hourTo:
        return make_response(jsonify({'error': 'Bad arguments! HourFrom param must be not greater than hourTo param!'}),
                             404)
    img = plot_histogram(data_proxy.get_delay_gain_histogram(hourFrom, hourTo, day, line))
    return img.getvalue()


@app.route('/api/getStretches/<float:minSupport>/<int:minDiffDelay>/', methods=['GET'])
def getStretches(minSupport, minDiffDelay):
    group_lags = find_group_lags(DelayGainDataIterator(data_proxy), minDiffDelay, minSupport)
    data = []
    for stretch in group_lags:
        points_pair = stretch[0]
        data.append(
            {
                "points": get_route(points_pair),
                "color": "#FF0000",
                "content": stretch[1]
            }
        )
    return jsonify(data)

if __name__ == '__main__':
    app.run(debug=True)

