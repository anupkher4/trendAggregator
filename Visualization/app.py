from flask import Flask
from flask import render_template
from pymongo import MongoClient
import json
from bson import json_util
from bson.json_util import dumps

app = Flask(__name__)

MONGODB_HOST = 'ds047911.mongolab.com'
MONGODB_PORT = 47911
DBS_NAME = 'cmpe273'
COLLECTION_NAME = 'fourSquareTrend'
#FIELDS = {'school_state': True, 'resource_type': True, 'poverty_level': True, 'date_posted': True, 'total_donations': True, '_id': False}

@app.route("/")
def index():
    return render_template("index.html")

@app.route("/twitter")
def test():
    return render_template("twitter.html")

@app.route("/instagramTrend")
def insta():
    return render_template("instagram.html")

@app.route("/trendaggregator/instagram")
def trendaggregator_instagram():
    connection = MongoClient(MONGODB_HOST, MONGODB_PORT)
    connection[DBS_NAME].authenticate('root','test123')
	#db.authenticate('root','test123')
    collection = connection[DBS_NAME]['instagramTrend']
    projects = collection.find({},{'_id': 1, 'url': 1, 'caption': 1,'commentCount': 1, 'likesCount': 1, 'created_time': 1, 'latitude': 1, 'longitude': 1})
    json_projects = []
    for project in projects:
        json_projects.append(project)
    json_projects = json.dumps(json_projects, default=json_util.default)
    connection.close()
	#MongoClient.close()
    return json_projects

@app.route("/trendaggregator/foursquare")
def trendaggregator_foursquare():
    connection = MongoClient(MONGODB_HOST, MONGODB_PORT)
    connection[DBS_NAME].authenticate('root','test123')
	#db.authenticate('root','test123')
    collection = connection[DBS_NAME]['fourSquareTrend']
    projects = collection.find({},{'name': 1,'category': 1, 'checkinsCounts': 1, 'usersCount': 1, 'state': 1, 'insertedTime': 1, '_id': 1})
    json_projects = []
    for project in projects:
        json_projects.append(project)
    json_projects = json.dumps(json_projects, default=json_util.default)
    connection.close()
	#MongoClient.close()
    return json_projects

@app.route("/trendaggregator/twitter")
def twitter():
    connection = MongoClient(MONGODB_HOST, MONGODB_PORT)
    connection[DBS_NAME].authenticate('root','test123')
	#db.authenticate('root','test123')
    collection = connection[DBS_NAME]['twitterTrend']
    projects = collection.find({},{'name': 1,'created_at': 1, 'as_of': 1, 'url': 1, '_id': 1})
    json_projects = []
    for project in projects:
        json_projects.append(project)
    json_projects = json.dumps(json_projects, default=json_util.default)
    connection.close()
	#MongoClient.close()
    return json_projects
if __name__ == "__main__":
    app.run(host='0.0.0.0',port=5000,debug=True)
