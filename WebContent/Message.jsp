<%@page import="quizzically.models.*"%>
<%@page import="quizzically.config.*"%>

<% Message msg = (Message)request.getAttribute("msg"); %>
<h3>Message by <%= msg.getFromUser().getName() %></h3>
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