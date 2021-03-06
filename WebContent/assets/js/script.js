/**
 * @author adi
 */

$(document).ready(function() {
	
	var request = null;
	var interval = 3000;
	var $question = "";
	var quiz_mode = false;
	
	$("#sign-in").submit(function(event){
	    // abort any pending request
	    if (request) request.abort();
	    
	    $("#navbar-form-loader").css("visibility", "visible");
	    
	    var $form = $(this);
	    var $inputs = $form.find("input");	//.find("input, select, button, textarea");
	    var serializedData = $form.serialize();
	    
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
	        	
	        	var html =  '<div class="form-group loader" id="navbar-form-loader">' +
		    				'<img src="assets/img/ajax-loader.gif"></div>' +
		    				'<a href="Inbox"><img src="assets/img/' + json["img"] + '" class="msg-icon" /></a>' +
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
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });

	    event.preventDefault();
	});
	
	
	$(".msg-close").click(function() {
		$(".msg-container").slideUp();
	});
	
	
	$(document).on('click', ".mid-popup .close", function(e){
		$(".mid-popup").fadeOut();
	});
	
	$(document).on('keypress', document, function(e){
		if (e.which == 32 && quiz_mode) {
			quiz_mode = false;
			$(".mid-popup .close").click();
			$($("#show-quiz .question").get(index)).fadeOut('fast', function(e) {
				index += 1;
				$($("#show-quiz .question").get(index)).fadeIn('fast');
			});
			return false;
		}
	});
	
	
	$("#sign-up-lnk").click(function(event){
	    request = $.ajax({
	        url: "Register",
	        type: "get"
	    });
	    
	    request.done(function (response, textStatus, jqXHR){
        	$(".mid-popup").html(response).fadeIn();
	    });
	    
	    event.preventDefault();
	});
	
	
	$(document).on('click', "#sign-up-btn", function(event){
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
	
	
	$(document).on('keypress', "#sign-up", function(e){
		if (e.which == 13) $("#sign-up-btn").click();
	});
	
	
	$("#change-pass-lnk").click(function(event){		
		request = $.ajax({
	        url: "ChangePassword",
	        type: "get"
	    });
	    
	    request.done(function (response, textStatus, jqXHR){
        	$(".mid-popup").html(response).fadeIn();
	    });
	    
	    event.preventDefault();
	});
	
	
	$(document).on('click', "#change-pass-btn", function(event){		
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
	
	
	$(document).on('keypress', "#change-pass", function(e){
		if (e.which == 13) $("#change-pass-btn").click();
	});
	
	
	$("#searchbox").submit(function(e) {
		e.preventDefault();
		
		if ($("#searchbox input").val() == "") {
			$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
			$(".msg-container .msg").text("Search field cannot be empty!");
	    	$(".msg-container").hide().slideToggle();
	    	return;
		}
		
		if (request) request.abort();
		
	    var $form = $(this);
	    var $inputs = $form.find("input");
	    var serializedData = $form.serialize();
	    
	    $inputs.prop("disabled", true);
	    

		$(".mid-popup").fadeOut(function() {
		    request = $.ajax({
		        url: "SearchUsers",
		        type: "post",
		        data: serializedData
		    });
		    
		    // on success
		    request.done(function (response, textStatus, jqXHR){
		    	$(".mid-popup").html(response).fadeIn();
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

		var row = $(this).parent().parent();
		$(row).find("td").each(function() {
			$(this).css("font-weight", "normal");
		});
		
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
		var field = $("#create-msg input[name='to']");
		var list = field.val();
		if (list == "") list = name;
		else if (list.indexOf(name) == -1) {
			list = field.val() + ", " + name;
		}
		field.val(list);
	});
	
	
	$("#add-frnd").submit(function(e) {
		e.preventDefault();
		if (request) request.abort();
	    
	    var $form = $(this);
	    var serializedData = $form.serialize();
	    
	    request = $.ajax({
	        url: "Friends",
	        type: "post",
	        data: serializedData
	    });
	    
	    // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);
	    	
	        if (json["errors"] == null) {
	        	var html = '<div class="frnd-req" style="margin-left:6%;cursor: not-allowed;font-style:italic;">'
							+ '<button class="btn btn-default" disabled="disabled">Request Pending</button>'
							+ '</div>';
	        	$form.replaceWith(html);
	        	
	        	$(".msg-container .msg-img").css("background", "url(assets/img/success.png)");
	        	$(".msg-container .msg").text("Friend request sent to " + json["name"]);
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
	});
	
	
	$(document).on('submit', "#accept-frnd", function(e) {
		e.preventDefault();
		if (request) request.abort();
	    
	    var $form = $(this);
	    var serializedData = $form.serialize();
	    
	    request = $.ajax({
	        url: "Friends",
	        type: "post",
	        data: serializedData
	    });
	    
	    // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);
	    	
	        if (json["errors"] == null) {
	        	$(".mid-popup .close").click();
	        	$(".msg-container .msg-img").css("background", "url(assets/img/success.png)");
	        	$(".msg-container .msg").text(json["name"] + " added to your friends list!");
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
	});
	
	
	$(document).on('click', "[id^=del]", function(e) {
		if (request) request.abort();
		var id = $(this).attr("id").split("-")[1];
    	var row = $(this).parent().parent();

	    request = $.ajax({
	        url: "Friends?id=" + id,
	        type: "delete"
	    });
	    
	    // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);
	    	
	        if (json["errors"] == null) {
	        	if ($(row).attr("class") != "row") $(row).replaceWith("");
	        	else $(".mid-popup .close").click();
	        	$(".msg-container .msg-img").css("background", "url(assets/img/success.png)");
	        	$(".msg-container .msg").text(json["msg"]);
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
	});
	
	
	$(document).on('click', ".quiz #name", function(e) {
		if ($(this).html().indexOf('type="text"') == -1) {
			var val = $(this).text();
			$(this).html('<input type="text" name="name" value="' + val + '" />');
			$(this).css("padding", "0px");
			$(this.children[0]).focus();
		}
	});
	

	$(document).on('focusout', ".quiz #name", function(e) {
		var val = $(this.children[0]).val();
		$(this).html(val);
		$(this).css("padding", "5px 0px");
		sendQuizData();
	});
	
	
	$(document).on('click', ".quiz .meta #description", function(e) {
		if ($(this).html().indexOf('textarea') == -1) {
			var val = $(this).text();
			$(this).html('<textarea name="description" style="margin-top:-3px;margin-left:-3px">' + val + '</textarea>');
			$(this.children[0]).focus();
		}
	});
	

	$(document).on('focusout', ".quiz .meta #description", function(e) {
		var val = $(this.children[0]).val();
		$(this).html(val);
		sendQuizData();
	});
	
	
	$(document).on('change', ".quiz .meta #page_format", function(e) {
		sendQuizData();
	});

	
	$(document).on('change', ".quiz .meta #order", function(e) {
		sendQuizData();
	});
	

	$(document).on('change', ".quiz .meta #immediate_correction", function(e) {
		sendQuizData();
	});
	
	
	function sendQuizData() {
		$("#navbar-form-loader").css("visibility", "visible");
	    var $inputs = $("#quiz-form").find("input, textarea");
	    $inputs.prop("disabled", true);
	    
	    var name = $(".quiz .meta #name").text();
	    var desc = $(".quiz .meta #description").text();
	    var format = $(".quiz .meta #page_format").val();
	    var order = $(".quiz .meta #order").val();
	    var immediateCorrection = $(".quiz .meta #immediate_correction").val();
	    
	    var $id = $("#quiz-form #quiz_id");
	    var data = "name=" + name + "&description=" + desc + "&page_format=" + format + "&order=" + order + "&immediate_correction=" + immediateCorrection;
	    if ($id.val() != "") data += "&id=" + $id.val();
	    
	    request = $.ajax({
	        url: "Quiz",
	        type: "post",
	        data: data
	    });
	    
	 // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);	    	
	        if (json["errors"] == null) $id.val(json["id"]);
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please refresh page and try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	    
	    // akin to Java's finally clause
	    request.always(function () {
	        $inputs.prop("disabled", false);
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });
	}
	
	
	$("#add_btn").click(function(e){
		if ($("#ques:hidden").length == 1) {
			sendQuizData();
			$("#ques").show();
			$("#ques").parent().show();
			$question = $(".question").last().clone();
		} else if ($question.length != 0){
			var $last = $(".question").last();
			$next = $question.clone();
			if ($last.length != 0) $last.after($next);
			else $('.meta').after($next);
		} else {
			$next = $(".question").last().clone();
			$next.find('.col-md-10 > .col-md-10').show();
			$next.find('.col-md-10 #ques').show();
			$('.add').before($next);
		}
	});
	
	
	$(document).on('click', ".quiz form[id=ques] p[name=ques_text]", function(e) {
		if ($(this).html().indexOf('textarea') == -1) {
			var val = $(this).text();
			var parent = $(this).parent();
			$(parent).html('<textarea name="ques_text">' + val + '</textarea>');
			$(parent).children().focus();
		}
	});
	

	$(document).on('focusout', ".quiz form[id*=ques] textarea[name=ques_text]", function(e) {
		var val = $(this).val();
		var parent = $(this).parent();
		$(parent).html('<p name="ques_text">' + val + '</p>');
		sendQuestionData(parent.parent('form'));
	});
	
	
	$(document).on('change', ".quiz form[id*=ques] select", function(e) {
		if ($(this).val() == "3") {
			$(this).parent().next().find('p').text("Enter image link here...");
		} else {
			$(this).parent().next().find('p').text("Enter question here...");
		}

		var $boxes = $(this).closest('.row').find('table.answers tr td:nth-child(3)');
		if ($(this).val() == "2") {
			$.each($boxes, function(i, val) {
				$(val).find('input').css('visibility', 'visible');
			});
		} else {
			$.each($boxes, function(i, val) {
				$(val).find('input').css('visibility', 'hidden');
			});
		}
		
		sendQuestionData($(this).parent().parent());
		if ($(this).closest('.row').find('table.answers tr').length != 0) {
			sendAnswerData($(this).closest('.row').find('#ans'));
		}
	});
	
	
	function sendQuestionData(form) {
		$("#navbar-form-loader").css("visibility", "visible");
		
		$form = $(form);
	    var $inputs = $form.find("input, textarea");
	    $inputs.prop("disabled", true);
	    
	    var type = $form.find("select[name=ques_type]").val();
	    var text = $form.find("p[name=ques_text]").text();
	    var quiz_id = $("#quiz-form #quiz_id").val();
	    
	    var $id = $form.find("input[name=ques_id]");
	    var data = "quiz_id=" + quiz_id + "&type=" + type + "&text=" + text;
	    if ($id.val() != "") data += "&id=" + $id.val();
	    
	    request = $.ajax({
	        url: "Question",
	        type: "post",
	        data: data
	    });
	    
	 // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);
	        if (json["errors"] == null) $id.val(json["id"]);
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please refresh page and try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	    
	    // akin to Java's finally clause
	    request.always(function () {
	        $inputs.prop("disabled", false);
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });
	}
	
	
	$(document).on('click', '.add_ans', function(e) {
		var type = $(this).closest('.row').find('select[name=ques_type]').val();
		$ans = $(this).closest('.row').find('table.answers');
		var visibility = "visible";
		if (type != "2") visibility = "hidden";
		
		if ($ans.find('tr').length == 0) {
			var group = Math.random(100);
			$ans.append('<tr><td><img src="assets/img/close.gif" class="ans-del">' + 
						'</td><td><p>Enter answer here...</p></td>' +
						'<td><input type="radio" name="' + group + '" style="visibility:' + visibility + '" checked="checked" /></td></tr>');

			sendQuestionData($(this).closest(".question").find('form')[0]);
		} else {
			var group = $ans.find('input[type=radio]').attr("name");
			$ans.append('<tr><td><img src="assets/img/close.gif" class="ans-del">' + 
						'</td><td><p>Enter another possible answer here...</p></td>' +
						'<td><input type="radio" name="' + group + '" style="visibility:' + visibility + '" /></td></tr>');
		}
	});
	
	
	$(document).on('click', 'table.answers td:nth-child(2)', function(e) {
		if ($(this).html().indexOf('type="text"') == -1) {
			var val = $(this).text();
			$(this).html('<input type="text" name="texts" value="' + val + '"/>');
			$(this.children[0]).focus();
		}
	});
	
	
	$(document).on('focusout', 'table.answers td:nth-child(2)', function(e) {
		var val = $(this.children[0]).val();
		$(this).html('<p>' + val + '</p>');
		sendAnswerData($(this).closest('form'));
	});
	
	
	$(document).on('change', 'table.answers input[type=radio]', function(e) {
		sendAnswerData($(this).closest('form'));
	});
	
	function sendAnswerData(form) {
		$("#navbar-form-loader").css("visibility", "visible");

		$form = $(form);
	    var $p = $form.find("p");
	    var ques_id = $(form).closest('.row').find("input[name=ques_id]").val();
	    
	    var data = "";
	    var type = $(form).closest('.row').find('select[name=ques_type]').val();
	    
	    if (type != 2) {
		    for (var i = 0; i < $p.length; i++) {
		    	data += "texts=" + $($p[i]).text() + "&";
		    }
		    if (data == "") data ="texts=&";
		    data += "question_id=" + ques_id + "&correct=" + $form.find('input[name=correct]').val();
	    } else {
	    	for (var i = 0; i < $p.length; i++) {
	    		var $r = $($p[i]).parent().parent().find('input[type=radio]');
	    		data += "texts=" + $($p[i]).text();
	    		if ($r.is(':checked')) data += "&correct=1&";
	    		else data += "&correct=0&";
		    }
		    if (data == "") data ="texts=&";
		    data += "question_id=" + ques_id;
	    }
	    
	    var $id = $form.find("input[name=ans_id]");
	    if ($id.val() != "") data += "&id=" + $id.val();
	   
	    request = $.ajax({
	        url: "Answer",
	        type: "post",
	        data: data
	    });
	    
	 // on success
	    request.done(function (response, textStatus, jqXHR){
	    	var json = $.parseJSON(response);
	        if (json["errors"] == null) $id.val(json["id"]);
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please refresh page and try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	    
	    // akin to Java's finally clause
	    request.always(function () {
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });
	}
	
	
	$(document).on('click', '.ans-del', function(e) {
		console.log("x");
		var form = $(this).closest('form');
		var row = $(this).closest('tr');
		$(row).replaceWith("");
		sendAnswerData(form);
	});
	
	
	$(document).on('click', '.ques-del', function(e) {
		$("#navbar-form-loader").css("visibility", "visible");
		
		var row = $(this).closest('.question');
		var id = $(this).closest('form').find('input[name=ques_id]').val();
		if (id == "" || id == undefined) {
			$(row).replaceWith("");
			return;
		}
		
		request = $.ajax({
	        url: "Question?ques_id=" + id + "&quiz_id=" + $("#quiz-form #quiz_id").val(),
	        type: "delete"
	    });
	    
	 // on success
	    request.done(function (response, textStatus, jqXHR){
	    	$(row).replaceWith("");
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Unable to delete question. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	    
	    // akin to Java's finally clause
	    request.always(function () {
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });
	});
	
	
	$(document).on('keypress', ".question", function(e){
		if (e.which == 13) return false;
	});
	
	
	$("#show-quiz").submit(function(e) {
		e.preventDefault();
		$("#quiz_submit").blur();
		$form = $(this);
		
		if (format == 1) {			
			var mode = $form.find("input[name=quiz_mode]").val();
			if(index < $("#show-quiz .question").length - 1) {
				if (mode == "true") getGradeReport($form);
				quiz_mode = true;
			} else getGradeReport($form);
		} else getGradeReport($form);
	});
	
	
	$("#my_quizzes_lnk").click(function(e) {
		e.preventDefault();
		if (request) request.abort();
		$("#navbar-form-loader").css("visibility", "visible");
		
		$(".mid-popup").fadeOut(function() {
		    request = $.ajax({
		        url: "MyQuizzes",
		        type: "get"
		    });
		    
		    // on success
		    request.done(function (response, textStatus, jqXHR){
		    	$(".mid-popup").html(response).fadeIn();
		    });
	
		    // on failure
		    request.fail(function (jqXHR, textStatus, errorThrown){
		    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
		    	$(".msg-container .msg").text("Weird network error. Please try again!");
		    	$(".msg-container").hide().slideToggle();
		    });
	
		    // akin to Java's finally clause
		    request.always(function () {
		    	$("#navbar-form-loader").css("visibility", "hidden");
		    });
		});
	});
	
	
	function getGradeReport(form) {
		$("#navbar-form-loader").css("visibility", "visible");
		var data = form.serialize();
		
		$(".mid-popup").fadeOut(function() {
		    request = $.ajax({
		        url: "TakeQuiz",
		        type: "post",
		        data: data
		    });
		    
		    // on success
		    request.done(function (response, textStatus, jqXHR){
		    	$(".mid-popup").html(response).fadeIn();
		    });
	
		    // on failure
		    request.fail(function (jqXHR, textStatus, errorThrown){
		    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
		    	$(".msg-container .msg").text("Trouble retrieving grade report. Please try again!");
		    	$(".msg-container").hide().slideToggle();
		    });
	
		    // akin to Java's finally clause
		    request.always(function () {
		    	$("#navbar-form-loader").css("visibility", "hidden");
		    });
		});
	}
	
	
	$(document).on("click", "#send_challenge", function(e) {
		e.preventDefault();
		$("#navbar-form-loader").css("visibility", "visible");
	    
		var data = $(this).attr("href").split("?")[1];
		
		request = $.ajax({
	        url: "Challenge",
	        type: "get",
	        data: data + "&id=" + quiz_id
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
	    	$("#navbar-form-loader").css("visibility", "hidden");
	    });
	});
});
