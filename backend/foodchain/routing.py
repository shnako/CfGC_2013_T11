from foodchain import models

def dummy_scheduler(**params):
    max_journey_duration = params.pop('max_journey_duration')
    max_meals_per_drive = params.pop('max_meals_per_drive')
    
