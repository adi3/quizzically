<%@page import="java.util.ArrayList"%>
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

<% if (request.getSession().getAttribute("name") == null) { %>
	<title>Quizzically</title>
<% } else { %>
	<title>Quizzically: <%= request.getSession().getAttribute("name") %></title>
<% } %>
</head>
<body>

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
  
  <div class="navbar-header">
	<form action="SearchUsers" method="post" class="navbar-form navbar-left" id="searchbox">
		<div class="form-group">
			<input type="text" name="param" placeholder="Search" class="form-control" />
		</div>
	</form>
  </div>
  
  <div class="navbar-collapse collapse">
  	<% if (request.getSession().getAttribute("user") == null) { %>
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
	    <a class="btn btn-success sign-up" href="Register" id="sign-up-lnk">Sign Up</a>
	    </form>
	 <% } else { %>
	 	<form class="navbar-form navbar-right">
	 		<a href="Inbox"><img src="assets/img/<%= request.getAttribute("msgIcon") %>" class="msg-icon" /></a>
	 		<a href="Profile" class="btn btn-success"><%= request.getSession().getAttribute("name") %></a>
	 		<div class="form-group line"></div>
	    	<a class="btn btn-success sign-out" href="Logout">Sign Out</a>
	 	</form>
	 <% } %>
  </div><!--/.navbar-collapse -->
  </div>
</div>