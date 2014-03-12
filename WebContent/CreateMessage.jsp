<%@page import="java.util.ArrayList"%>
<%@page import="quizzically.models.*"%>

<% String mode = (String) request.getParameter("mode");  %>
<% Message msg = (Message) request.getAttribute("msg");  %>

<h3>
<% if (mode.equals("new")) { %>
	New Message
<% } else if (mode.equals("reply")) { %>
	Reply to <%= msg.getFromUser().getName() %>
<% } %>
</h3>

<div class="loader" id="form-loader">
	<img src="assets/img/ajax-loader.gif" />
</div>
<div class="close">
	<img src="assets/img/close.gif" />
</div>

<div id="msg">
	<form action="Messages" method="post" id="create-msg">
		<div class="row">
			<% if (mode.equals("new")) { %>
				<div class="col-md-12 msg-info">
					<b>To: </b><input type="text" name="to" placeholder="Enter comma-separated usernames..." style="width: 90%;padding: 3px" /></p>
				</div>
				
				<div class="col-md-5"></div>
				<div class="col-md-5">
				<% ArrayList<User> friends = (ArrayList<User>)request.getAttribute("friends"); %>
				<% if (friends != null && !friends.isEmpty()) { %>
					<select>
					<% for (User friend : friends) { %>
					 	<option value="<%= friend.getUsername() %>"><%= friend.getName() %></option>
					<% } %>
					</select>
				</div>
				<div class="col-md-2">
					<a class="btn btn-default" href="#" id="add-receiver-btn">Add</a>
				</div>
				<% } %>
					
				<div><b>Message:</b>
			<% } else if (mode.equals("reply")) {%>
				<div class="col-md-12 msg-info">
					<b>To: </b><a href="Profile?id=<%= msg.getFromUser().getId() %>">
						<%= msg.getFromUser().getName() %>
					</a>
				</div>
				<input type="hidden" name="to" value="<%= msg.getFromUser().getUsername() %>" />
				<div class="col-md-12 msg-info">
					<b>Original Message: </b><%= msg.getMsg() %></b>
				</div>
				<div class="col-md-12" style="margin-top: 10px"><b>Your Reply:</b>
			<% } %>
				<br />
				<textarea name="msg" placeholder="Enter message here..."></textarea>
			</div>
			
			<div class="col-md-12" style="margin-top: 2%">
				<input type="submit" class="btn btn-default" value="Send" />
			</div>	
		</div>
	</form>
</div>