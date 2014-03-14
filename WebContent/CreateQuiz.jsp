<%@include file="frags/Header.jsp" %>

<div class="container quiz">
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
		<div class="col-md-10" style="margin-bottom:10px">
			<form action="Quiz" method="post" id="quiz-form">
				<div class="col-md-1">
					<input type="hidden" name="id" value="" id="quiz_id" />
				</div>
				<div class="col-md-7">	
					<h3 id="name">Title</h3>
					<h4 id="description">Description</h4>
				</div>
				<div class="col-md-3" style="margin-top:20px">
					<h5 style="width:25%">Format</h5>
					<select name="page_format" id="page_format">
						<% for (int val : Quiz.PAGE_FORMATS) { %>
							<% String str = Quiz.PAGE_FORMAT_STRINGS[val]; %>
							<option value="<%= val %>"><%= str %></option>
						<% } %>
					</select>
					<h5 style="margin-top: 10px;width:25%">Order</h5>
					<select name="order" id="order">
						<% for (int val : Quiz.ORDERS) { %>
							<% String str = Quiz.ORDER_STRINGS[val]; %>
							<option value="<%= val %>"><%= str %></option>
						<% } %>
					</select>
				</div>
			</form>
		</div>
		<div class="col-md-1"></div>
	</div>
	
	
	<div class="row question" >
		<div class="col-md-1"></div>
		<div class="col-md-10">
			<div class="col-md-1"></div>
			<div class="col-md-10" style="display:none">
				<form action="Question" method="post" id="ques" style="display:none">
					<div class="col-md-1">
						<input type="hidden" name="ques_id" value="" />
						<img src="assets/img/close.gif" class="ques-del">
						<h5 class="index">Q</h5>
					</div>
					<div class="col-md-4">
						<h5>Type</h5>
						<select name="ques_type">
							<% for (int type : Question.TYPES) { %>
								<% String typeStr = Question.typeString(type); %>
								<option value="<%= type %>"><%= typeStr %></option>
							<% } %>
						</select>
					</div>
					<div class="col-md-5" style="padding:0;">	
						<p name="ques_text">Enter question here...</p>
					</div>
					<div class="col-md-2 add_ans">
						<img src="assets/img/add.png" />
						<b>Add Answer</b>
					</div>
				</form>
				<div class="col-md-1"></div>
			</div>
			<div class="col-md-1"></div>
		</div>
		
		<div class="col-md-1"></div>
		
		<div class="col-md-5"></div>
		<div class="col-md-3">
			<form action="Answer" method="post" id="ans">
				<input type="hidden" name="correct" value="1" />
				<input type="hidden" name="ans_id" value="" />
				<table class="answers">
				</table>
			</form>
		</div>
		<div class="col-md-4"></div>
	</div>
	
	<div class="row add">
		<div class="col-md-1"></div>
		<div class="col-md-10">
			<div class="col-md-1"></div>
			<div class="col-md-10" style="padding:0">
				<hr />
				<div id="add_btn">
					<img src="assets/img/add.png" />
					<h5>Add Question</h5>
				</div>
			</div>
			<div class="col-md-1"></div>
		</div>
		<div class="col-md-1"></div>
	</div>
	
</div>


<div class="mid-popup">
</div>

<%@include file="frags/Footer.jsp" %>