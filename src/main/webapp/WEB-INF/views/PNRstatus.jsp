<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="ISO-8859-1">
<title>PNR Status</title>
<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}
</style>
</head>

<body>
<label for="pnr">Enter PNR:</label>
<form class="pnrform" name="pnrForm">
<input type="text" id="pnr" name="pnr" minlength="1" maxlength="6" required><br><br>
<input type="submit" value="Get Status">
</form>
<br> <br>
<div class="PnrStatus">

<table class="pnrTable">
  
</table>
<br/>
<a href="http://localhost/com.railwayreservation.jersey/home"><button type="button">Home Page</button></a>
</div>
</body>
<script>

  var table = document.querySelector(".pnrTable");
  document.querySelector(".pnrform").addEventListener("submit", (e) => {
	  e.preventDefault();
	  table.innerHTML="<tr>"+
		    "<th>S No.</th>"+
		    "<th>Passenger Id</th>"+
		    "<th>Name</th>"+
		    "<th>Age</th>"+
		    "<th>Booking Status</th>"+
		    "<th>Current Status</th>"+
		    "<th>Seat No</th>"+
		    "<th>Berth Type</th>"+
		    "<th>Cabin No</th>"
		  "</tr>";
	  const pnr = document.forms["pnrForm"].elements['pnr'].value;
      
	  const params = {
    		  pnr
      }
	  
	  let responseData;
      
	  fetch("http://localhost/com.railwayreservation.jersey/api/v1/pnr", {
		    method: "POST",
		    body: JSON.stringify(params),
		    headers: {
		      "Content-Type": "application/json"
		    }
	  })
	  .then(function(response) {
	    return response.json();
	  }).then(function(data) {
		  console.log(data);
		  console.log(data.status);
		  console.log(data.msg);
		  console.log(data.pnrs);
		 responseData = data.pnrs;
		 
		 responseData.forEach((el, index) => {
			 
			 let name = el.name;
			 let age = el.age;
			 let booking_status = el.booking_status;
			 let current_status = el.current_status;
			 let seat_no = el.seat_no;
			 let slno = index+1;
			 let pid = el.pid;
			 let berth_type = el.berth_type;
			 let cabin_no = el.cabin_no;
			 
             // ---------------------------------
			 
			 let format_seat_no = seat_no;
			 let format_berth_type = berth_type;
			 let format_cabin_no = cabin_no;
			 
			 if(seat_no.startsWith("NA")){
				 format_seat_no = "-";
			 }
			 
			 if(berth_type.startsWith("WL")){
				 format_berth_type = "-";
			 }
			 
			 if(cabin_no === "0"){
				 format_cabin_no = "NA";
			 }
			 
			 //----------------------------------
			 
			 
			 
			 let record = "<tr><td>" + slno + "</td><td>" + pid +"</td><td>"+ name +"</td><td>"+ age +"</td><td>"+ booking_status +"</td><td>"+
			              current_status +"</td><td>"+ format_seat_no +"</td><td>"+format_berth_type+"</td><td>"+format_cabin_no+"</td></tr>";
             table.insertAdjacentHTML("beforeend", record);
		 })
		 
	  });
  })
</script>

</html>