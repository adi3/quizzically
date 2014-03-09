<%@page import="java.util.ArrayList"%>
<%@page import="quizzically.models.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Inbox: <%= request.getAttribute("name") %></title>
</head>
<body>
	<h1><%= request.getAttribute("name") %>'s Inbox</h1>
	<% ArrayList<String> errors = (ArrayList<String>)request.getAttribute("errors"); %>
	<% if (errors != null && !errors.isEmpty()) { %>
		<ul>
		<% for (String error : errors) { %>
			<li><%= error %></li>	
		<% } %>
		</ul>
	<% } %>
	
	<p><a href="Messages?mode=new">New Message</a></p>
	
	<% ArrayList<Message> msgs = (ArrayList<Message>)request.getAttribute("msgs"); %>
	<% if (msgs == null || msgs.isEmpty()) { %>
		<p>Your inbox is empty :(</p>
	<% } else { %>
		<table>
			<tr><th>Type</th><th>Message</th><th>From</th><th>Date</th></tr>
			<% for (Message msg : msgs) { %>
				<tr>
					<td>
						<% if (!msg.isRead()) %><b>
						<%= msg.getType() %>
						<% if (!msg.isRead()) %></b>
					</td>
					<td>
						<% if (!msg.isRead()) %><b>
						<a href="Messages?id=<%= msg.getId() %>">
							<%= msg.getPreviewMsg() %>
						</a>
						<% if (!msg.isRead()) %></b>
					</td>
					<td>
						<% if (!msg.isRead()) %><b>
						<a href="Profile?user=<%= msg.getFromUser().getUsername() %>">
							<%= msg.getFromUser().getName() %>
						</a>
						<% if (!msg.isRead()) %></b>
					</td>
					<td>
						<% if (!msg.isRead()) %><b>
						<%= msg.getDate() %>
						<% if (!msg.isRead()) %></b>
					</td>
				</tr>
			<% } %>
		</table>
	<% } %>

</body>
</html>