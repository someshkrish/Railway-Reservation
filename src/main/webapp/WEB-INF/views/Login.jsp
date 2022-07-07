<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Login Page</title>
		<script
  src="https://code.jquery.com/jquery-3.6.0.min.js"
  integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4="
  crossorigin="anonymous"></script>
	</head>
	<body>
		<form class="login-form">
		  <label for="name">Name:</label>
		  <input type="text" id="name" name="name"><br><br>
		  <label for="password">Password:</label>
		  <input type="text" id="password" name="password"><br><br>
		  <input type="submit" value="Log in">
		</form>
		<br/>
		<p>Don't Have an account? Signup <a href="http://localhost/com.railwayreservation.jersey/signup">here</a></p>
	</body>
	<script>
		  $(".login-form").submit(
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
			            url: "http://localhost/com.railwayreservation.jersey/api/v1/login",
			            data: JSON.stringify(data),
			            contentType: "application/json"
			        } )
			        .done( function(response){
			           console.log(response);
			           
			           if(response.status == "200"){
			        	   alert("Loggedin Successfully.");
			        	   window.setTimeout(() => {
			        		   window.location.replace("http://localhost/com.railwayreservation.jersey/home");
			        	   }, 500)
			           } else {
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

