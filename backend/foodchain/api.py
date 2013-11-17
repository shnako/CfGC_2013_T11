import json

from django.http import HttpResponse, HttpResponseForbidden
from django.contrib.auth import authenticate

from foodchain.models import Delivery, Drive

def header_auth(f):
    def inner(request, *args, **kwargs):
        username = request.META.get('HTTP_X_USERNAME')
        password = request.META.get('HTTP_X_PASSWORD')
        user = authenticate(username=username, password=password)
        if user is not None and user.is_active:
            request.user = user
        else:
            return HttpResponseForbidden('Login needed')
        return f(request, *args, **kwargs)
    return inner

@header_auth
def user_check(request):
    return HttpResponse('1')

@header_auth
def is_delivery_assigned(request):
    if Drive.objects.filter(driver=request.user).count() == 1:
        return HttpResponse('1')
    else:
        return HttpResponse('0')

@header_auth
def get_deliveries(request):
    drive = Drive.objects.get(driver=request.user)
    deliveries = drive.delivery_set.all()
    results = []
    for delivery in deliveries:
        results.append(dict(
            id=delivery.id,
            address=delivery.recipient.postcode,
            lat=delivery.recipient.lat,
            lng=delivery.recipient.lng,
            delivered=delivery.delivered,
            meals=[m.meal_type for m in delivery.recipient.meal_set.all()]
        ))
    return HttpResponse(json.dumps(results, indent=4))