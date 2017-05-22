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


@app.route('/api/getBusStops', methods=['GET'])
def getBusStops():
    list = db.getAllStopPoints()
    for el in list:
        el.lines = core.getLinesForStopId(el.id)
    return(json.dumps(list, namedtuple_as_object=True, indent=2, ensure_ascii=False))    
    

@app.route('/api/getBadBusStops/<int:grouping_distance>/<int:search_distance>/<int:speed_threshold>', methods=['GET'])
def getBadBusStops(grouping_distance, search_distance, speed_threshold):
    print(0.001 * grouping_distance)
    print(0.001 * search_distance)
    print(0.001 * speed_threshold)
    core.group_distance = 0.001 * grouping_distance
    core.search_distance = 0.001 * search_distance
    core.speed_threshold = 0.001 * speed_threshold
    list = core.analyzeAllStops()
    return(json.dumps(list, namedtuple_as_object=True, indent=2, ensure_ascii=False))

@app.route('/api/getBadBusStop/<int:id>/<int:grouping_distance>/<int:search_distance>/<int:speed_threshold>', methods=['GET'])
def getBadBusStop(id, grouping_distance, search_distance, speed_threshold):
    print(0.001 * grouping_distance)
    print(0.001 * search_distance)
    print(0.001 * speed_threshold)
    core.group_distance = 0.001 * grouping_distance
    core.search_distance = 0.001 * search_distance
    core.speed_threshold = 0.001 * speed_threshold
    list = core.analyzeOneStop(id)
    return(json.dumps(list, namedtuple_as_object=True, indent=2, ensure_ascii=False))


if __name__ == '__main__':
    db = Database("back")
    core = Core(db, 0.2, 0.02, 0.1)
    app.run()
    #list = core.analyzeAllStops()
    #list = db.getAllStopPoints()
    #file = open("list.json", "w")
    #file.write(json.dumps(list, namedtuple_as_object=True, indent=2, ensure_ascii=False))
    #file.close()

    

    