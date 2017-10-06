from django.conf.urls import url
from . import views

urlpatterns = [
    # All crud operations API endpoints
    url(r'^classifier/$', views.ImageView.as_view({'post': 'classify_image'})),
    url(r'^image-info/$', views.ImageView.as_view({'post': 'image_info'})),
]
