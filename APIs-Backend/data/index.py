from elasticsearch import Elasticsearch
from pymongo import MongoClient

client = MongoClient('localhost:27017')
db = client.herb_data

data = db.information.find()

es = Elasticsearch()
counter = 0
for i in data:
    es.index('herbs_info', 'doc', {
        'botanical_name': i['botanical_name'],
        'places': i['places'],
        'parts_used': i['parts_used'],
        'properties': i['properties']})
    print counter
    counter += 1
