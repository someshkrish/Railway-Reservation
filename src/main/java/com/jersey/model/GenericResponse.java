package com.jersey.model;

public class GenericResponse {
    private int status;
    private String msg;
    
    GenericResponse(String msg, int status) {
    	this.msg = msg;
    	this.status = status;
    }
    
    public GenericResponse() {
		// TODO Auto-generated constructor stub
	}

	public void setStatus(int status) {
    	this.status = status;
    }
    
    public void setMsg(String msg) {
    	this.msg = msg;
    }
    
    public int getStatus() {
    	return this.status;
    }
    
    public String getMsg() {
    	return this.msg;
    }
}
