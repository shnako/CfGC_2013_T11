from django import forms

ALGORITHMS = [
    ('dummy', 'Dummy algorithm'),
    ('smart', 'Smart algorithm')
]

class PlannerParametersForm(forms.Form):
    algorithm = forms.ChoiceField(choices=ALGORITHMS, initial='smart')
    
    max_journey_duration = forms.IntegerField(label='Maximum allowed journey duration', initial=120)
    max_meals_per_drive = forms.IntegerField(label='Maximum number of meals per drive', initial=12)
