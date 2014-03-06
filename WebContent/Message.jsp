<%@page import="quizzically.models.*"%>
<%@page import="quizzically.config.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% Message msg = (Message)request.getAttribute("msg"); %>
<title>Message by <%= msg.getFromUser().getName() %></title>
</head>
<body>
	<div><b>From: </b>
		<a href="Profile?user=<%= msg.getFromUser().getUsername() %>">
			<%= msg.getFromUser().getName() %>
		</a>
	</div>
	<div><b>On: </b><%= msg.getDate() %></div>
	<div><b>Type: </b><%= msg.getType() %></div>
	<br />
	<div><%= msg.getMsg() %></div>
	<br />
	<% if(msg.getType().equals("Request") && msg.getMsg().equals(MyConfigVars.REQUEST_MSG)) { %>
		<form action="Friends" method="post">
			<div>
				<input type="hidden" name="mode" value="accept" />
				<input type="hidden" name="user" value="<%= msg.getFromUser().getUsername() %>" />
				<input type="submit" value="Accept" />
			</div>
		</form>
	<% } else { %>
		<div><a href="Messages?mode=reply&id=<%= msg.getId() %>">Reply</a></div>
	<% } %>
	<div><a href="Inbox">Back</a></div>
</body>
</html>