<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Log In</title>
</head>
<body>
	<h1>Log In</h1>
	<% ArrayList<String> errors = (ArrayList<String>)request.getAttribute("errors"); %>
	<% if (errors != null && !errors.isEmpty()) { %>
		<ul>
		<% for (String error : errors) { %>
			<li><%= error %></li>	
		<% } %>
		</ul>
	<% } %>
	
	<% String user = (String) request.getAttribute("user"); %>
	<% user = user == null ? "" : user; %>
	
	<form action="Login" method="post">
		<div>
			<p>Username: <input type="text" name="user" value="<%= user %>" /></p>
			<p>Password: <input type="password" name="pass" /></p>
			<input type="submit" value="Log in" />
		</div>
	</form>
	<p>
		<a href="Register">Create New Account</a>
	</p>
</body>
</html>