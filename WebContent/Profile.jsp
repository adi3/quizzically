<%@include file="frags/Header.jsp" %>
<%@ page import="quizzically.models.*" %> 
<%@ page import="java.util.*" %> 


<% ArrayList<User> friends = (ArrayList<User>)request.getAttribute("friends"); %>
<% String sessionUser = (String)request.getSession().getAttribute("user"); %>
<% List<Achievement> achievements = Achievement.earnedAchievements((new User((String) request.getSession().getAttribute("user"))).getId()); %>
<div class="container profile">
 	<div class="row">
		<br />
		
		<form method="post" action="UpdateProfile" id="profile-form">
			<div class="col-md-1"></div>
			<div class="col-md-3">	
				<h1><%= request.getAttribute("name") %></h1>
				<br />
				<img src="assets/img/<%= request.getAttribute("img") %>" />
			</div>
			
			<% if (sessionUser != null) { %>
				<% User self = new User(sessionUser); %>
			<% } %>
			<div class="col-md-4">
				<% if ((sessionUser == null || !friends.contains(new User(sessionUser))) && !request.getAttribute("username").equals(sessionUser)) { %>					
					<div class="profile-info">
						<table>
							<tr>
								<td>Username</td>
								<td class="blur">Hidden</td>
							</tr>
							<tr>
								<td>Email</td>
								<td class="blur">Hidden</td>
							</tr>
							<tr>
								<td>Location</td>
								<td class="blur">Hidden</td>
							</tr>
						</table>
					</div>
				<% } else { %>
					<div class="profile-info">
						<table>
							<tr>
								<td>Username</td>
								<td name="username"><%= request.getAttribute("username") %></td>
							</tr>
							<tr>
								<td>Email</td>
								<td name="email"><%= request.getAttribute("email") %></td>
							</tr>
							<tr>
								<td>Location</td>
								<td name="loc"><%= request.getAttribute("loc") %></td>
							</tr>
							<tr>
								<td>Achievements</td>
								<td name="achievements"><%
								for (Achievement a : achievements) {
									out.println(a + "<br />");
								}
								%>
						</table>
					</div>
				<% } %>
			</div>
		</form>
		
		<div class="col-md-3">
			<div class="friends-info">
				<div class="sidebar-title"><h3>Friends</h3></div>
			<% if (friends == null || friends.isEmpty()) { %>
				<p><i>Nothing to see here. Move along.</i></p>
			<% } else { %>
				<table>
					<% for (User friend : friends) { %>
						<tr>
							<td><img src="assets/img/<%= friend.getImg() %>" /></td>
							<td><a href="Profile?id=<%= friend.getId() %>" ><%= friend.getName() %></a></td>
							<td><img src="assets/img/close.gif" id="del-<%= friend.getId() %>" /></td>
						</tr>
					<% } %>
				</table>
			<% } %>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-1"></div>
		<div class="col-md-3">
		<% if (sessionUser != null && request.getAttribute("username").equals(sessionUser)) { %>
				<button class="btn btn-edit" id="update-profile-btn">Update Profile</button>
				<button class="btn btn-edit" id="save-profile-btn">Save Profile</button>
				<button class="btn btn-edit" id="change-pass-lnk">Change Password</button>
		<% } else if (sessionUser != null) { %>
			<% User self = new User(sessionUser); %>
			<% if (!friends.contains(self) && !request.getAttribute("username").equals(sessionUser)) { %>
				<% if (self.isPendingFriend((String)request.getAttribute("username"))) { %>
					<div class="frnd-req" style="margin-left:6%;cursor: not-allowed;font-style:italic;">
						<button class="btn btn-default" disabled="disabled">Request Pending</button>
					</div>
				<% } else { %>
					<form method="post" action="Friends" id="add-frnd">
						<div class="frnd-req">
							<input type="hidden" name="mode" value="add" />
							<input type="hidden" name="user" value="<%= request.getAttribute("username") %>" />
							<input type="submit" class="btn btn-default" value="Send Friend Request" />
						</div>
					</form>
				<% } %>
			<% } %>
		<% } %>
		</div>
		<div class="col-md-4"></div>
		<% if (sessionUser != null && request.getAttribute("username").equals(sessionUser)) { %>
			<div class="col-md-3">
				<div class="quiz-info">
					<div class="sidebar-title"><h3>Quiz</h3></div>
					<table>
						<tr>
							<td><a href="Quiz" ><img src="assets/img/add.png" /></a></td>
							<td><a href="Quiz" >Create A Quiz!</a></td>
						</tr>
						<tr>
							<td><a href="Quiz" ><img src="assets/img/edit.png" /></a></td>
							<td><a href="#" id="my_quizzes_lnk">My Quizzes</a></td>
						</tr>
					</table>
				</div>
			</div>
		<% } %>
	</div>
</div>

<div class="mid-popup">
</div>

<%@include file="frags/Footer.jsp" %>