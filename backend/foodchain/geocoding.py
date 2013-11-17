import json, time
import urllib, urllib2

def geocode(postcode=None, address=None):
    if address is not None:
        url_tpl = "http://maps.googleapis.com/maps/api/geocode/json?address={0}&sensor=false"
        url = url_tpl.format(urllib.quote_plus(address))
    else:
        url_tpl = "http://maps.googleapis.com/maps/api/geocode/json?components=country:UK|postal_code:{0}&sensor=false"
        url = url_tpl.format(urllib.quote_plus(postcode))
    req = urllib2.urlopen(url)
    result = json.loads(req.read())
    if result['status'] == 'ZERO_RESULTS':
        return None
    if result['status'] != 'OK':
        raise Exception('API call returned %s' % result['status'])
    res = result['results'][0]['geometry']['location']
    return res['lat'], res['lng']
