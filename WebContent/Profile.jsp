<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile: <%= request.getAttribute("name") %></title>
</head>
<body>
	<h1>Hello <%= request.getAttribute("name") %>!</h1>
	
	<p>Here's your info. Better pretty this up!</p>
	<ul>
		<li>Name: <%= request.getAttribute("name") %>
		<li>Username: <%= request.getAttribute("username") %>
		<li>Email: <%= request.getAttribute("email") %>
	</ul>
	
	<a href="UpdateProfile">Update Profile</a> | <a href="ChangePassword">Change Password</a>
	<br />
	<a href="Logout">Log Out</a>
</body>
</html>