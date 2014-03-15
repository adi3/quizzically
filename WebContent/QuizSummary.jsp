<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="quizzically.models.*" %> 


<%!
String renderTable(QuizAttempt[] attempts) {
	String output = "";
	if (attempts.length == 0) {
		return "Nothing to see here...";
	}

	output += "<table>";
	output += "<th>Who</th>";
	output += "<th>Percent Correct</th>";
	output += "<th>Time Taken</th>";
	output += "<th>When</th>";
	for (QuizAttempt qa : attempts) {
		output += "<tr>";
		output += "<td>" +
			"<a href=\"" + qa.user().profileLink() + "\">" + 
			qa.user().getName() + "</a>" +
			"</td>";
		output += "<td>" +
			qa.percentCorrect() +
			"</td>";
		output += "<td>" +
			qa.timeTaken() +
			"</td>";
		output += "<td>" +
			qa.completedAt() +
			"</td>";
		output += "</tr>";
	}
	output += "</table>";
	return output;
}
%>

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
		<%= renderTable(userAttempts) %>
	</div>
	<div>
		<h2>Top Scores (All Time)</h2>
		<%= renderTable(highestAllTimeAttempts) %>
	</div>
	<div>
		<h2>Top Scores (Today)</h2>
		<%= renderTable(highestTodayAttempts) %>
	</div>
	<div>
		<h2>Recent Scores</h2>
		<%= renderTable(recentAttempts) %>
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
