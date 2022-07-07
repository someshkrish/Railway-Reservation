package com.jersey.controller;

import org.glassfish.jersey.server.mvc.Viewable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

@Path("/")
public class ViewsController {
	@GET
	@Path("/login")
    public Viewable login() {
		System.out.println("inside login");
        return new Viewable("/Login");
    }
	
	@GET
	@Path("/signup")
    public Viewable signup() {
		System.out.println("inside signup");
        return new Viewable("/Signup");
    }
	
	@GET
	@Path("/home")
    public Viewable home(@Context HttpServletRequest request) {
		System.out.println("inside home");
		
		HttpSession session = request.getSession();
		
		if(session == null || session.getAttribute("LoggedIn") == null) {
			return new Viewable("/Error");
		}
		
        return new Viewable("/Welcome");
    }
	
	@GET
	@Path("/error")
    public Viewable error() {
		System.out.println("inside error");
        return new Viewable("/Error");
    }
	
	@GET
	@Path("/pnrstatus")
    public Viewable pnrstatus(@Context HttpServletRequest request) {
		System.out.println("inside pnrstatus");
		
        HttpSession session = request.getSession();
		
		if(session == null || session.getAttribute("LoggedIn") == null) {
			return new Viewable("/Error");
		}
		
        return new Viewable("/PNRstatus");
    }
	
	@GET
	@Path("/cancelticket")
    public Viewable cancelTicket(@Context HttpServletRequest request) {
		System.out.println("inside ticket cancellation");
		
        HttpSession session = request.getSession();
		
		if(session == null || session.getAttribute("LoggedIn") == null) {
			return new Viewable("/Error");
		}
		
        return new Viewable("/CancelTicket");
    }
	
	@GET
	@Path("/bookticket")
    public Viewable bookTicket(@Context HttpServletRequest request) {
		System.out.println("inside ticket booking");
		
        HttpSession session = request.getSession();
		
		if(session == null || session.getAttribute("LoggedIn") == null) {
			return new Viewable("/Error");
		}
		
        return new Viewable("/Bookticket");
    }
}