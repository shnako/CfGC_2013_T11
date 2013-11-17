import csv
import itertools
import time

from geocoding import geocode
from foodchain import models

def import_kitchens(csv_file):
    reader = csv.DictReader(csv_file.splitlines())
    items = list(reader)

    models.Kitchen.objects.all().delete()
    for item in items:
        if not item['Kitchen ID']:
            continue
        k = models.Kitchen()
        k.kitchen_id = item['Kitchen ID']
        k.address = item['Address']
        k.postcode = item['Postcode']
        k.capacity = int(item['Capacity'])
        k.lat, k.lng = geocode(address=k.address) or (None, None)
        k.save()
        
        time.sleep(0.3)

def import_recipients(csv_file):
    reader = csv.DictReader(csv_file.splitlines())
    items = list(reader)
    models.Recipient.objects.all().delete()
    
    for k, g in itertools.groupby(items, lambda item: item['Booking ID']):
        group = list(g)
        if not k:
            raise Exception('Booking ID contains empty value')
        recipient = models.Recipient()
        recipient.nickname = group[0]['Nickname'].decode('latin1')
        recipient.postcode = group[0]['Primary Postal Code']       	
        recipient.booking_id = k
        if recipient.postcode:
            recipient.lat, recipient.lng = geocode(recipient.postcode) or (None, None)
        recipient.save()
        for meal in group:
            recipient.meal_set.create(meal_type=meal['Meal Type'], comment=meal['Comments'])
        
        time.sleep(0.3)
