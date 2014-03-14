

<form action="/Quizzically/api/Answer" method="POST" id="form">
	question_id: <input type="text" name="question_id" /><br />
	texts: 
	<input type="text" name="texts" /><br />
	<input type="text" name="texts" /><br />
	<input type="text" name="texts" /><br />
	(arbitrary number of texts allowed)<br />
	<input type="hidden" name="correct" value="1" /><br />
	<input type="submit" />
</form>

<script src="http://code.jquery.com/jquery-1.10.2.min.js" type="text/javascript"></script>
<script>
	$("#form").submit(function(e) {
		e.preventDefault();
		console.log($(this).serialize());
	});
</script>

</body>
</html>
