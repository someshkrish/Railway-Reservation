package com.jersey.model;

public class BookingResponse extends GenericResponse {
    private int success;
    private int failure;
    private String allotedPnr;
    
    public BookingResponse(String msg, int status, int success, int failure, String allotedPnr) {
    	super(msg, status);
    	this.success = success;
    	this.failure = failure;
    	this.allotedPnr = allotedPnr;
    }
    
    public void setSuccess(int s) {
    	this.success = s;
    }
    
    public void setFaiure(int f) {
    	this.failure = f;
    }
    
    public void setAllotedPnr(String ap) {
    	this.allotedPnr = ap;
    }
    
    public int getSuccess() {
    	return this.success; 
    }
    
    public int getFailure() {
    	return this.failure;
    }
    
    public String getAllotedPnr() {
    	return this.allotedPnr;
    }
	
}
