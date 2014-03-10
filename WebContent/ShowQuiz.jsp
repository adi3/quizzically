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
	<h1>Take Quiz: <%= quiz.name() %></h1>
	<form action="ShowQuiz" method="POST">
		<input type="hidden" name="id" value="<%=quiz.id()%>" />

		<ol>
		<% for (int pos = 0; pos < quiz.questions().size(); pos++ ) {
			Question q = quiz.questions().get(pos);
		 %>
			<li>
				<p>
					<%= q.text() %>
				</p>
				<%
				for (Answer a : q.answers()) {
					 String qs = "question-" + q.id() + "-answer-" + a.id()
						 + "-pos-" + pos;
					switch (q.type()) { 
						case Question.TYPE_TEXT:
						case Question.TYPE_FILL_IN:
						case Question.TYPE_PICTURE:
							out.println("<input type=\"text\" name=\"" + qs + "\" /><br />");
							break;
						case Question.TYPE_MULTIPLE_CHOICE:
							out.println("<input type=\"radio\" name=\"" + qs + "\" />" + a.text() + "<br />");
							break;
					}
				} %>
			</li>
		<% } %>
		</ol>

		<input type="submit" value="Grade" />
	</form>

</body>
</html>
