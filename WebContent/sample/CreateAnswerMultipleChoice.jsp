<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sample Create Answer</title>
</head>
<body>
<form action="/Quizzically/api/Answer" method="POST">
	question_id: <input type="text" name="question_id" /><br />
	texts: 
	<input type="text" name="texts" /><br />
	correct: 
	<input type="checkbox" name="correct" value="1" /><br />
	<input type="submit" />
</form>
+ add button to show more forms..
<form action="/Quizzically/api/Answer" method="POST">
	question_id: <input type="text" name="question_id" /><br />
	texts: 
	<input type="text" name="texts" /><br />
	correct: 
	<input type="checkbox" name="correct" value="1" /><br />
	<input type="submit" />
</form>

<form action="/Quizzically/api/Answer" method="POST">
	question_id: <input type="text" name="question_id" /><br />
	texts: 
	<input type="text" name="texts" /><br />
	correct: 
	<input type="checkbox" name="correct" value="1" /><br />
	<input type="submit" />
</form>

</body>
</html>
