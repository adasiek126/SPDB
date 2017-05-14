from flask import Flask
app = Flask(__name__)
pageContent = open('main.html', 'rb').read().decode('utf-8')

@app.route('/')
def index():
    return pageContent

@app.route('/api/getBadBusStops/', methods=['GET'])
def getBadBusStops():
    default = open("default.json", "r")
    return default.read()

if __name__ == '__main__':
   app.run()