import math
import urllib2
import json

from foodchain import models

def handle_scheduling(form_data):
    models.Drive.objects.all().delete()  # Delete current schedule

    algorithm = form_data.pop('algorithm')
    if algorithm == 'dummy':
        return dummy_scheduler(**form_data)
    elif algorithm == 'smart':
        return smart_scheduler(**form_data)
    raise Exception('Algorithm not implemented')

def dummy_scheduler(**params):
    max_journey_duration = params.pop('max_journey_duration')
    max_meals_per_drive = params.pop('max_meals_per_drive')

    kitchens = models.Kitchen.objects.all()
    recipients = models.Recipient.objects.all()

    # Create one drive per each recipient
    the_kitchen = kitchens[0]
    for recipient in recipients:
        drive = models.Drive()
        drive.kitchen = the_kitchen
        drive.save()

        drive.delivery_set.create(order=1, recipient=recipient)

        drive.meals_to_deliver = sum(d.recipient.meal_set.count() for d in drive.delivery_set.all())
        drive.save()

def dist(a, b):
    return math.hypot(a.lat - b.lat, a.lng - b.lng)

def smart_scheduler(max_journey_duration, max_meals_per_drive):
    kitchens = models.Kitchen.objects.all()
    recipients = models.Recipient.objects.all()
    for r in recipients:
        r.num_meals = r.meal_set.count()

    for k in kitchens:
        k.pool = []
        k.remaining = k.capacity

    for r in recipients:
        r.kitchen = min((k for k in kitchens if k.remaining >= r.num_meals),
                        key=lambda k: dist(r, k))
        r.kitchen.pool.append(r)
        r.kitchen.remaining -= r.num_meals
        #print r, r.kitchen, r.kitchen.remaining

    for k in kitchens:
        r_set = set(k.pool)
        while r_set:
            first = min(r_set, key=lambda r: dist(k, r))
            r_set.remove(first)
            route = [first]
            meals_left = max_meals_per_drive - first.num_meals

            while r_set:
                prev = route[-1]
                next_node = min(r_set, key=lambda r: dist(prev, r))
                if meals_left - next_node.num_meals < 0 or len(route) > 7:
                    break
                route.append(next_node)
                meals_left -= next_node.num_meals
                r_set.remove(next_node)

            while True:
                req_string='http://maps.googleapis.com/maps/api/directions/json?origin='+k.postcode.replace(' ','%20')+',UK'
                if (len(route) > 1):
                    pipe_str=''
                    for rt in route:
                        pipe_str+=rt.postcode.replace(' ','%20')+',UK'+'|'
                    pipe_str=pipe_str[:-1]
                req_string=req_string+'&destination='+route[-1].postcode.replace(' ','%20')+',UK'
                if (len(route) > 1):
                    req_string=req_string+'&waypoints='+pipe_str
                req_string=req_string+'&sensor=false'

                request = urllib2.urlopen(req_string)
                r = json.loads(request.read())
                #print req_string
                sum_time = 0;
                if len(r['routes']) > 0:
                    for step in r['routes'][0]['legs']:

                        sum_time +=step['duration']['value']
                #print sum_time
                if (sum_time>120*60-(10*60*len(route))):
                    r_set.add(route[-1])
                    route = route[:-1]
                else:
                    break

            print 'Route added'
            drive = models.Drive()
            drive.kitchen = k
            drive.duration = sum_time
            drive.save()

            for i, r in enumerate(route, start=1):
                drive.delivery_set.create(order=i, recipient=r)

            drive.meals_to_deliver = sum(d.recipient.meal_set.count() for d in drive.delivery_set.all())
            drive.save()


