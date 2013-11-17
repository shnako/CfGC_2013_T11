from foodchain import models

def handle_scheduling(form_data):
    models.Drive.objects.all().delete()  # Delete current schedule
    
    algorithm = form_data.pop('algorithm')
    if algorithm == 'dummy':
        return dummy_scheduler(**form_data)
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
