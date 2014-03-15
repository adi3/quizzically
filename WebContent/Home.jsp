<%@include file="frags/Header.jsp" %>
<%
QuizAttempt[] userAttempts = (QuizAttempt[]) request.getAttribute("userAttempts");
//QuizAttempt[] recentQuizzes = Quiz.retrieveOrderByCreated();
%>

<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Hey, guys!</h1>
    <p><a class="btn btn-primary btn-lg" role="button">Learn more &raquo;</a></p>
  </div>
</div>

<div class="container">
  <!-- Example row of columns -->
  <div class="row">
    <div class="col-md-4">
			<h2>My Recently Taken Quizzes</h2>
			<%= QuizAttempt.renderTable(userAttempts, false) %>
    </div>
    <div class="col-md-4">
      <h2>Herr </h2>
      <p>Still more stuff.</p>
      <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
   </div>
    <div class="col-md-4">
      <h2>Czar</h2>
      <p>Keep it coming.</p>
      <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
    </div>
		<div>
		</div>
	</div>
		</div>
  </div>

  <hr>
</div> <!-- /container -->

<div class="mid-popup">
</div>

<%@include file="frags/Footer.jsp" %>
