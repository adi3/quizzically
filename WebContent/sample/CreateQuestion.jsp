<%@page import="quizzically.models.Question"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sample Create Question</title>
</head>
<body>
<form action="/Quizzically/api/Question" method="POST">
<!--<input type="hidden" name="id" value="15" />-->
	quiz_id: <input type="text" name="quiz_id" /><br />
	text: <input type="text" name="text" /><br />
	type: 
	<select name="type">
	<%
	for (int type : Question.TYPES) {
		String typeStr = Question.typeString(type);
	%>
		<option value="<%= type %>"><%= typeStr %></option>
	<% } %>
	</select><br />
</form>

</body>
</html>
