package com.jersey.model;

import java.util.List;

@SuppressWarnings("unused")
public class StatusResponse extends GenericResponse {
	private List<PnrDetails> pnrs;
	
	public StatusResponse(String msg, int status, List<PnrDetails> pnrs){
		super(msg, status);
		this.pnrs = pnrs;
	}
	
}
