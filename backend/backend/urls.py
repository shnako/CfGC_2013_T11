from django.conf.urls import patterns, include, url

from foodchain.models import Kitchen, Recipient
from rest_framework import viewsets, routers

from django.contrib import admin
admin.autodiscover()


class KitchenViewSet(viewsets.ModelViewSet):
    model = Kitchen

class RecipientViewSet(viewsets.ModelViewSet):
    model = Recipient

router = routers.DefaultRouter()
router.register(r'kitchens', KitchenViewSet)
router.register(r'recipients', RecipientViewSet)

urlpatterns = patterns('',
    url(r'^admin/', include(admin.site.urls)),
    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
)
