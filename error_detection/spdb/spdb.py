from flask import Flask, jsonify
import codecs
from database import Database
from datatypes import *
from analyzes import *
import simplejson as json

app = Flask(__name__)
pageContent = open('main.html', 'rb').read().decode('utf-8')

@app.route('/')
def index():
    return pageContent

@app.route('/api/getBadBusStops/', methods=['GET'])
def getBadBusStops():
    with codecs.open("default.json",'r',encoding='utf8') as f:
        text = f.read()
    #return text
    lines = []
    line = Line(1, "Kabaty", [])
    stop = Stop(1, 122134.1421, 122134.1421, 122134.1421, 122134.1421, "Mlociny")
    line.stops.append(stop)
    stop2 = Stop(2, 122134.1421, 122134.1421, 122134.1421, 122134.1421, "Kabaty")
    line.stops.append(stop2)
    line2 = Line(2, "Kabaty", [])
    line2.stops.append(stop)
    line2.stops.append(stop2)
    lines.append(line2)
    lines.append(line)
    return jsonify(lines)

def getDayCourseTraceWithSpeedsList(day_course_id):
    one_course_trace_list = db.getTraceForDayCourse(day_course_id)
    one_course_trace_with_speeds_list = []
    for idx, val in enumerate(one_course_trace_list):
        if(idx < len(one_course_trace_list) - 1):
            sp = speed(val.point, one_course_trace_list[idx+1].point, (one_course_trace_list[idx+1].datetime-val.datetime).total_seconds())
            if(sp < 3):
                one_course_trace_with_speeds = TraceWithSpeeds(pointBetween(val.point, one_course_trace_list[idx+1].point), sp)
                one_course_trace_with_speeds_list.append(one_course_trace_with_speeds)
    for row in one_course_trace_with_speeds_list:
        print(row)
    return one_course_trace_with_speeds_list


if __name__ == '__main__':
    db = Database("back")
    #app.run()
    core = Core(db)
    #core.analyzeStop(9)
    list = core.analyzeAllStops()
    file = open("list.json", "w")
    file.write(json.dumps(list, namedtuple_as_object=True, indent=2, ensure_ascii=False))
    file.close()

    

    