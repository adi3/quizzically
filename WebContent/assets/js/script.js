/**
 * @author adi
 */

$(document).ready(function() {
	
	var request = null;
	var interval = 3000;
	
	$("#sign-in").submit(function(event){
	    // abort any pending request
	    if (request) request.abort();
	    
	    $(".navbar-form #form-loader").css("visibility", "visible");
	    
	    var $form = $(this);
	    var $inputs = $form.find("input");	//.find("input, select, button, textarea");
	    var serializedData = $form.serialize();
	    
	    // disable inputs for the duration of the ajax request TODO: maybe disable a processing circle
	    $inputs.prop("disabled", true);

	    request = $.ajax({
	        url: "Login",
	        type: "post",
	        data: serializedData
	    });

	    // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);
	        if (json["errors"] == null) {
	        	$(".msg-container .msg-img").css("background", "url(assets/img/success.png)");
	        	$(".msg-container .msg").text("You have been logged in as " + json["name"]);
	        	
	        	var html = '<a href="Inbox"><img src="assets/img/' + json["img"] + '" class="msg-icon" /></a>' +
	        				'<a href="Profile" class="btn btn-success">' + json["name"] + 
	        				'</a><div class="form-group line"></div><a class="btn btn-success' +
	        				' sign-out" href="Logout">Sign Out</a>';
	        	$("#sign-in").html(html);
	        } else {
	        	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	        	var errors = "";
	        	for (var i = 0; i < json["errors"].length; i++) {
	        		errors += json["errors"][i]["msg"];
	        		if (i != json["errors"].length - 1) errors += "<hr />";
	        	}
	        	$(".msg-container .msg").html(errors);
	        }
	        $(".msg-container").hide().slideToggle();
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
	    	$(".navbar-form input[name='password']").val("");
	        $inputs.prop("disabled", false);
	        $(".container .navbar-form #form-loader").css("visibility", "hidden");
	    });

	    event.preventDefault();
	});
	
	
	$(".msg-close").click(function() {
		$(".msg-container").slideUp();
	});
	
	
	$(document).on('click', ".mid-popup .close", function(e){
		$(".mid-popup").fadeOut();
	});
	
	
	$("#sign-up-lnk").click(function(event){		
	    $(".mid-popup").fadeIn();
	    event.preventDefault();
	});
	
	
	$("#sign-up-btn").click(function(event){
	    if (request) request.abort();

	    $(".mid-popup .close").hide();
	    $(".mid-popup #form-loader").show();
	    
	    var $form = $(this).parent().parent('form');
	    var $inputs = $form.find("input");
	    var serializedData = $form.serialize();

	    $inputs.prop("disabled", true);

	    request = $.ajax({
	        url: "Register",
	        type: "post",
	        data: serializedData
	    });
	    
	 // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);
	    	
	        if (json["errors"] == null) {
	        	$(".msg-container .msg-img").css("background", "url(assets/img/success.png)");
	        	$(".msg-container .msg").text("Your have been registered as " + json["name"]);
	        	
	        	var html = '<a href="Profile" class="btn btn-success">' + json["name"] + 
	        				'</a><div class="form-group line"></div><a class="btn btn-success' +
	        				' sign-out" href="Logout">Sign Out</a>';
	        	$("#sign-in").html(html);
	        	$inputs.val("");
		        $(".mid-popup").fadeOut();
	        } else {
	        	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	        	var errors = "";
	        	for (var i = 0; i < json["errors"].length; i++) {
	        		errors += json["errors"][i]["msg"];
	        		if (i != json["errors"].length - 1) errors += "<hr />";
	        	}
	        	$(".msg-container .msg").html(errors);
	        }
	        $(".msg-container").hide().slideToggle();
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
	    	$("#sign-up input[name='password']").val("");
	    	$("#sign-up input[name='pass-conf']").val("");
	        $inputs.prop("disabled", false);
	        $(".mid-popup #form-loader").hide();
	        $(".mid-popup .close").show();
	    });
	    
	    event.preventDefault();
	});
	
	
	$("#sign-up").keypress(function(e) {
		if (e.which == 13) $("#sign-up-btn").click();
	});
	
	
	$("#change-pass-lnk").click(function(event){		
	    $(".mid-popup").fadeIn();
	    event.preventDefault();
	});
	
	
	$("#change-pass-btn").click(function(event){		
		if (request) request.abort();

	    $(".mid-popup .close").hide();
	    $(".mid-popup #form-loader").show();
	    
	    var $form = $(this).parent().parent('form');
	    var $inputs = $form.find("input");
	    var serializedData = $form.serialize();
	    
	    $inputs.prop("disabled", true);
	    
	    request = $.ajax({
	        url: "ChangePassword",
	        type: "post",
	        data: serializedData
	    });
	    
	    // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);
	    	
	        if (json["errors"] == null) {
	        	$(".msg-container .msg-img").css("background", "url(assets/img/success.png)");
	        	$(".msg-container .msg").text("Your password has been updated!");
		        $(".mid-popup").fadeOut();
	        } else {
	        	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	        	var errors = "";
	        	for (var i = 0; i < json["errors"].length; i++) {
	        		errors += json["errors"][i]["msg"];
	        		if (i != json["errors"].length - 1) errors += "<hr />";
	        	}
	        	$(".msg-container .msg").html(errors);
	        }
	        $(".msg-container").hide().slideToggle();
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
	    	$inputs.val("");
	        $inputs.prop("disabled", false);
	        $(".mid-popup #form-loader").hide();
	        $(".mid-popup .close").show();
	    });
	    
	    event.preventDefault();
	});
	
	
	$("#change-pass").keypress(function(e) {
		if (e.which == 13) $("#change-pass-btn").click();
	});
	
	
	$("#searchbox").submit(function(e) {
		if ($("#searchbox input").val() == "") {
			$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
			$(".msg-container .msg").text("Search field cannot be empty!");
	    	$(".msg-container").hide().slideToggle();
			e.preventDefault();
		}
	});
	
	
	$("#update-profile-btn").click(function(e) {
		var boxes = $(".profile-info table").find("td:last-child");
		$(boxes).each(function() {
			var val = $(this).text();
			var name = $(this).attr("name");
			var str = '<input type="text" name="' + name + '" value="' + val + '" />';
			$(".profile-info table td").css("padding", "2px");
			$(this).html(str);
		});
		
		var title = $("#profile-form h1");
		var val = $(title).text();
		$(title).html('<input type="text" name="name" value="' + val + '" />');
		
		$("#update-profile-btn").hide();
		$("#save-profile-btn").show();
	});
	
	
	$("#save-profile-btn").click(function(e) {
		$("#profile-form").submit();
	});
	
	
	$(".inbox table td:nth-child(2) a").click(function(e) {
		e.preventDefault();
		var data = $(this).attr("href").split("?")[1];
		
		request = $.ajax({
	        url: "Messages",
	        type: "get",
	        data: data
	    });
	    
	    // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = null;
	    	
	    	try {
	    	    json = $.parseJSON(data);
	    	    $(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	        	var errors = "";
	        	for (var i = 0; i < json["errors"].length; i++) {
	        		errors += json["errors"][i]["msg"];
	        		if (i != json["errors"].length - 1) errors += "<hr />";
	        	}
	        	$(".msg-container .msg").html(errors);
		        $(".msg-container").hide().slideToggle();
	    	} catch (e) {
	    	    $(".mid-popup").html(response);
		        $(".mid-popup").fadeIn();	
	    	}
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	});
	
	
	$(document).on('click', "#msg-lnk", function(e){
		e.preventDefault();
		
		$(".mid-popup .close").hide();
	    $(".mid-popup #form-loader").show();
	    
		var data = $(this).attr("href").split("?")[1];
		
		request = $.ajax({
	        url: "Messages",
	        type: "get",
	        data: data
	    });
		
		// on success
	    request.done(function (response, textStatus, jqXHR){
    		var json = null;
	    	
	    	try {
	    	    json = $.parseJSON(data);
	    	    $(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	        	var errors = "";
	        	for (var i = 0; i < json["errors"].length; i++) {
	        		errors += json["errors"][i]["msg"];
	        		if (i != json["errors"].length - 1) errors += "<hr />";
	        	}
	        	$(".msg-container .msg").html(errors);
		        $(".msg-container").hide().slideToggle();
	    	} catch (e) {
		        $(".mid-popup").fadeOut('fast', function() {
		    	    $(".mid-popup").html(response).fadeIn();
		        });
	    	}
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
	        $(".mid-popup #form-loader").hide();
	        $(".mid-popup .close").show();
	    });
	});
	
	
	$(document).on('submit', "#create-msg", function(e){
		e.preventDefault();
		if (request) request.abort();

	    $(".mid-popup .close").hide();
	    $(".mid-popup #form-loader").show();
	    
	    var $form = $(this);
	    var $inputs = $form.find("input");
	    var serializedData = $form.serialize();
	    
	    $inputs.prop("disabled", true);
	    
	    request = $.ajax({
	        url: "Messages",
	        type: "post",
	        data: serializedData
	    });
	    
	    // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);
	    	
	        if (json["errors"] == null) {
		    	$inputs.val("");
	        	$(".msg-container .msg-img").css("background", "url(assets/img/success.png)");
	        	$(".msg-container .msg").text("Message(s) delivered!");
		        $(".mid-popup").fadeOut();
	        } else {
	        	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	        	var errors = "";
	        	for (var i = 0; i < json["errors"].length; i++) {
	        		errors += json["errors"][i]["msg"];
	        		if (i != json["errors"].length - 1) errors += "<hr />";
	        	}
	        	$(".msg-container .msg").html(errors);
	        }
	        $(".msg-container").hide().slideToggle();
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
	        $inputs.prop("disabled", false);
	        $(".mid-popup #form-loader").hide();
	        $(".mid-popup .close").show();
	    });
	});
	
	
	$(document).on('click', "#add-receiver-btn", function(e){
		var name = $("#create-msg select").val();
		console.log(name);
		var field = $("#create-msg input[name='to']");
		var list = field.val();
		if (list == "") list = name;
		else if (list.indexOf(name) == -1) {
			list = field.val() + ", " + name;
		}
		field.val(list);
		console.log(field.val());
	});
	
});