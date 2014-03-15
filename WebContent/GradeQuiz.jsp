<%@page import="quizzically.models.Answer"%>
<%@page import="quizzically.models.AnswerText"%>
<%@page import="quizzically.models.Question"%>
<%@page import="quizzically.models.QuestionResponse"%>
<%@page import="quizzically.models.Quiz"%>
<%@page import="quizzically.models.QuizAttempt"%>
<%
Quiz quiz = (Quiz) request.getAttribute("quiz");
QuestionResponse[] qrs = (QuestionResponse[]) request.getAttribute("gradedResponses");
QuizAttempt qA = (QuizAttempt) request.getAttribute("attempt");
%>
<h3>Grade Report: <%= quiz.name() %></h3>
<div class="close">
	<img src="assets/img/close.gif" />
</div>

<div class="row" id="search-results">

	<div class="col-md-1"></div>
	<div class="col-md-10" style="padding:0">
		<table class="grade_report">
			<tr>
				<th style="text-align:left !important;"><b>Question</b></th>
				<th><b>Correct Answer(s)</b></th>
				<th><b>Your Answer</b></th>
				<th><b>Result</b></th>
			</tr>
			<% for (int pos = 0; pos < qrs.length; pos++ ) {
				Question q = qrs[pos].question();
				Question.Grade g = qrs[pos].grade();
			 %>
				<tr>
					<td style="padding:0;padding-left: 10px;"><%= q.text() %></td>
					<td><% switch (q.type()) { 
							case Question.TYPE_TEXT:
							case Question.TYPE_FILL_IN:
							case Question.TYPE_PICTURE:
								for (Answer a : q.answers()) {
									String str = "";
									for (AnswerText at : a.answerTexts()) {
										str += at.text() + ", ";
									}
									out.println(str.substring(0, str.length() - 2));
								}
								break;
							case Question.TYPE_MULTIPLE_CHOICE:
								for (Answer a : q.answers()) {
									if (a.correct()) out.println(a.text());									
								}
								break;
							}
						%></td>
						<td style="text-align:center"><%= qrs[pos].responseString() %></td>
						<td style="text-align:center">
							<% if (g.points() == 1) { %>
								<img style="width:20px;" src="assets/img/yes.gif" />
							<% } else { %>
								<img style="width:20px;" src="assets/img/no.png" />
							<% } %>
						</td>
				</tr>
			<% } %>
		</table>
	</div>
</div>


<div class="row">
	<% if (!qA.completed()) { %>
		<div class="col-md-1"></div>
		<div class="col-md-4" style="margin-top:15px;">
			<a href="#" class="btn btn-default" id="send_challenge">Challenge A Friend!</a>
		</div>
		<div class="col-md-6 score-msg">Total Score: <%= qA.score() %></div>
	<% } else { %>
		<div class="col-md-11 tiny-msg">Press space bar to continue...</div>
	<% } %>
</div>
