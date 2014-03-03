<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Register</title>
</head>
<body>
	<h1>Register</h1>
	<% ArrayList<String> errors = (ArrayList<String>)request.getAttribute("errors"); %>
	<% if (errors != null && !errors.isEmpty()) { %>
		<ul>
		<% for (String error : errors) { %>
			<li><%= error %></li>	
		<% } %>
		</ul>
	<% } %>
	
	<form action="Register" method="post">
		<div>
			<p>Name: <input type="text" name="name" value="<%= request.getAttribute("name") %>" /></p>
			<p>Email: <input type="text" name="email" value="<%= request.getAttribute("email") %>" /></p>
			<p>Username: <input type="text" name="username" value="<%= request.getAttribute("username") %>" /></p>
			<p>Password: <input type="password" name="password" /></p>
			<input type="submit" value="Register" />
		</div>
	</form>
</body>
</html>