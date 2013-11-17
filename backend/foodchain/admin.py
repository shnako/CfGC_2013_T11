from django.contrib import admin
from django.http import HttpResponse
from django.conf.urls import url, patterns
from django.shortcuts import render, redirect
from django import forms

from foodchain.models import Kitchen, Recipient, Meal
from foodchain import csv_import

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
    list_display = ['booking_id', 'nickname', 'postcode']
    inlines = [MealInline]
    
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
