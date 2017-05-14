from flask import Flask
import codecs

app = Flask(__name__)
pageContent = open('main.html', 'rb').read().decode('utf-8')

@app.route('/')
def index():
    return pageContent

@app.route('/api/getBadBusStops/', methods=['GET'])
def getBadBusStops():
    with codecs.open("default.json",'r',encoding='utf8') as f:
        text = f.read()
    return text

if __name__ == '__main__':
   app.run()