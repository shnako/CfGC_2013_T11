import math

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

    for r in recipients:
        r.kitchen = min(kitchens, key=lambda k: dist(r, k))
        r.kitchen.pool.append(r)

    for k in kitchens:
        print 'Kitchen', k.lat, k.lng
        r_set = set(k.pool)
        while r_set:
            first = min(r_set, key=lambda r: dist(k, r))
            r_set.remove(first)
            route = [first]
            meals_left = max_meals_per_drive - first.num_meals
            counter = 0
            while r_set:
                prev = route[-1]
                next_node = min(r_set, key=lambda r: dist(prev, r))
                new_set = r_set.copy()
                while meals_left != 0 and meals_left - next_node.num_meals < 0 and new_set:
                    counter_loop= 0
                    print meals_left
                    print meals_left - next_node.num_meals
                    print
                    new_set.remove(next_node)
                    if new_set:
                        next_node = min(new_set, key=lambda r: dist(prev, r))
                    counter_loop += 1
                    print 'loop: ', counter_loop
                route.append(next_node)
                meals_left -= next_node.num_meals
                r_set.remove(next_node)
                counter += 1
                print 'counter ', counter

            print 'Route', ', '.join('%s;%s' % (r.lat, r.lng) for r in route)


