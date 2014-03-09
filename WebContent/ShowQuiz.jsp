<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="quizzically.models.Answer"%>
<%@page import="quizzically.models.Question"%>
<%@page import="quizzically.models.Quiz"%>
<%
Quiz quiz = (Quiz) request.getAttribute("quiz");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Take Quiz: <%= quiz.name() %></title>
</head>
<body>
	<h1><%= quiz.name() %></h1>
	<ol>
	<% for (Question q : quiz.questions()) {
	 String qs = "question_" + q.id();
	 %>
		<li>
			<p>
				<%= q.text() %>
			</p>
			<% for (Answer a : q.answers()) {
				switch (q.type()) { 
					case Question.TYPE_TEXT:
					case Question.TYPE_FILL_IN:
					case Question.TYPE_PICTURE:
			%>
				<input type="text" name="<%=qs%>" /><br />
			<%
						break;
					case Question.TYPE_MULTIPLE_CHOICE:
			%>
				<input type="radio" name="<%=qs%>" /><%= a.text() %><br />
			<%
						break;
				%>
				<% }
			} %>

		</li>
	<% } %>
	</ol>

</body>
</html>
