<%@include file="frags/Header.jsp" %>
<%@page import="quizzically.models.Quiz,quizzically.models.QuizAttempt,java.util.List"%>
<% 
QuizAttempt attempt = (QuizAttempt) request.getAttribute("attempt");
Quiz quiz = attempt.quiz();
%>

<div class="container quiz">
	
	<div class="row caption">
		<div class="col-md-1"></div>
		<div class="col-md-10">
			<h1>Quiz: <%= quiz.name() %></h1>
			<p><%= quiz.description() %></p>
			<p><a href="Profile?id=<%= quiz.ownerId() %>"><%= quiz.owner().getName() %></a></p>
			<hr />
		</div>
		<div class="col-md-1"></div>
	</div>	
	
	
	<form action="TakeQuiz" method="post" id="show-quiz">
		<input type="hidden" name="attempt_id" value="<%=attempt.id()%>" />
		<input type="hidden" name="quiz_mode" value="<%= quiz.immediateCorrection() %>" />
		<% List<Question> questions = quiz.questions(); %>
		<% for (int pos = 0; pos < questions.size(); pos++ ) { %>
			<% Question q = questions.get(pos); %>
	 
			<div class="row question" style="display:none">
				<div class="col-md-1"></div>
				<div class="col-md-10">
					<div class="col-md-12">
						<div class="col-md-1">
							<h5 class="index">Q</h5>
						</div>
						<div class="col-md-10" style="height: inherit;">
							<h5><%= q.text() %></h5>
						</div>
						<div class="col-md-1"></div>
					</div>
				</div>
				
				<div class="col-md-1"></div>
				
				<div class="col-md-2"></div>
				<div class="col-md-8 answer">
					<% for (Answer a : q.answers()) {
						String qs = "question-" + q.id() + "-pos-" + pos;
						switch (q.type()) { 
							case Question.TYPE_TEXT:
							case Question.TYPE_FILL_IN:
							case Question.TYPE_PICTURE:
								out.println("<input placeholder=\"Your Answer\" type=\"text\" name=\"" + qs + "\" /><br />");
								break;
							case Question.TYPE_MULTIPLE_CHOICE:
								out.println("<div><input type=\"radio\" name=\"" + qs + "\" value=\"" + a.id() + "\"/>" +
											"<p class=\"opts\">" + a.text() + "</p></div>");
								break;
						}
					} %>
				</div>
				<div class="col-md-2"></div>
			</div>
		<% } %>
		
		<div class="row">
			<div class="col-md-1"></div>
			<div class="col-md-10">
				<hr style="margin:4% 2% 1%;"/>
				<input type="submit" value="Submit!" class="btn btn-default" id="quiz_submit"/>
			</div>
			<div class="col-md-1"></div>
		</div>
		
	</form>
	
</div>

<div class="mid-popup">
</div>

<%@include file="frags/Footer.jsp" %>
<script type="text/javascript">
	var format = <%= quiz.pageFormat() %>;
	var index = 0;
	var quiz_id = <%= quiz.id() %>;
	
	if (format == 0) {
		$.each($("#show-quiz .question"), function(i, val) {
			$(val).show();
		});
	} else $("#show-quiz .question").first().show();
</script>
