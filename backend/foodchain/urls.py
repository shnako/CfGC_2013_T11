from django.conf.urls import patterns, include, url


urlpatterns = patterns('',
    url(r'^api/user_check$', 'foodchain.api.user_check'),
    url(r'^api/is_delivery_assigned$', 'foodchain.api.is_delivery_assigned')
)
