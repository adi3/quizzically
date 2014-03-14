<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="quizzically.models.Answer"%>
<%@page import="quizzically.models.AnswerText"%>
<%@page import="quizzically.models.Question"%>
<%@page import="quizzically.models.QuestionResponse"%>
<%@page import="quizzically.models.Quiz"%>
<%
Quiz quiz = (Quiz) request.getAttribute("quiz");
QuestionResponse[] qrs = (QuestionResponse[]) request.getAttribute("gradedResponses");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Results: <%= quiz.name() %></title>
</head>
<body>
	<h1>Quiz Results: <%= quiz.name() %></h1>

	<ol>
	<% for (int pos = 0; pos < qrs.length; pos++ ) {
		Question q = qrs[pos].question();
		Question.Grade g = qrs[pos].grade();
	 %>
		<li>
			<p>
				<%= q.text() %>
			</p>
			<p>
				<%
				switch (q.type()) { 
					case Question.TYPE_TEXT:
					case Question.TYPE_FILL_IN:
					case Question.TYPE_PICTURE:
						out.println("Correct Answer(s):<br />");
						for (Answer a : q.answers()) {
							out.println("<ul>");
							for (AnswerText at : a.answerTexts()) {
								out.println("<li>" + at.text() + "</li>");
							}
							out.println("</ul>");
						}
						break;
					case Question.TYPE_MULTIPLE_CHOICE:
						out.println("Options (<span style=\"color:green;\">correct</span>):<br />");
						out.println("<ul>");
						for (Answer a : q.answers()) {
							String output = "";
							output += "<li>";
							if (a.correct()) {
								output += "<span style=\"color:green;\">";
							}
							output += a.text();
							if (a.correct()) {
								output += "</span>";
							}
							output += "</li>";
							out.println(output);
						}
						out.println("</ul>");
						break;
				}
				%>
		</p>
		<p>
	 		Your answer: <%= qrs[pos].responseString() %>
		</p>
		<p>
			Your score: <%= g.points() %> / <%= g.possible() %>
		</p>
		</li>
	<% } %>
	</ol>


</body>
</html>
