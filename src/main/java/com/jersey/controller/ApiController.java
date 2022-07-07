package com.jersey.controller;

import com.google.gson.Gson;
import com.jersey.handler.LoginHandler;
import com.jersey.handler.PnrStatusRetrieverHandler;
import com.jersey.handler.SignupHandler;
import com.jersey.handler.TicketCancellationHandler;
import com.jersey.handler.BookticketHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.jersey.model.User;
import com.jersey.model.BookingRequest;
import com.jersey.model.GenericResponse;

@Path("/api/v1")
public class ApiController {
	@GET
	@Path("/test")
	public String test() {
		return "It's working...";
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user, @Context HttpServletRequest request) {
		
		GenericResponse response = LoginHandler.loginHandler(user, request);
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signup(User user, @Context HttpServletRequest request) {
		
		GenericResponse response = SignupHandler.signupHanler(user, request);
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
		
	}
	
	@POST
	@Path("/book")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response book(String str) {
		GenericResponse response = null;
		try {			
			Gson gson = new Gson(); 
			BookingRequest BR = gson.fromJson(str, BookingRequest.class);
			
			response = BookticketHandler.bookTicket(BR);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return Response.ok(response, MediaType.APPLICATION_JSON).build();	
	}
	
	@POST
	@Path("/pnr")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pnr(String str) {
		GenericResponse response = null;
		String json = null;
		try {
			response  = PnrStatusRetrieverHandler.status(str);
			json = new Gson().toJson(response);
			System.out.println(json);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/canceltkt")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelTkt(String str) {
		GenericResponse response = null;
		String json = null;
		
		try {
			response  = TicketCancellationHandler.tktCancel(str);
			json = new Gson().toJson(response);
			System.out.println(json);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok(json).build();
	}
}
