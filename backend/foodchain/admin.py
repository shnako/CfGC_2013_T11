import itertools
import json

from django.contrib import admin, messages
from django.http import HttpResponse
from django.conf.urls import url, patterns
from django.shortcuts import render, redirect
from django import forms

from foodchain.models import Kitchen, Recipient, Meal, Drive
from foodchain import csv_import, routing
from foodchain.forms import PlannerParametersForm

# Register your models here.

class CSVForm(forms.Form):
    file = forms.FileField()


class KitchenAdmin(admin.ModelAdmin):
    list_display = ['kitchen_id', 'address', 'postcode', 'capacity']
    
    def get_urls(self):
        urls = super(KitchenAdmin, self).get_urls()
        my_urls = patterns('',
            url(r'^import_csv/$', self.admin_site.admin_view(self.import_csv), name='kitchen-import-csv')
        )
        return my_urls + urls
    
    def import_csv(self, request):
        if request.method == 'POST':
            form = CSVForm(request.POST, request.FILES)
            if form.is_valid():
                csv_contents = form.cleaned_data['file'].read()
                csv_import.import_kitchens(csv_contents)
                self.message_user(request, 'Import succeeded')
                return redirect('admin:foodchain_kitchen_changelist')
        else:
            form = CSVForm()
        return render(request, 'admin/foodchain/import_csv.html', { 'form': form })

admin.site.register(Kitchen, KitchenAdmin)


class MealInline(admin.TabularInline):
    model = Meal
    extra = 0

class RecipientAdmin(admin.ModelAdmin):
    list_display = ['booking_id', 'nickname', 'postcode', 'coordinates']
    inlines = [MealInline]
    
    def coordinates(self, obj):
        if not obj.lat or not obj.lng:
            return '(none)'
        return '%.4f, %.4f' % (obj.lat, obj.lng)
    
    def get_urls(self):
        urls = super(RecipientAdmin, self).get_urls()
        my_urls = patterns('',
            url(r'^import_csv/$', self.admin_site.admin_view(self.import_csv), name='recipient-import-csv')
        )
        return my_urls + urls
    
    def import_csv(self, request):
        if request.method == 'POST':
            form = CSVForm(request.POST, request.FILES)
            if form.is_valid():
                csv_contents = form.cleaned_data['file'].read()
                csv_import.import_recipients(csv_contents)
                self.message_user(request, 'Import succeeded')
                return redirect('admin:foodchain_recipient_changelist')
        else:
            form = CSVForm()
        return render(request, 'admin/foodchain/import_csv.html', { 'form': form })

admin.site.register(Recipient, RecipientAdmin)

class DriveAdmin(admin.ModelAdmin):
    list_display = ['__str__', 'kitchen', 'meals_to_deliver', 'route', 'driver']
    list_editable = ['driver']
    
    def route(self, obj):
        return '<br />'.join(unicode(delivery) for delivery in obj.delivery_set.all())
    route.allow_tags = True
    
    def get_urls(self):
        urls = super(DriveAdmin, self).get_urls()
        my_urls = patterns('',
            url(r'^planner/$', self.admin_site.admin_view(self.planner_view), name='route-planner')
        )
        return my_urls + urls
    
    def planner_view(self, request):
        if request.POST.get('clear_plan'):
            Drive.objects.all().delete()
            return redirect('admin:route-planner')
        
        if request.method == 'POST':
            form = PlannerParametersForm(request.POST)
            if form.is_valid():
                routing.handle_scheduling(form.cleaned_data)
                self.message_user(request, 'Schedule updated successfully')
                return redirect('admin:route-planner')
        else:
            form = PlannerParametersForm()
        
        unmapped_kitchens = Kitchen.objects.filter(lat=None)
        if unmapped_kitchens:
            self.message_user(request,
                              '%d kitchens have no associated coordinates' % len(unmapped_kitchens),
                              level=messages.ERROR)
        
        unmapped_recipients = Recipient.objects.filter(lat=None)
        if unmapped_recipients:
            self.message_user(request,
                              '%d recipients have no associated coordinates' % len(unmapped_recipients),
                              level=messages.ERROR)
        
        drives = list(Drive.objects.all())
        drives.sort(key=lambda d: d.kitchen.kitchen_id)
        
        kitchens = []
        for kitchen, d in itertools.groupby(drives, key=lambda d: d.kitchen):
            d = list(d)
            kitchen.num_meals = sum(x.meals_to_deliver for x in d)
            kitchen.num_drives = len(d)
            kitchens.append(kitchen)
        kitchens.sort(key=lambda k: k.kitchen_id)
        
        js_data = {
            'kitchens': list(Kitchen.objects.filter(lat__isnull=False).values('kitchen_id', 'lat', 'lng')),
            'recipients': list(Recipient.objects.filter(lat__isnull=False).values('nickname', 'booking_id', 'lat', 'lng')),
            'drives': list(([(d.kitchen.lat, d.kitchen.lng)] + [(x.recipient.lat, x.recipient.lng) for x in d.delivery_set.all()]) for d in drives)
        }
        
        return render(request, 'admin/foodchain/planner.html', {
            'form': form,
            'drives': drives,
            'kitchens': kitchens,
            'js_data': json.dumps(js_data),
        })

admin.site.register(Drive, DriveAdmin)

