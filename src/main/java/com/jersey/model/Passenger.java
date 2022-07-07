package com.jersey.model;

public class Passenger {
    private String name;
    private int age;
    private String preferred_berth;
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public void setAge(int age) {
    	this.age = age;
    }
    
    public void setPreferredBerth(String pb) {
    	this.preferred_berth = pb;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public int getAge() {
    	return this.age;
    }
    
    public String getPreferredBerth() {
    	return this.preferred_berth;
    }
}
