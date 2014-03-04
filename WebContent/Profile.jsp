<%@page import="java.util.ArrayList"%>
<%@page import="quizzically.models.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile: <%= request.getAttribute("name") %></title>
</head>
<body>
	<h1>Hello <%= request.getAttribute("name") %>!</h1>
	
	<form action="SearchUsers" method="post">
		<div>
			<input type="text" name="param" />
			<input type="submit" value="Search" />
		</div>
	</form>
	
	<p>Here's your info. Better pretty this up!</p>
	<ul>
		<li>Name: <%= request.getAttribute("name") %>
		<li>Username: <%= request.getAttribute("username") %>
		<li>Email: <%= request.getAttribute("email") %>
	</ul>
	
	<% ArrayList<User> friends = (ArrayList<User>)request.getAttribute("friends"); %>
	<% if (request.getAttribute("username").equals(request.getSession().getAttribute("user"))) { %>
		<% if (friends == null || friends.isEmpty()) { %>
			<p>You don't have any friends yet :(</p>
		<% } else { %>
			<p>Your Friends:</p>
			<ul>
				<% for (User friend : friends) { %>
					<li>
						<a href="Profile?user=<%= friend.getUsername() %>" ><%= friend.getName() %></a>
					</li>	
				<% } %>
			</ul>
		<% } %>
		<br />
		<a href="UpdateProfile">Update Profile</a> | <a href="ChangePassword">Change Password</a>
		<br />
		<a href="Logout">Log Out</a>
		
	<% } else { %>
		<% if (friends == null || friends.isEmpty()) { %>
			<p><%= request.getAttribute("name") %> doesn't have any friends yet :(</p>
		<% } else { %>
			<p>Friends of <%= request.getAttribute("name") %>:</p>
			<ul>
				<% for (User friend : friends) { %>
					<li>
						<a href="Profile?user=<%= friend.getUsername() %>" ><%= friend.getName() %></a>
					</li>	
				<% } %>
			</ul>
		<% } %>
		
		<% User self = new User((String)request.getSession().getAttribute("user")); %>
		<% if (friends.contains(self)) { %>
			<p>You and <%= request.getAttribute("name") %> are friends!</p>
		<% } else { %>
			<form action="Friends" method="post">
				<div>
					<input type="hidden" name="mode" value="add" />
					<input type="hidden" name="user" value="<%= request.getAttribute("username") %>" />
					<input type="submit" value="Send Friend Request" />
				</div>
			</form>
		<% } %>
		<br />
		<a href="Profile">My Profile</a>
	<% } %>
</body>
</html>