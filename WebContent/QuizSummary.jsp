<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="quizzically.models.*" %> 



<%
Quiz quiz = (Quiz) request.getAttribute("quiz");
User user = (User) request.getAttribute("user");
QuizAttempt[] userAttempts = (QuizAttempt[]) request.getAttribute("userAttempts"),
		highestAllTimeAttempts = (QuizAttempt[]) request.getAttribute("highestAllTimeAttempts"),
		highestTodayAttempts = (QuizAttempt[]) request.getAttribute("highestTodayAttempts"),
		recentAttempts = (QuizAttempt[]) request.getAttribute("recentAttempts");
String averageScore = (String) request.getAttribute("averageScore");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Summary</title>
</head>
<body>
	<div>
	<h1> <%=quiz.name()%> Summary</h1>
	<p>Description: <%=quiz.description()%></p>
	</div>
	<div>
		<h2>Created By</h2>
		<a href="<%= quiz.owner().profileLink() %>"> <%=quiz.owner().getName()%> </a>
	</div>
	<div>
		<h2>My History</h2>
		<%= QuizAttempt.renderTable(userAttempts, false) %>
	</div>
	<div>
		<h2>Top Scores (All Time)</h2>
		<%= QuizAttempt.renderTable(highestAllTimeAttempts) %>
	</div>
	<div>
		<h2>Top Scores (Today)</h2>
		<%= QuizAttempt.renderTable(highestTodayAttempts) %>
	</div>
	<div>
		<h2>Recent Scores</h2>
		<%= QuizAttempt.renderTable(recentAttempts) %>
	</div>
	<div>
		<h2>Average Score</h2>
		<%= averageScore %>
	</div>
	<div>
		<a href="<%= quiz.takeLink() %>"> Take Quiz </a>
		<%
		if(user.equals(quiz.owner())){
		%>
		<a href="<%= quiz.editLink() %>"> Edit Quiz </a>
		<%
		}
		%>
	</div>
</body>
</html>
