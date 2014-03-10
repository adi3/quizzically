<jsp:include page="header.jsp">
	<jsp:param name="pageTitle" value="Log in"></jsp:param>
</jsp:include>
	<form action="Login" method="post">
		<div>
			<p>Username: <input type="text" name="username" value="<%= request.getAttribute("username") %>" /></p>
			<p>Password: <input type="password" name="password" /></p>
			<input type="submit" value="Log in" />
		</div>
	</form>
	<p>
		<a href="Register">Create New Account</a>
	</p>
<%@include file="footer.jsp" %>