package com.jersey.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.jersey.model.BookingRequest;
import com.jersey.model.BookingResponse;
import com.jersey.model.GenericResponse;
import com.jersey.model.Passenger;
import com.jersey.utils.getConnection;


public class BookticketHandler {	
	private static Connection conn = null;
	private static int cabin_size = 2;
    
    private static class CabinSeats {
    	String berth_type;
    	int available_seats;
    	String next_berth;
    }
    
    private static String pnrGenerator() {
    	Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    
    private static String passengerIdGenerator() {
    	Random rnd = new Random();
        int number = rnd.nextInt(9999);

        // this will convert any number sequence into 6 character.
        return "p"+String.format("%04d", number);
    }
    
    private static ResultSet queryExecutor (String sql) throws SQLException {
    	Statement stmt = null;
		ResultSet rs = null;
		
		System.out.println(sql);
		
        stmt = conn.createStatement();
	    rs = stmt.executeQuery(sql);
	    
	    return rs;
    }
    
    private static HashMap<String, CabinSeats> cabinSeatsCollector (int cabin_no) throws SQLException{
		
		HashMap<String, CabinSeats> seats = new HashMap<>();
		
		String sql = "SELECT * FROM berth where cabin_no = " + cabin_no; 
	    
		ResultSet rs = queryExecutor(sql);
		
	    // Preparing the available seats container
	    while(rs.next()) {
	    	CabinSeats seat = new CabinSeats();
	    	seat.berth_type = rs.getString("berth_type");
	    	seat.available_seats = rs.getInt("available_seats");
	    	seat.next_berth = rs.getString("next_berth");	
	    	seats.put(seat.berth_type , seat);
	    }
	    
	    return seats;
    }
    
    private static boolean checkAvailability(int cabin_no, HashMap<String, Integer> berth) {
    	try {
    		
    		HashMap<String, CabinSeats> seats = new HashMap<>();
            seats = cabinSeatsCollector(cabin_no);
            
		    // Login for checking availability
		    for(Map.Entry<String, Integer> entry : berth.entrySet()) {
		    	String chosenBerth = entry.getKey();
		    	Integer seatsRequired = entry.getValue();
		    	
		    	if(seats.get(chosenBerth).available_seats < seatsRequired) {
		    		return false;
		    	}
		    }
		    
		    return true;
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    private static int allotInACabin( HashMap<String, Integer> berth) {
    	for(int i=1; i<=cabin_size; i++) {
    		if(checkAvailability(i, berth)) {
    			return i;
    		}
    	}
    	return 0;
    }
    
    private static List<Passenger> bookSeatsInACabin(String pnr, int allotedCabin, List<Passenger> passengers) throws SQLException{
    	List<Passenger> bookedPersons = new LinkedList<>();
    	for(Passenger p: passengers) {
    		
    		boolean booked = bookForThisPerson(pnr, p.getPreferredBerth(), "AVL", "CNF", p, allotedCabin);
    		
    		if(booked) {
    			bookedPersons.add(p);
    		}
    		if(!booked) {
    			System.out.println("Error In Booking. Line no. 141");
    			break;
    		}
    	}
    	
    	if(!bookedPersons.isEmpty()) {    		
    		passengers.removeAll(bookedPersons);
    	}
    	
    	return passengers;
    }
    
    private static boolean bookForThisPerson(String pnr, String allotedBerth,String seatStatus, String bookingStatus, Passenger p, int cabin_no) throws SQLException {
    	Statement stmt = null;
    	
    	String name = p.getName();
		int age = p.getAge();
		String preferredBirth = allotedBerth;
		String status = bookingStatus;
		String pid = passengerIdGenerator();
		
		// writing queries
		String recordBooking = "insert into pnr_status (pnr, name, current_status, booking_status, passenger_id) values ("+ pnr + ",'" + name + "', '"+ status +"', '"+ status +"', '"+ pid +"')";
		String bookingQuery = "update booking_table set pnr = "+ pnr +", name = '"+ name + "', age ="+ age +", current_status = '"+ status +"', passenger_id = '"+ pid +"' FROM (SELECT * FROM booking_table WHERE berth_type = '" + preferredBirth + "' and cabin_no = "+ cabin_no +" and current_status = '"+ seatStatus +"'FETCH FIRST 1 ROW ONLY) AS subquery WHERE booking_table.seat_no = subquery.seat_no";
		String countUpdateQuery = "update berth set available_seats = available_seats-1 where berth_type = '" + preferredBirth + "' and cabin_no =" + cabin_no + "";
		
		// creating statements and executing the queries as transaction
		conn.setAutoCommit(false);
	    stmt = conn.createStatement();
	    
	    stmt.execute(recordBooking);
	    stmt.executeUpdate(bookingQuery);
	    stmt.executeUpdate(countUpdateQuery);
	    
	    conn.commit();
	    conn.setAutoCommit(true);
    	
    	return true;
    }
    
    private static List<Passenger> optimalSeatAllocationRACorWL(String allotedPnr, int cabin_no, List<Passenger> passengers, String preferredBerth, String bookingStatus){
    	try {
    		HashMap<String, CabinSeats> seatsInACabin = cabinSeatsCollector(cabin_no);
    		List<Passenger> bookedPersons = new LinkedList<>();
    		
    		for(Passenger p: passengers) {
    		    if(seatsInACabin.get(preferredBerth).available_seats >= 1) {
    				boolean booked = bookForThisPerson(allotedPnr, preferredBerth, bookingStatus, bookingStatus, p, cabin_no);
    				if(booked) {
    					seatsInACabin.get(preferredBerth).available_seats -= 1;
    					bookedPersons.add(p);
    				} else {
    					System.out.println("Error booking RAC. Line 188");
    				    break;
    				}
    			} else {
    				break;
    			}
    			
    		}
    		
    		if(!bookedPersons.isEmpty()) {    			
    			passengers.removeAll(bookedPersons);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return passengers;
    }
    
    private static List<Passenger> optimalSeatAllocation(String allotedPnr, int cabin_no, List<Passenger> passengers) {
    	try {
    		HashMap<String, CabinSeats> seatsInACabin = cabinSeatsCollector(cabin_no);
    		List<Passenger> bookedPersons = new LinkedList<>();
    		
    		for(Passenger p : passengers) {
    			
    			String preferredBerth = p.getPreferredBerth();
    			boolean found = false;
    			
    			// iterating and trying to allocate the group of passengers in near berths available
    			while(!found) {    				
    				if(seatsInACabin.get(preferredBerth).available_seats >= 1) {
    					boolean booked = bookForThisPerson(allotedPnr, preferredBerth, "AVL", "CNF", p, cabin_no);
    					
    					if(booked) {
    						seatsInACabin.get(preferredBerth).available_seats -= 1;
    						found = true;
    					} else {
    						System.out.println("Error in booking the optimal berth.");
    						break;
    					}
    					
    				} else {
    					// looking next preferred berth
    					preferredBerth = seatsInACabin.get(preferredBerth).next_berth;
    					
    					// Condition to check whether all the berths are looked or not
    					if(preferredBerth.equals("NA")) {
    						break;
    					}
    				}
    			}
    			if(!found) {
    				break;
    			} else {
    				bookedPersons.add(p);
    			}
    		}
    		
    		if(!bookedPersons.isEmpty()) {    			
    			passengers.removeAll(bookedPersons);
    		}
   
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return passengers;
    }
    
    private static List<Passenger> bookSeatsOptimally(String allotedPnr, int totalTickets, List<Passenger> passengers) throws SQLException {
    	
    	HashMap<Integer, Integer> isSeatsAvailable = new HashMap<>();
    	
    	String sql = "select cabin_no, sum(available_seats) from berth where berth_type != 'SL' group by cabin_no;";
    	ResultSet rs = queryExecutor(sql);
    	
    	while(rs.next()) {
    		Integer cabin_no = rs.getInt("cabin_no");
    		Integer available_seats = rs.getInt("sum");
    		
    		isSeatsAvailable.put(cabin_no, available_seats);
    	}
    	
    	//PASSENGER COLLECTION PIPELINE STARTS HERE
    	
    	// Try allocating normal berths
    	for(int i=1; i<=cabin_size; i++) {
    		if(isSeatsAvailable.get(i) > 0) {
    			passengers = optimalSeatAllocation(allotedPnr, i, passengers);
    		}
    	}
    	
    	if(passengers.isEmpty()) {
    		System.out.println("All passengers are successfully alloted a seat.");
    		return passengers;
    	}
    	
    	// Try allocating RAC
    	for(int i=1; i<=cabin_size; i++) {
    		passengers = optimalSeatAllocationRACorWL(allotedPnr, i, passengers, "SL", "RAC");
    	}
    	
    	if(passengers.isEmpty()) {
    		System.out.println("All passengers are successfully alloted a seat in normal berth or RAC.");
    		return passengers;
    	}
    	
    	// Try allocating WL
    	passengers = optimalSeatAllocationRACorWL(allotedPnr, 0, passengers, "WL", "WL");
    	
    	if(passengers.isEmpty()) {
    		System.out.println("Some passengers are in waiting list...");
    		return passengers;
    	}
    	
    	// Cancel ticket booking
    	System.out.println("Some tickets are cancelled");
    	return passengers;
    }
    
	public static GenericResponse bookTicket(BookingRequest request) throws SQLException {
		BookingResponse response = null;
		conn = getConnection.getConn();
		
		
        int totalTickets = request.getCount();
		
		List<Passenger> passengers = Arrays.asList(request.getPassengersList());
		passengers = new LinkedList<Passenger>(passengers);
		List<Passenger> unbookedPassengers = new LinkedList<>();
		
		HashMap<String, Integer> berth = new HashMap<>();
		
		for(Passenger p: passengers) {
			String preferred_berth = p.getPreferredBerth();
			if(berth.containsKey(preferred_berth)) {
				int current_value = berth.get(preferred_berth) + 1;
				berth.put(preferred_berth, current_value);
			} else {
				berth.put(preferred_berth, 1);
			}
		}
		
		System.out.println(berth.toString());
		
		String allotedPnr = pnrGenerator();
		
		// Checking the availability of berth in single cabin
		int allotedCabin = allotInACabin(berth);
		
		// Allocate all seats in one cabin
		// if allotedCabin = 0 no cabin is alloted
		if(allotedCabin > 0) {
			try {				
				unbookedPassengers = bookSeatsInACabin(allotedPnr, allotedCabin, passengers);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		// Use optimal Allocation
		else {
			try {
				unbookedPassengers = bookSeatsOptimally(allotedPnr, totalTickets, passengers);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		String msg = "";
		int success = totalTickets - unbookedPassengers.size();
		int failure = unbookedPassengers.size();
		
		if(unbookedPassengers.isEmpty()) {
			msg = "All passengers are allocated a seat.";
			System.out.println(msg);
		} else {
			msg = "Some passengers are not allocated a seat.";
			System.out.println(msg);
//			System.out.println(unbookedPassengers.toString());
		}
		
		int status = 200;
		if(failure > 0) {
			status = 400;
		}
		
		response = new BookingResponse(msg, status, success, failure, allotedPnr);
		return response;
		
	}
	
}
