<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= request.getParameter("pageTitle") %></title>
</head>
<body>
<h1><%= request.getParameter("pageTitle") %></h1>
<% ArrayList<String> errors = (ArrayList<String>)request.getAttribute("errors"); %>
<% if (errors != null && !errors.isEmpty()) { %>
	<ul>
	<% for (String error : errors) { %>
		<li><%= error %></li>	
	<% } %>
	</ul>
<% } %>