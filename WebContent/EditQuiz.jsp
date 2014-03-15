<%@include file="frags/Header.jsp" %>
<%@page import="java.util.List"%>
<% Quiz quiz = (Quiz) request.getAttribute("quiz"); %>

<div class="container quiz">
	<div class="row caption">
		<div class="col-md-1"></div>
		<div class="col-md-10">
			<h1>Edit Quiz</h1>
			<p>(Psst...click on ze attributes to edit them)</p>
			<hr />
		</div>
		<div class="col-md-1"></div>
	</div>
	
 	<div class="row meta">
		<div class="col-md-1"></div>
		<div class="col-md-10" style="height: 120px;">
			<form action="Quiz" method="post" id="quiz-form">
				<div class="col-md-1">
					<input type="hidden" name="id" value="<%= quiz.id() %>" id="quiz_id" />
				</div>
				<div class="col-md-7">	
					<h3 id="name"><%= quiz.name() %></h3>
					<h4 id="description"><%= quiz.description() %></h4>
				</div>
				<div class="col-md-3" style="margin-top:20px">
					<h5 style="width:25%">Format</h5>
					<select name="page_format" id="page_format">
						<% for (int val : Quiz.PAGE_FORMATS) { %>
							<% String str = Quiz.PAGE_FORMAT_STRINGS[val]; %>
							<option value="<%= val %>"
								<% if (quiz.pageFormat() == val) { %>
									selected="selected"
								<% } %>
							><%= str %></option>
						<% } %>
					</select>
					<h5 style="margin-top: 5px;width:25%">Order</h5>
					<select name="order" id="order">
						<% for (int val : Quiz.ORDERS) { %>
							<% String str = Quiz.ORDER_STRINGS[val]; %>
							<option value="<%= val %>" 
								<% if (quiz.order() == val) { %>
									selected="selected"
								<% } %>
							><%= str %></option>
						<% } %>
					</select>
					<h5 style="margin-top: 5px;width:25%">Grading</h5>
					<select name="immediate_correction" id="immediate_correction">
						<option value="0"
							<% if (!quiz.immediateCorrection()) { %>
								selected="selected"
							<% } %>
						>At the End</option>
						<option value="1"
							<% if (quiz.immediateCorrection()) { %>
								selected="selected"
							<% } %>
						>Immediate</option>
					</select>
				</div>
			</form>
		</div>
		<div class="col-md-1"></div>
	</div>
	
	<% List<Question> ques =  quiz.questions(); %>
	<% for (Question q : ques) { %>
		<div class="row question" >
			<div class="col-md-1"></div>
			<div class="col-md-10">
				<div class="col-md-1"></div>
				<div class="col-md-10">
					<form action="Question" method="post" id="ques">
						<div class="col-md-1">
							<input type="hidden" name="ques_id" value="<%= q.id() %>" />
							<img src="assets/img/close.gif" class="ques-del">
							<h5 class="index">Q</h5>
						</div>
						<div class="col-md-4">
							<h5>Type</h5>
							<select name="ques_type">
								<% for (int type : Question.TYPES) { %>
									<% String typeStr = Question.typeString(type); %>
									<option value="<%= type %>"
										<% if (q.type() == type) { %>
											selected="selected"
										<% } %>
									><%= typeStr %></option>
								<% } %>
							</select>
						</div>
						<div class="col-md-5" style="padding:0;">	
							<p name="ques_text"><%= q.text() %></p>
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
					<% double group = Math.random(); %>
					<% List<Answer> ans =  q.answers(); %>
					<input type="hidden" name="ans_id" value="<%= ans.get(0).id() %>" />
					
					<table class="answers">
						<% if (q.type() == 2) { %>
							<% for (Answer a : ans) { %>
								<tr>
									<td><img src="assets/img/close.gif" class="ans-del"></td>
									<td><p><%= a.text() %></p></td>
									<td><input type="radio" name="<%= group %>" style="visibility:visible" 
										<% if (a.correct())  {%>
											checked="checked"
										<% } %>
									/></td>
								</tr>
							<% } %>
						<% } else { %>
							<% for (AnswerText text : ans.get(0).answerTexts()) { %>
								<tr>
									<td><img src="assets/img/close.gif" class="ans-del"></td>
									<td><p><%= text.text() %></p></td>
									<td><input type="radio" name="<%= group %>" style="visibility:hidden" 
										<% if (ans.get(0).correct())  {%>
											checked="checked"
										<% } %>
									/></td>
								</tr>
							<% } %>
						<% } %>
					</table>
				</form>
			</div>
			<div class="col-md-4"></div>
		</div>
	<% } %>
	
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
	
	<!-- Worse hack than the Texas Chainsaw Massacre -->
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
	
</div>


<div class="mid-popup">
</div>

<%@include file="frags/Footer.jsp" %>
