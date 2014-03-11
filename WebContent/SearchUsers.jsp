<%@page import="quizzically.models.*"%>
<%@page import="java.util.ArrayList"%>

<h3>Search Users</h3>
<div class="loader" id="form-loader">
	<img src="assets/img/ajax-loader.gif" />
</div>
<div class="close">
	<img src="assets/img/close.gif" />
</div>

<div class="row" id="search-results">
<% ArrayList<User> users = (ArrayList<User>)request.getAttribute("users"); %>

<% if (users == null || users.isEmpty()) { %>
	<div class="col-md-12" style="text-align:center">
		<i>Unable to find any users for this query. Please try again!</i>
	</div>
<% } else { %>
	<div class="col-md-2"></div>
	<div class="col-md-8">
	<table>
		<% for (User user : users) { %>
			<tr>
				<td><img src="assets/img/<%= user.getImg() %>" /></td>
				<td><a href="Profile?id=<%= user.getId() %>" ><%= user.getName() %></a></td>
			</tr>	
		<% } %>
	</table>
	</div>
<% } %>
</div>