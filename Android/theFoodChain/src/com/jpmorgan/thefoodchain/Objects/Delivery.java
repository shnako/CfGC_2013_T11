package com.jpmorgan.thefoodchain.Objects;

import java.util.HashMap;

public class Delivery {
	public static final String STATUS_PENDING = "Pending", STATUS_DELIVERED = "Delivered", STATUS_SKIPPED = "Skipped", STATUS_RESCHEDULED = "Rescheduled"; 
	private String address, status;
	private double longitude, latitude;
	private boolean delivered;
	HashMap<String, Integer> meals = new HashMap<String, Integer>();
	
	//Constructor.
	public Delivery(String address, double longitude, double latitude){
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		delivered = false;
		status = STATUS_PENDING;
	}
	
	//Copy constructor.
	public Delivery(Delivery delivery) {
		this.address = delivery.address;
		this.longitude = delivery.longitude;
		this.latitude = delivery.latitude;
		delivered = delivery.isDelivered();
		this.meals = delivery.getMeals();
	}
	
	public void addMeals(String meal){
		if (meals.containsKey(meal)){
			meals.put(meal, meals.get(meal) + 1);
		} else {
			meals.put(meal, 1);
		}
	}
	
	public String getAddress(){
		return address;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public HashMap<String, Integer> getMeals(){
		return meals;
	}
	
	//Parse the meals hashmap to a string for output.
	public String getMealsString(){
		String result = "";
		for (String s : meals.keySet()){
			result += s + ": " + meals.get(s) + "\n";
		}
		
		return result;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void skipped() {
		status = STATUS_SKIPPED;
	}
	
	public void delivered(){
		delivered = true;
		status = STATUS_DELIVERED;
	}
	
	public boolean isDelivered(){
		return delivered;
	}
}
