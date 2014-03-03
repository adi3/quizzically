<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change Password:  <%= request.getAttribute("name") %></title>
</head>
<body>
	<h1>Change Password</h1>
	<% ArrayList<String> errors = (ArrayList<String>)request.getAttribute("errors"); %>
	<% if (errors != null && !errors.isEmpty()) { %>
		<ul>
		<% for (String error : errors) { %>
			<li><%= error %></li>	
		<% } %>
		</ul>
	<% } %>
	
	<form action="ChangePassword" method="post">
		<div>
			<p>Password: <input type="password" name="pass" /></p>
			<p>Confirm Password: <input type="password" name="passConf" /></p>
			<input type="submit" value="Change" />
		</div>
	</form>
</body>
</html>