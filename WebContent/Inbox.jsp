<%@include file="frags/Header.jsp" %>

<% ArrayList<Message> msgs = (ArrayList<Message>)request.getAttribute("msgs"); %>
<div class="container inbox">
	<div class="row">
	<br />
		<div class="col-md-2"></div>
		<div class="col-md-3">	
			<h1>Inbox</h1>
		</div>
		
		<div class="col-md-2"></div>
		<div class="col-md-2" id="new-msg-lnk">
			<a id="msg-lnk" href="Messages?mode=new" class="btn btn-default">New Message</a>
		</div>
	</div>
 	<div class="row">	
 		<br />	
		<div class="col-md-2"></div>
		<div class="col-md-7">
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
								<a href="Profile?id=<%= msg.getFromUser().getId() %>">
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
		</div>
	</div>
</div>

<div class="mid-popup" style="height:auto;margin:-185px auto auto -298px">
</div>

<%@include file="frags/Footer.jsp" %>