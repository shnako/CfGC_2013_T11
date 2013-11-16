import csv

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
