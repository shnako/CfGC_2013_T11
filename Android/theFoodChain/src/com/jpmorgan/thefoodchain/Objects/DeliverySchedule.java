package com.jpmorgan.thefoodchain.Objects;

import java.util.ArrayList;

public class DeliverySchedule {
	private ArrayList<Delivery> deliveries;
	private short currentDelivery;
	
	public DeliverySchedule(){
		deliveries = new ArrayList<Delivery>();
		currentDelivery = 0;
	}
	
	public void addDelivery(Delivery delivery){
		deliveries.add(delivery);
	}
	
	/**
	 * 
	 * @return false if all deliveries finished, true otherwise!
	 */
	public boolean nextDelivery() {
		deliveries.get(currentDelivery++).delivered();
		if (currentDelivery >= deliveries.size()){
			return false;
		}
		
		return true;
	}
	
	public boolean skip(){
		deliveries.get(currentDelivery++).skipped();
		currentDelivery++;
		if (currentDelivery >= deliveries.size()){
			return false;
		}
		
		deliveries.add(new Delivery(deliveries.get(currentDelivery - 1)));
		
		return true;
	}
	
	public String getAddress(){
		return deliveries.get(currentDelivery).getAddress();
	}
	
	public String getCurrentDeliveryMeals() {
		return deliveries.get(currentDelivery).getMealsString();
	}
	
	public double getCurrentLatitude() {
		return deliveries.get(currentDelivery).getLatitude();
	}
	
	public double getCurrentLongitude() {
		return deliveries.get(currentDelivery).getLongitude();
	}
	
	public int getCount() {
		return deliveries.size();
	}
	
	public String getStatus() {
		return deliveries.get(currentDelivery).getStatus();
	}
}
