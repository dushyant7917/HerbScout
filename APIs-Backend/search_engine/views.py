# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework.decorators import detail_route
from rest_framework import status
from django.core.exceptions import ObjectDoesNotExist
from elasticsearch import Elasticsearch
import json
from pymongo import MongoClient
from django.utils.text import slugify
from math import sqrt

client = MongoClient('localhost:27017')
db = client.herb_data

es = Elasticsearch()

states_data = {}
with open('./data/states.txt', 'r') as states:
    for s in states:
        state_info = s.strip("\n").split('|')
        states_data[state_info[0]] = {}
        states_data[state_info[0]]['latitude'] = state_info[1]
        states_data[state_info[0]]['longitude'] = state_info[2]

states.close()

# Create your views here.
class HerbView(viewsets.ModelViewSet):

    @detail_route(methods=['get'])
    def herb_info(self, request, *args, **kwargs):
        herb_botanical_name = kwargs['herb_name_slug'].split('-')
        name_1 = herb_botanical_name[0]
        name_2 = herb_botanical_name[1]
        botanical_name = name_1 + " " + name_2
        herb_data = db.information.find_one({"botanical_name": botanical_name.capitalize()})

        herb_info = {}
        if herb_data != None:
            herb_info = {
                'parts_used': herb_data['parts_used'],
                'properties': herb_data['properties'],
                'places': herb_data['places'],
                'botanical_name': herb_data['botanical_name']
            }

            map_info = []
            for s in herb_info['places']:
                marker = {}
                marker['latitude'] = states_data[s]['latitude']
                marker['longitude'] = states_data[s]['longitude']
                map_info.append(marker)

            query = ""
            for prop in herb_info['properties']:
                query += " " + prop.replace(" disorders", "")

            search_doc = {
                "size": 6,
                "query": {
                    "dis_max": {
                        "queries": [
                            {
                                "match": {
                                    "parts_used": {
                                        "query": query,
                                        "boost": 4.0
                                    }
                                }
                            },
                            {
                                "match": {
                                    "properties": {
                                        "query": query,
                                        "boost": 4.0
                                    }
                                }
                            }
                        ]
                    }
                },
                "sort": [
                    { "_score": { "order": "desc" }}
                ]
            }

            similar_properties = es.search(index = "herbs_info", doc_type = "doc", body = json.dumps(search_doc), sort = "_score")

            recommendations = []
            for similar in similar_properties['hits']['hits']:
                item = {}
                if similar['_source']['botanical_name'] != herb_data['botanical_name']:
                    item['botanical_name'] = similar['_source']['botanical_name'].replace(" ","-").lower()
                    recommendations.append(item)

        if herb_data is not None:
            content = {
                "found": True,
                "herb_data": herb_info,
                "map_info": map_info,
                "recommendations": recommendations
            }
            return Response(content, status=status.HTTP_200_OK)
        else:
            content = {
                "found": False,
                "herb_data": [],
                "map_info": [],
                "recommendations": []
            }
            return Response(content, status=status.HTTP_200_OK)
