<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Sign up</title>
		<script
  src="https://code.jquery.com/jquery-3.6.0.min.js"
  integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4="
  crossorigin="anonymous"></script>
	</head>
	<body>
		<form class="signup-form">
		  <label for="name">Name:</label>
		  <input type="text" id="name" name="name"><br><br>
		  <label for="password">Password:</label>
		  <input type="text" id="password" name="password"><br><br>
		  <input type="submit" value="Create Account">
		</form>
		
		<p>Already an existing user? Go to <span><a href="http://localhost/com.railwayreservation.jersey/login">Login Page</a></span> </p>
		
	</body>
	<script>
		  $(".signup-form").submit(
			 function(e){
			     e.preventDefault();
				 
				 const name = $("input[name=name]").val();
				 const password = $("input[name=password]").val();
				 
				 $("input[name=name]").val("");
		         $("input[name=password]").val("");
				 
				 const data = {
						 name,
						 password
				 }
				 
				 console.log(JSON.stringify(data));
				 
				 $.ajax( {
			            method: "POST",
			            url: "http://localhost/com.railwayreservation.jersey/api/v1/signup",
			            data: JSON.stringify(data),
			            contentType: "application/json"
			        } )
			        .done( function(response){
			           console.log(response);
			           
			           if(response.status == "200"){
			        	   alert("Account created successfully.\nLogin to continue.")
			        	   window.setTimeout(() => {
			        		   window.location.replace("http://localhost/com.railwayreservation.jersey/login");
			        	   }, 500)
			           }else {
			        	   alert(response.msg);
			           }
			        })
			        .fail( function(jqXHR, textStatus){
			            alert(textStatus);
			        });
			 }
		  )
	</script>	
</html>