package com.jersey.model;

import java.util.Arrays;

public class BookingRequest {
	
	private int count;
	private Passenger[] passengersList;
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void setPassengersList(Passenger[] list) {
		this.passengersList = list;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public Passenger[] getPassengersList(){
		return this.passengersList;
	}
	
	@Override
	public String toString() {
	    return "BookingRequest [count=" + count + ", passengers=" + Arrays.toString(this.passengersList)+ "]";
	}
}
