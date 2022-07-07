package com.jersey.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class getConnection {
	public static Connection getConn() throws SQLException {
		String jdbcURL = "jdbc:postgresql://localhost:5432/zyklerrailways";
		Connection connection = DriverManager.getConnection(jdbcURL, "postgres", "postgres");
		System.out.println("database connection successfull. getConnectionUtil. Line 11.");
		return connection;
	}
}
