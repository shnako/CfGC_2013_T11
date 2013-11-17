from django.db import models

# Create your models here.

class Kitchen(models.Model):
    kitchen_id = models.CharField(max_length=255)
    address = models.CharField(max_length=255)
    postcode = models.CharField(max_length=50)
    
    capacity = models.IntegerField()
    
    lat = models.FloatField(blank=True, null=True)
    lng = models.FloatField(blank=True, null=True)


class Recipient(models.Model):
    nickname = models.CharField(max_length=255)
    postcode = models.CharField(max_length=50)
    booking_id = models.CharField(max_length=255)
    
    lat = models.FloatField(blank=True, null=True)
    lng = models.FloatField(blank=True, null=True)


class Meal(models.Model):
    recipient = models.ForeignKey(Recipient)
    meal_type = models.CharField(max_length=255)
    comment = models.TextField(blank=True)


class Drive(models.Model):
    kitchen = models.ForeignKey(Kitchen)
    meals_to_deliver = models.IntegerField(null=True, blank=True)

class Delivery(models.Model):
    class Meta:
        ordering = ['order']
    
    drive = models.ForeignKey(Drive)
    order = models.IntegerField()
    recipient = models.ForeignKey(Recipient)
    delivered = models.BooleanField(default=False)