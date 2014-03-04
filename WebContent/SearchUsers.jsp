<%@page import="java.util.ArrayList"%>
<%@page import="quizzically.models.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Users</title>
</head>
<body>
	<h1>Search Users</h1>
	<% ArrayList<String> errors = (ArrayList<String>)request.getAttribute("errors"); %>
	<% if (errors != null && !errors.isEmpty()) { %>
		<ul>
		<% for (String error : errors) { %>
			<li><%= error %></li>	
		<% } %>
		</ul>
	<% } %>
	
	<form action="SearchUsers" method="post">
		<div>
			<input type="text" name="param" />
			<input type="submit" value="Search" />
		</div>
	</form>
	
	<br />
	
	<% String param = (String) request.getAttribute("param"); %>
	<% ArrayList<User> users = (ArrayList<User>)request.getAttribute("users"); %>
	<% if (users == null || users.isEmpty()) { %>
		<% if (!param.isEmpty()) { %>
			<p>Unable to find users beginning with <b><%= param %></b>.</p>
		<% } %>
	<% } else { %>
		<p>Found the following users beginning with <b><%= param %></b>:</p>
		<ul>
			<% for (User user : users) { %>
				<li>
					<a href="Profile?user=<%= user.getUsername() %>" ><%= user.getName() %></a>
				</li>	
			<% } %>
		</ul>
	<% } %>
	
</body>
</html>