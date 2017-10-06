# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework.decorators import detail_route
from rest_framework import status
from django.core.exceptions import ObjectDoesNotExist
import base64
from . import label_image
import os

# Create your views here.
class ImageView(viewsets.ModelViewSet):

    @detail_route(methods=['POST'])
    def classify_image(self, request):
        image_string = request.data.get('image')
        image_data = base64.b64decode(image_string)
        filename = 'classifier/Classification_Image/test_image.jpg'
        with open(filename, 'wb') as f:
            f.write(image_data)

        result = label_image.LabelImage(filename)

        os.remove(filename)

        if result[1] > 0.6:
            return Response({"identified": True, "result": result}, status=status.HTTP_200_OK)
        else:
            return Response({"identified": False, "result": "Sorry can't identify herb!" }, status=status.HTTP_200_OK)

    @detail_route(methods=['POST'])
    def image_info(self, request):
        herb_name = request.data.get('herb_name')
        return Response({'herb_name': herb_name}, status=status.HTTP_200_OK)
