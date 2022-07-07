package com.jersey.model;

public class CancelResponse extends GenericResponse {	
	public String pid;
	public String pnr;
	public String cabinNo;
	public String berthType;
	
	public CancelResponse(String msg, int status, String pid, String pnr, String cabinNo, String berthType) {
		super(msg, status);
		this.pid = pid;
		this.pnr = pnr;
		this.cabinNo = cabinNo;
		this.berthType = berthType;
	}
}
