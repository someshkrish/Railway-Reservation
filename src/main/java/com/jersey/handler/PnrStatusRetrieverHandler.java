package com.jersey.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.jersey.model.GenericResponse;
import com.jersey.model.PnrDetails;
import com.jersey.model.StatusResponse;
import com.jersey.utils.getConnection;

@SuppressWarnings("unused")
public class PnrStatusRetrieverHandler {
	private static Connection conn = null;
	
	private static class PNR {
    	private String pnr;
    	
		public void setPnr(String pnr) {
			this.pnr = pnr;
		}
    	
    	public String getPnr() {
    		return pnr;
    	}
    }
	
	private static ResultSet getPnrDetails(int pnr) {
    	ResultSet rs = null;
    	try {
    		Statement stmt = null;
    		
    		String sql = "select b.pnr, b.name, b.age, b.seat_no, b.cabin_no, b.berth_type, p.current_status, p.booking_status, p.passenger_id from booking_table b inner join pnr_status"+
    		             " p on p.pnr = b.pnr and p.name = b.name where b.pnr = " + pnr;
    		stmt = conn.createStatement();
    		
    		rs = stmt.executeQuery(sql);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return rs;
    }
	
	public static GenericResponse status (String str) throws SQLException {
		StatusResponse response = null;
		conn = getConnection.getConn();
		
		Gson g = new Gson();
		PNR pnr = g.fromJson(str, PNR.class);
		int pnrNo = Integer.parseInt(pnr.getPnr());
		
		ResultSet rs = getPnrDetails(pnrNo);
		List<PnrDetails> pnrDetails = new LinkedList<>();
		
		try {
			while(rs.next()) {
				PnrDetails tkt = new PnrDetails();
				tkt.name = rs.getString("name");
				tkt.age = rs.getInt("age");
				tkt.booking_status = rs.getString("booking_status");
				tkt.current_status = rs.getString("current_status");
				tkt.seat_no = rs.getString("seat_no");
				tkt.pnr = rs.getInt("pnr");
				tkt.pid = rs.getString("passenger_id");
				tkt.berth_type = rs.getString("berth_type");
				tkt.cabin_no = rs.getString("cabin_no");
				
				pnrDetails.add(tkt);
			}
			
			response = new StatusResponse("Retrieved Successfully.", 200, pnrDetails);
		} catch (Exception e) {
			e.printStackTrace();
			response = new StatusResponse("Not Retrieved Successfully. " + e.getMessage(), 400, null);
		}
		
		return response;
	}
}