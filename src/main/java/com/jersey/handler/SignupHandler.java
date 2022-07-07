package com.jersey.handler;


import jakarta.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.Statement;

import com.jersey.model.GenericResponse;
import com.jersey.model.User;
import com.jersey.utils.getConnection;

public class SignupHandler{
	public static GenericResponse signupHanler(User user, HttpServletRequest request) {
		GenericResponse response = new GenericResponse();
		
		try {
			Connection conn = getConnection.getConn();
			
			String name = user.getName();
			String password = user.getPassword();
			
			System.out.println(name +" "+password + "\n");
			
			
			String sql = "INSERT INTO user_record( name, password) VALUES( '" + name + "','"+ password +"')"; 
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			
			conn.close();
			
			System.out.println("executed successfully.");
			
			response.setMsg("Account Created Successfully.");
			response.setStatus(200);
			
		} catch(Exception e) {
			e.printStackTrace();
			response.setMsg("Internal Error.");
    		response.setStatus(400);
		}
		
		return response;
	}
}
