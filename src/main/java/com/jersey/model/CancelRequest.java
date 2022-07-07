package com.jersey.model;

public class CancelRequest {
	public String pid;
	public String pnr;
	public String cabin_no;
	public String berth_type;
	
	@Override
	public String toString() {
	    return "CancelRequest [pid=" + pid + ", pnr=" + pnr+ ", cabin_no=" + cabin_no +", berth_type=" + berth_type + "]";
	}
}