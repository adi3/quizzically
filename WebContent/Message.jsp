<%@page import="quizzically.models.*"%>
<%@page import="quizzically.config.*"%>
<% Message msg = (Message)request.getAttribute("msg"); %>

<h3>Message For You</h3>
<div class="loader" id="form-loader">
	<img src="assets/img/ajax-loader.gif" />
</div>
<div class="close">
	<img src="assets/img/close.gif" />
</div>
<div id="msg">
	<div class="row">
		<div class="col-md-7">
			<div class="msg-info">
				<b>From: </b>
				<a href="Profile?id=<%= msg.getFromUser().getId() %>">
					<%= msg.getFromUser().getName() %>
				</a>
			</div>
			<div class="msg-info"><b>On: </b><%= msg.getDate() %></div>
			<div class="msg-info"><b>Type: </b><%= msg.getType() %></div>
		</div>
		
		<div class="col-md-5">
		<% if(msg.getType().equals("Request") && msg.getMsg().equals(MyConfigVars.REQUEST_MSG)) { %>
			<form action="Friends" method="post" id="accept-frnd">
				<div>
					<input type="hidden" name="mode" value="accept" />
					<input type="hidden" name="user" value="<%= msg.getFromUser().getUsername() %>" />
					<input type="submit" class="btn btn-default" value="Accept" />
				</div>
			</form>
			<br /><br />
			<button class="btn btn-default" style="width:67px" id="del-<%= msg.getFromUser().getId() %>">Reject</button>
		<% } else { %>
			<a id="msg-lnk" class="btn btn-default" href="Messages?mode=reply&id=<%= msg.getId() %>">Reply</a>
		<% } %>
		</div>
	</div>
	
	<hr />
	<div class="row" id="show-msg">	
		<%= msg.getMsg() %>
	</div>
</div>
