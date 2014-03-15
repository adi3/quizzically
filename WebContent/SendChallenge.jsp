<%@page import="java.util.ArrayList"%>
<%@page import="quizzically.models.*"%>

<h3>Send Challenge</h3>

<div class="loader" id="form-loader">
	<img src="assets/img/ajax-loader.gif" />
</div>
<div class="close">
	<img src="assets/img/close.gif" />
</div>

<div id="msg">
	<form action="Messages" method="post" id="create-msg">
		<input type="hidden" name="challenge" value="1" />
		<div class="row">
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
				
				<div class="col-md-3">
					<% User u = new User((String) request.getSession().getAttribute("user")); %>
					<% Quiz q = Quiz.retrieve(Integer.parseInt((String)request.getAttribute("quiz_id"))); %>
					<b>Quiz:</b> <%= q.name() %>
					
					<textarea name="msg" style="display:none">
						<a href="Profile?id="<%= u.getId() %> ><%= u.getName() %></a> has challenged you to take <%= q.name() %>.
						Click <a href=TakeQuiz?id=<%= q.id() %> >here</a> to take it now!
					</textarea>
				</div>
			
			<div class="col-md-12" style="margin-top: 2%">
				<hr />
				<input type="submit" class="btn btn-default" value="Send" style="float:left" />
			</div>	
		</div>
	</form>
</div>