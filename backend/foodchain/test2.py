import urllib2
import json


request = urllib2.urlopen('http://maps.googleapis.com/maps/api/directions/json?origin=SE22%208AH&destination=SE23%203LZ&waypoints=SE15%204LB|SE15%203LF|SE15%203EJ|SE4%202HU|SE23%203LZ&sensor=false')
r = json.loads(request.read())
sum = 0;
for step in r['routes'][0]['legs']:
    
    sum +=step['duration']['value']
print sum
#print r['routes'][0]['legs'][0]['duration']['value']

