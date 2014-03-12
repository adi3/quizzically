<%@page import="quizzically.models.Quiz"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sample Create Quiz</title>
</head>
<body>
<form action="/Quizzically/api/Quiz" method="POST">
<!--<input type="hidden" name="id" value="1" />-->
	name: <input type="text" name="name" /><br />
	description: <input type="text" name="description" /><br />
	page_format:
	<select name="page_format">
	<%
	for (int val : Quiz.PAGE_FORMATS) {
		String str = Quiz.PAGE_FORMAT_STRINGS[val];
	%>
		<option value="<%= val %>"><%= str %></option>
	<% } %>
	</select><br />
	order:
	<select name="order">
	<%
	for (int val : Quiz.ORDERS) {
		String str = Quiz.ORDER_STRINGS[val];
	%>
		<option value="<%= val %>"><%= str %></option>
	<% } %>
	</select><br />
	<input type="submit" />
</form>

</body>
</html>
