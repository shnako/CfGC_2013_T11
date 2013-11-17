from django.db import models
from django.contrib.auth.models import User

# Create your models here.

class Kitchen(models.Model):
    kitchen_id = models.CharField(max_length=255)
    address = models.CharField(max_length=255)
    postcode = models.CharField(max_length=50)
    
    capacity = models.IntegerField()
    
    lat = models.FloatField(blank=True, null=True)
    lng = models.FloatField(blank=True, null=True)
    
    def __unicode__(self):
        return self.kitchen_id


class Recipient(models.Model):
    nickname = models.CharField(max_length=255)
    postcode = models.CharField(max_length=50)
    booking_id = models.CharField(max_length=255)
    
    lat = models.FloatField(blank=True, null=True)
    lng = models.FloatField(blank=True, null=True)
    
    def __unicode__(self):
        return ', '.join([self.nickname, self.postcode])


class Meal(models.Model):
    recipient = models.ForeignKey(Recipient)
    meal_type = models.CharField(max_length=255)
    comment = models.TextField(blank=True)
    
    def __unicode__(self):
        return self.meal_type


class Drive(models.Model):
    kitchen = models.ForeignKey(Kitchen)
    meals_to_deliver = models.IntegerField(null=True, blank=True)
    driver = models.ForeignKey(User, null=True, blank=True)
    
    def __unicode__(self):
        return 'Drive #%d' % self.id

class Delivery(models.Model):
    class Meta:
        ordering = ['order']
    
    drive = models.ForeignKey(Drive)
    order = models.IntegerField()
    recipient = models.ForeignKey(Recipient)
    delivered = models.BooleanField(default=False)
    
    def __unicode__(self):
        return '#%d [%s]%s' % (self.order, unicode(self.recipient), ' delivered' if self.delivered else '')
