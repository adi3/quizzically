<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ page import="quizzically.models.*" %> 
	
	<%
	Quiz quiz = (Quiz) request.getAttribute("quiz");
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Summary</title>
</head>
<body>
	<h1> <%=quiz.name()%> Summary</h1>
	<p>Description: <%=quiz.description()%></p>
	<a href="<%= quiz.owner().profileLink() %>"> <%=quiz.owner().getName()%> </a>
	<br />
	<a href="<%= quiz.takeLink() %>"> Take Quiz </a>
	<%
	if(((User)request.getAttribute("user")).equals(quiz.owner())){
	%>
	<a href="<%= quiz.editLink() %>"> Edit Quiz </a>
	<%
	}
	%>
</body>
</html>