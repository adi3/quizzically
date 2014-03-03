<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Update Profile: <%= request.getAttribute("name") %></title>
</head>
<body>
	<h1>Update Info</h1>
	<% ArrayList<String> errors = (ArrayList<String>)request.getAttribute("errors"); %>
	<% if (errors != null && !errors.isEmpty()) { %>
		<ul>
		<% for (String error : errors) { %>
			<li><%= error %></li>	
		<% } %>
		</ul>
	<% } %>
	
	<form action="UpdateProfile" method="post">
		<div>
			<p>Name: <input type="text" name="name" value="<%= request.getAttribute("name") %>" /></p>
			<p>Username: <input type="text" name="username" value="<%= request.getAttribute("username") %>" /></p>			
			<p>Email: <input type="text" name="email" value="<%= request.getAttribute("email") %>" /></p>
			<input type="submit" value="Update" />
		</div>
	</form>
</body>
</html>