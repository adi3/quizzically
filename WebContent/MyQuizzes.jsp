<%@page import="quizzically.models.Quiz,java.util.List"%>
<% Quiz[] qs = (Quiz[])request.getAttribute("quizzes"); %>

<h3>My Quizzes</h3>
<div class="close">
	<img src="assets/img/close.gif" />
</div>
<div class="row" id="search-results">
<% if (qs == null || qs.length == 0) { %>
	<div class="col-md-12" style="text-align:center">
		<i>You haven't created any quizzes yet. Make one now!</i>
	</div>
<% } else { %>
	<div class="col-md-1"></div>
	<div class="col-md-10">
	<table style="table-layout:fixed;">
		<% for (Quiz q : qs) { %>
			<tr>
				<td style="padding: 8px;text-align: center;"><a href="" ><%= q.name() %></a></td>
				<td style="min-width:300px;font-size: 12px;font-style: italic;text-align:left;"><%= q.description() %></td>
				<td style="text-align:right;padding-right:5px;padding-left:0">
					<a href="Quiz?id<%= q.id() %>" ><img style="width:16px" src="assets/img/edit.png" /></a>
				</td>
			</tr>	
		<% } %>
	</table>
	</div>
<% } %>
</div>