<%@include file="frags/Header.jsp" %>

<div class="container quiz">
	
	<!--<input type="hidden" name="id" value="1" />-->

	<div class="row caption">
		<div class="col-md-1"></div>
		<div class="col-md-10">
			<h1>New Quiz</h1>
			<p>(Psst...click on ze attributes to edit them)</p>
			<hr />
		</div>
		<div class="col-md-1"></div>
	</div>
	
 	<div class="row meta">
		<div class="col-md-1"></div>
		<div class="col-md-10">
		<form action="Quiz" method="post" id="quiz-form">
			<div class="col-md-1">
				<input type="hidden" name="id" value="" id="id" />
			</div>
			<div class="col-md-7">	
				<h3 id="name">Title</h3>
				<h4 id="description">Description</h4>
			</div>
			<div class="col-md-3" style="margin-top:20px">
				<h5>Format</h5>
				<select name="page_format" id="page_format">
					<% for (int val : Quiz.PAGE_FORMATS) { %>
						<% String str = Quiz.PAGE_FORMAT_STRINGS[val]; %>
						<option value="<%= val %>"><%= str %></option>
					<% } %>
				</select>
				<h5 style="margin-top: 10px">Order</h5>
				<select name="order" id="order">
					<% for (int val : Quiz.ORDERS) { %>
						<% String str = Quiz.ORDER_STRINGS[val]; %>
						<option value="<%= val %>"><%= str %></option>
					<% } %>
				</select>
			</div>
		</form>
		<div class="col-md-1"></div>
	</div>
</div>



<div class="mid-popup">
</div>

<%@include file="frags/Footer.jsp" %>