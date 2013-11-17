from django.http import HttpResponse, HttpResponseForbidden
from django.contrib.auth import authenticate

from foodchain.models import Delivery

def header_auth(f):
    def inner(request, *args, **kwargs):
        print request.META
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
