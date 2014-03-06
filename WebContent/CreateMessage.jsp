<%@page import="java.util.ArrayList"%>
<%@page import="quizzically.models.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% String mode = (String) request.getParameter("mode");  %>
<% Message msg = (Message) request.getAttribute("msg");  %>
<title>
<% if (mode.equals("new")) { %>
	New Message
<% } else if (mode.equals("reply")) { %>
	Reply to <%= msg.getFromUser().getName() %>
<% } %>
</title>
</head>
<body>
	<h1><% if (mode.equals("new")) { %>New Message<% } %></h1>
	
	<form action="Messages" method="post">
		<div>
			<% if (mode.equals("new")) { %>
				<p><b>To: </b><input type="text" name="to" placeholder="Enter comma-separated usernames..." /></p>
				
				<% ArrayList<User> friends = (ArrayList<User>)request.getAttribute("friends"); %>
				<% if (friends != null && !friends.isEmpty()) { %>
					<p><select>
					<% for (User friend : friends) { %>
					 	<option value="<%= friend.getUsername() %>">
					 		<%= friend.getName() + " [" + friend.getUsername() + "]" %>
					 	</option>
					<% } %>
					</select></p>
				<% } %>
				<p>... add select button working with jquery...</p>
					
				<p><b>Message:</b>
			<% } else if (mode.equals("reply")) {%>
				<p><b>To: </b><%= msg.getFromUser().getName() %></p>
				<input type="hidden" name="to" value="<%= msg.getFromUser().getUsername() %>" />
				<p><b>Message: </b><%= msg.getMsg() %></b></p>
				<p><b>Your Reply:</b>
			<% } %>
				<br />
				<textarea name="msg" placeholder="Enter message here..."></textarea>
			</p>
			<p><input type="submit" value="Send" /></p>	
		</div>
	</form>
	
</body>
</html>