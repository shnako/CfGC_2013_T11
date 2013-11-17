import csv
import itertools

from foodchain import models

def import_kitchens(csv_file):
    reader = csv.DictReader(csv_file.splitlines())
    items = list(reader)
    # TODO: do merging
    models.Kitchen.objects.all().delete()
    for item in items:
        if not item['Kitchen ID']:
            continue
        k = models.Kitchen()
        k.kitchen_id = item['Kitchen ID']
        k.address = item['Address']
        k.postcode = item['Postcode']
        k.capacity = int(item['Capacity'])
        k.save()

def import_recipients(csv_file):
    reader = csv.DictReader(csv_file.splitlines())
    items = list(reader)
    models.Recipient.objects.all().delete()
    print items
    for k, g in itertools.groupby(items, lambda item: item['Booking ID']):
        group = list(g)
        if not k:
            raise Exception('Booking ID contains empty value')
        recipient = models.Recipient()
        recipient.nickname = group[0]['Nickname'].decode('latin1')
        recipient.postcode = group[0]['Primary Postal Code']
        recipient.booking_id = k
        recipient.save()
        for meal in group:
            recipient.meal_set.create(meal_type=meal['Meal Type'], comment=meal['Comments'])
