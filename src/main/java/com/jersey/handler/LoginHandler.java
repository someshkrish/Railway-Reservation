package com.jersey.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.jersey.model.User;
import com.jersey.utils.getConnection;
import com.jersey.model.GenericResponse;

public class LoginHandler{
	
	public static GenericResponse loginHandler(User user, HttpServletRequest request) {
		GenericResponse response = new GenericResponse();
		
		try {			
			Connection conn = getConnection.getConn();
			
			String name = user.getName();
			String password = user.getPassword();
			
			System.out.println(name +" "+password + "\n");
			
			HttpSession session = request.getSession();
			
			String sql = "SELECT * FROM user_record WHERE name = '" + name + "'"; 
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			conn.close();
			
		    boolean userexist = false;
		    
		    
		    if(rs.next()) {
		    	userexist = true;
		    	String obtainedPassword = rs.getString("password");
		    	if(obtainedPassword.equals(password)) {
		    		System.out.println("Authentication successful. LoginHandler. Line 44");
		    		session.setAttribute("LoggedIn", "true");
		    		session.setAttribute("uname", name);
		    		session.setMaxInactiveInterval(60);
		    		
		    		response.setMsg("Authentication successful.");
		    		response.setStatus(200);
		    		
		    	} else {
		    		System.out.println("Authentication unsuccessful. LoginHandler. Line 51");
                    response.setMsg("Authentication unsuccessful.");
		    		response.setStatus(400);
		    	}
		    }
		    
		    if(!userexist) {
		    	System.out.println("Invalid user name.");
		    	response.setMsg("Authentication unsuccessful.");
	    		response.setStatus(400);
		    }
			
		} catch (Exception e) {
            e.printStackTrace();
            response.setMsg("Internal Error.");
    		response.setStatus(400);
		}
		
		return response;
	  
  }

}
