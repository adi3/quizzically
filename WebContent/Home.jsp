<%@page import="quizzically.models.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<link rel="shortcut icon" href="../../assets/ico/favicon.ico">
<link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="assets/css/style.css" />

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<title>Quizzically</title>
</head>
<body>

<div class="msg-container">
	<div class="msg-box">
		<div class="msg-img"></div>
		<div class="msg-close"></div>
		<p class="msg">Success Dialog which is sticky</p>
	</div>
</div>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
<div class="container">
  <div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
    <a class="navbar-brand" href="/Quizzically">Quizzically</a>
  </div>
  <div class="navbar-collapse collapse">
  	<% if (request.getAttribute("username") == null) { %>
	    <form class="navbar-form navbar-right" role="form" id="sign-in" method="post">
	    	<div class="form-group loader" id="form-loader">
	    		<img src="assets/img/ajax-loader.gif" />
	    	</div>
	      <div class="form-group">
	        <input type="text" placeholder="Username" name="username" class="form-control">
	      </div>
	      <div class="form-group">
	        <input type="password" placeholder="Password" name="password" class="form-control">
	      </div>
	      <button type="submit" class="btn btn-success" id="sign-in-btn">Sign in</button>
	      <div class="form-group line"></div>
	    	<button class="btn btn-success sign-up">Sign Up</button>
	    </form>
	 <% } else { %>
	 	<form class="navbar-form navbar-right">
	 		<a href="Profile" class="btn btn-success"><%= request.getAttribute("name") %></a>
	 		<div class="form-group line"></div>
	    	<a class="btn btn-success sign-out" href="Logout">Sign Out</a>
	 	</form>
	 <% } %>
  </div><!--/.navbar-collapse -->
  </div>
</div>

<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Hey, guys!</h1>
    <p>Put your dumb shit here.</p>
    <p><a class="btn btn-primary btn-lg" role="button">Learn more &raquo;</a></p>
  </div>
</div>

<div class="container">
  <!-- Example row of columns -->
  <div class="row">
    <div class="col-md-4">
      <h2>Herr Schmidt</h2>
      <p>More shit.</p>
      <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
    </div>
    <div class="col-md-4">
      <h2>Herr Becker</h2>
      <p>Still more shit.</p>
      <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
   </div>
    <div class="col-md-4">
      <h2>Czar Nicholas Platias III</h2>
      <p>Keep it coming.</p>
      <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
    </div>
  </div>

  <hr>

  <footer>
    <p>&copy; 2014 Adi | Dominic | Nicholas</p>
  </footer>
</div> <!-- /container -->

<script src="http://code.jquery.com/jquery-1.10.2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="assets/js/script.js"></script>
</body>
</html>