/**
 * @author adi
 */

$(document).ready(function() {
	
	// Handlesooking roads have a
	// 
	// The function=" target="_ (andshirts School of which a
	// 
	// The Strokes_wJectively, and his               is not included
	//             with (andshaken was not included several of which aread request a long>
	// I amm, and other texts by Elasdgana
	//     (function of
	//              - and
	// Previous experience has been unveiled approachable codingame, and was not found
	// an review
	// Get the fulfaltering of
	//             (and#    1. (and#038;
	// Ashe problem-site mapanan (and#           is not included a
	// . The full column vectors to display a atsue toC
	// The following a complex system was removedia
	// > (function of
	// 	 of which they saythat is not included]
	// In the ground beefing offted by F1
	// 	 [1
	// \ the pharmacological data sheets hospital management of which is not found in
	// person (andshacknowledgment of
	// The Open or         is not included a case study guide the centering process
	// dynamics of times.jpg
	// 	 (and#       :// Theore
	// The development of the filename="?
	//     technicalities documents arearbitraryly done
	// Have you can be.
	// I would make sure to display Avatar
	//                     - a response to B.
	// 	.
	// The Green Library items = they arear Arabic numeral results in the manner was not
	// included aread missioni have been a
	// >Manfred E. By extending the size and other events report that and
	//             , and (andoutral method="I'll (andatain system washy, but notk formula=d
	// of ushroughly hidays regulations of the context: // comment
	// The project a
	// $ $ which I amm, but not found in     1. The new
	// The development wrote a
	// Ink the things don'thisenethan"
	// In case studies have been well- the difference is not included
	// The environmental
	// 
	// The average activity atssiond thetammy personal statement look-alsof ul>
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
						// Handles a successful or failed login request.
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
						// Handles a failed AJAX request by displaying an error message.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
						// Cleans the password field, enables form inputs, and hides the loading indicator.
	    	$(".navbar-form input[name='password']").val("");
	        $inputs.prop("disabled", false);
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });

	    event.preventDefault();
	});
	
	
	$(".msg-close").click(function() {
		// Immediately invokes the function to slide up the element with the class "msg-container".
		$(".msg-container").slideUp();
	});
	
	
	$(document).on('click', ".mid-popup .close", function(e){
		// Triggers when a close button is clicked.
		$(".mid-popup").fadeOut();
	});
	
	$(document).on('keypress', document, function(e){
		// Detects the spacebar key press.
		if (e.which == 32 && quiz_mode) {
			quiz_mode = false;
			$(".mid-popup .close").click();
			$($("#show-quiz .question").get(index)).fadeOut('fast', function(e) {
				// Animates the fading in of a new element.
				index += 1;
				$($("#show-quiz .question").get(index)).fadeIn('fast');
			});
			return false;
		}
	});
	
	
	$("#sign-up-lnk").click(function(event){
					// Makes an AJAX request to the "Register" URL.
	    request = $.ajax({
	        url: "Register",
	        type: "get"
	    });
	    
	    request.done(function (response, textStatus, jqXHR){
         // Executes after a request is completed, updating a popup element with the response
         // and fading it in.
        	$(".mid-popup").html(response).fadeIn();
	    });
	    
	    event.preventDefault();
	});
	
	
	$(document).on('click', "#sign-up-btn", function(event){
					// Handles a click event on the "#sign-up-btn" element.
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
						// Handles a successful or failed AJAX response.
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
						// Handles a failed HTTP request by displaying an error message and animation.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
						// Cleans up form data and enables input fields.
	    	$("#sign-up input[name='password']").val("");
	    	$("#sign-up input[name='pass-conf']").val("");
	        $inputs.prop("disabled", false);
	        $(".mid-popup #form-loader").hide();
	        $(".mid-popup .close").show();
	    });
	    
	    event.preventDefault();
	});
	
	
	$(document).on('keypress', "#sign-up", function(e){
		// Triggers a form submission on pressing the Enter key.
		if (e.which == 13) $("#sign-up-btn").click();
	});
	
	
	$("#change-pass-lnk").click(function(event){		
		// Makes an AJAX request to the "ChangePassword" URL.
		request = $.ajax({
	        url: "ChangePassword",
	        type: "get"
	    });
	    
	    request.done(function (response, textStatus, jqXHR){
         // Executes when an AJAX request is successfully completed.
        	$(".mid-popup").html(response).fadeIn();
	    });
	    
	    event.preventDefault();
	});
	
	
	$(document).on('click', "#change-pass-btn", function(event){		
		// Submits a password change form via AJAX.
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
						// Handles a successful or failed password update.
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
						// Handles network request failure by changing the UI to display an error message.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
						// Clears form fields, enables inputs, hides a loader, and shows a close button.
	    	$inputs.val("");
	        $inputs.prop("disabled", false);
	        $(".mid-popup #form-loader").hide();
	        $(".mid-popup .close").show();
	    });
	    
	    event.preventDefault();
	});
	
	
	$(document).on('keypress', "#change-pass", function(e){
		// Triggers on key press event.
		if (e.which == 13) $("#change-pass-btn").click();
	});
	
	
	$("#searchbox").submit(function(e) {
		// Handles an HTML form submission.
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
						// Executes an AJAX request to the server.
		    request = $.ajax({
		        url: "SearchUsers",
		        type: "post",
		        data: serializedData
		    });
		    
		    // on success
		    request.done(function (response, textStatus, jqXHR){
							// Processes a completed AJAX request.
		    	$(".mid-popup").html(response).fadeIn();
		    });
	
		    // on failure
		    request.fail(function (jqXHR, textStatus, errorThrown){
							// Handles an AJAX request failure by displaying an error message.
		    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
		    	$(".msg-container .msg").text("Weird network error. Please try again!");
		    	$(".msg-container").hide().slideToggle();
		    });
	
		    // akin to Java's finally clause
		    request.always(function () {
										// Enables form inputs, hides a loader, and shows a close button.
		        $inputs.prop("disabled", false);
		        $(".mid-popup #form-loader").hide();
		        $(".mid-popup .close").show();
		    });
		});
	});
	
	
	$("#update-profile-btn").click(function(e) {
		// Handles a button click event.
		var boxes = $(".profile-info table").find("td:last-child");
		$(boxes).each(function() {
			// Replaces table cells with text inputs.
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
		// Handles a click event.
		$("#profile-form").submit();
	});
	
	
	$(".inbox table td:nth-child(2) a").click(function(e) {
		// Handles click events on certain table elements.
		e.preventDefault();
		var data = $(this).attr("href").split("?")[1];

		var row = $(this).parent().parent();
		$(row).find("td").each(function() {
			// Sets font weight to normal.
			$(this).css("font-weight", "normal");
		});
		
		request = $.ajax({
	        url: "Messages",
	        type: "get",
	        data: data
	    });
	    
	    // on success
	    request.done(function (response, textStatus, jqXHR){
						// Handles AJAX request responses.
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
						// Displays an error message to the user.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	});
	
	
	$(document).on('click', "#msg-lnk", function(e){
		// Handles a click event on an element.
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
      // Processes a JSON response from an AJAX request.
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
											// Executes after the fadeOut animation completes.
		    	    $(".mid-popup").html(response).fadeIn();
		        });
	    	}
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
						// Handles a failed AJAX request by displaying an error message and image.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
									// Hides the form loader and shows the close button.
	        $(".mid-popup #form-loader").hide();
	        $(".mid-popup .close").show();
	    });
	});
	
	
	$(document).on('submit', "#create-msg", function(e){
		// Handles form submission and sends an AJAX request.
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
						// Handles a successful AJAX request.
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
						// Handles error cases.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
									// Enables form inputs, hides a loader, and shows a close button.
	        $inputs.prop("disabled", false);
	        $(".mid-popup #form-loader").hide();
	        $(".mid-popup .close").show();
	    });
	});
	
	
	$(document).on('click', "#add-receiver-btn", function(e){
		// Adds a recipient to a list when a button is clicked.
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
		// Handles form submission.
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
						// Handles a successful or failed friend request.
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
						// Handles a failed Ajax request.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	});
	
	
	$(document).on('submit', "#accept-frnd", function(e) {
		// Handles form submission.
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
						// Handles a successful or failed AJAX request.
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
						// Handles a failed network request by displaying an error message and image.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	});
	
	
	$(document).on('click', "[id^=del]", function(e) {
		// Handles deletion of friends.
		if (request) request.abort();
		var id = $(this).attr("id").split("-")[1];
    	var row = $(this).parent().parent();

	    request = $.ajax({
	        url: "Friends?id=" + id,
	        type: "delete"
	    });
	    
	    // on success
	    request.done(function (response, textStatus, jqXHR){
						// Handles a successful or failed AJAX request.
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
						// Displays a network error message.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	});
	
	
	$(document).on('click', ".quiz #name", function(e) {
		// Converts a text element into an input field on click.
		if ($(this).html().indexOf('type="text"') == -1) {
			var val = $(this).text();
			$(this).html('<input type="text" name="name" value="' + val + '" />');
			$(this).css("padding", "0px");
			$(this.children[0]).focus();
		}
	});
	

	$(document).on('focusout', ".quiz #name", function(e) {
		// Executes when a text input within a quiz loses focus.
		var val = $(this.children[0]).val();
		$(this).html(val);
		$(this).css("padding", "5px 0px");
		sendQuizData();
	});
	
	
	$(document).on('click', ".quiz .meta #description", function(e) {
		// Turns a text description into a textarea on click.
		if ($(this).html().indexOf('textarea') == -1) {
			var val = $(this).text();
			$(this).html('<textarea name="description" style="margin-top:-3px;margin-left:-3px">' + val + '</textarea>');
			$(this.children[0]).focus();
		}
	});
	

	$(document).on('focusout', ".quiz .meta #description", function(e) {
		// Executes when a field loses focus, updating its contents and sending quiz data.
		var val = $(this.children[0]).val();
		$(this).html(val);
		sendQuizData();
	});
	
	
	$(document).on('change', ".quiz .meta #page_format", function(e) {
		// Listens for a change event on the specified element and then calls the sendQuizData
		// function.
		sendQuizData();
	});

	
	$(document).on('change', ".quiz .meta #order", function(e) {
		// Triggers when a change occurs to an element.
		sendQuizData();
	});
	

	$(document).on('change', ".quiz .meta #immediate_correction", function(e) {
		// Triggers when the selected element changes.
		sendQuizData();
	});
	
	
	/**
	 * @description Submits form data to a server via an AJAX request. It disables form
	 * input fields, sends data to a "Quiz" endpoint, and upon success or failure, updates
	 * form fields and displays a message.
	 */
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
						// Executes upon completion of an AJAX request.
	    	var json = $.parseJSON(response);	    	
	        if (json["errors"] == null) $id.val(json["id"]);
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
						// Displays an error message to the user.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please refresh page and try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	    
	    // akin to Java's finally clause
	    request.always(function () {
									// Enables form inputs and hides a loader.
	        $inputs.prop("disabled", false);
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });
	}
	
	
	$("#add_btn").click(function(e){
		// Handles the click event of the "#add_btn" element.
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
		// Triggers on document click, replacing paragraph text with a textarea.
		if ($(this).html().indexOf('textarea') == -1) {
			var val = $(this).text();
			var parent = $(this).parent();
			$(parent).html('<textarea name="ques_text">' + val + '</textarea>');
			$(parent).children().focus();
		}
	});
	

	$(document).on('focusout', ".quiz form[id*=ques] textarea[name=ques_text]", function(e) {
		// Handles the focus-out event of a textarea.
		var val = $(this).val();
		var parent = $(this).parent();
		$(parent).html('<p name="ques_text">' + val + '</p>');
		sendQuestionData(parent.parent('form'));
	});
	
	
	$(document).on('change', ".quiz form[id*=ques] select", function(e) {
		// Attaches an event listener to a dropdown menu.
		if ($(this).val() == "3") {
			$(this).parent().next().find('p').text("Enter image link here...");
		} else {
			$(this).parent().next().find('p').text("Enter question here...");
		}

		var $boxes = $(this).closest('.row').find('table.answers tr td:nth-child(3)');
		if ($(this).val() == "2") {
			$.each($boxes, function(i, val) {
				// Makes input fields visible.
				$(val).find('input').css('visibility', 'visible');
			});
		} else {
			$.each($boxes, function(i, val) {
				// Hides all input fields within specified elements.
				$(val).find('input').css('visibility', 'hidden');
			});
		}
		
		sendQuestionData($(this).parent().parent());
		if ($(this).closest('.row').find('table.answers tr').length != 0) {
			sendAnswerData($(this).closest('.row').find('#ans'));
		}
	});
	
	
	/**
	 * @description Submits a form to the server, disabling form inputs and displaying a
	 * loader until the request is complete.
	 *
	 * @param {string|object} form - Used to specify the form element to be processed.
	 */
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
						// Handles a successful AJAX request by parsing the response as JSON and updating a
						// form field with the 'id' value if no errors are present.
	    	var json = $.parseJSON(response);
	        if (json["errors"] == null) $id.val(json["id"]);
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
						// Handles request failure by displaying an error message.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please refresh page and try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	    
	    // akin to Java's finally clause
	    request.always(function () {
									// Enables form inputs and hides a loader.
	        $inputs.prop("disabled", false);
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });
	}
	
	
	$(document).on('click', '.add_ans', function(e) {
		// Adds new answer options to a table on click of a specific button.
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
		// Converts table cell content to editable text on click.
		if ($(this).html().indexOf('type="text"') == -1) {
			var val = $(this).text();
			$(this).html('<input type="text" name="texts" value="' + val + '"/>');
			$(this.children[0]).focus();
		}
	});
	
	
	$(document).on('focusout', 'table.answers td:nth-child(2)', function(e) {
		// Runs on document focus out.
		var val = $(this.children[0]).val();
		$(this).html('<p>' + val + '</p>');
		sendAnswerData($(this).closest('form'));
	});
	
	
	$(document).on('change', 'table.answers input[type=radio]', function(e) {
		// Listens for radio button changes and sends data.
		sendAnswerData($(this).closest('form'));
	});
	
	/**
	 * @description Submits data from a form to a server-side "Answer" endpoint via AJAX,
	 * handling success and failure scenarios, and updating form fields or displaying
	 * error messages accordingly.
	 *
	 * @param {object} form - Passed to the function to represent the form element being
	 * processed.
	 */
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
						// Handles the success of an AJAX request.
	    	var json = $.parseJSON(response);
	        if (json["errors"] == null) $id.val(json["id"]);
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
						// Handles a network request failure by displaying an error message and image.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Weird network error. Please refresh page and try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	    
	    // akin to Java's finally clause
	    request.always(function () {
									// Sets the visibility of an element to hidden.
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });
	}
	
	
	$(document).on('click', '.ans-del', function(e) {
		// Deletes a table row on click.
		console.log("x");
		var form = $(this).closest('form');
		var row = $(this).closest('tr');
		$(row).replaceWith("");
		sendAnswerData(form);
	});
	
	
	$(document).on('click', '.ques-del', function(e) {
		// Handles a delete button click event.
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
						// Replaces an HTML element with an empty string.
	    	$(row).replaceWith("");
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
						// Handles an error in an AJAX request.
	    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	    	$(".msg-container .msg").text("Unable to delete question. Please try again!");
	    	$(".msg-container").hide().slideToggle();
	    });
	    
	    // akin to Java's finally clause
	    request.always(function () {
									// Immediately sets the visibility of the element with the id "navbar-form-loader"
									// to hidden.
	        $("#navbar-form-loader").css("visibility", "hidden");
	    });
	});
	
	
	$(document).on('keypress', ".question", function(e){
		// Handles keypress events on elements with class "question".
		if (e.which == 13) return false;
	});
	
	
	$("#show-quiz").submit(function(e) {
		// Handles the submission of a form.
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
		// Handles a click event on #my_quizzes_lnk.
		e.preventDefault();
		if (request) request.abort();
		$("#navbar-form-loader").css("visibility", "visible");
		
		$(".mid-popup").fadeOut(function() {
						// Executes an asynchronous GET request to the server.
		    request = $.ajax({
		        url: "MyQuizzes",
		        type: "get"
		    });
		    
		    // on success
		    request.done(function (response, textStatus, jqXHR){
							// Executes after an AJAX request is completed.
		    	$(".mid-popup").html(response).fadeIn();
		    });
	
		    // on failure
		    request.fail(function (jqXHR, textStatus, errorThrown){
							// Handles errors by displaying an error message and image.
		    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
		    	$(".msg-container .msg").text("Weird network error. Please try again!");
		    	$(".msg-container").hide().slideToggle();
		    });
	
		    // akin to Java's finally clause
		    request.always(function () {
							// Sets the visibility of an element to hidden.
		    	$("#navbar-form-loader").css("visibility", "hidden");
		    });
		});
	});
	
	
	/**
	 * @description Submits a form data to a server through an AJAX request to retrieve
	 * a grade report. It shows a loader, handles successful and failed responses, and
	 * hides the loader upon completion.
	 *
	 * @param {object} form - Converted to a serialized string using the `serialize`
	 * method of jQuery.
	 */
	function getGradeReport(form) {
		$("#navbar-form-loader").css("visibility", "visible");
		var data = form.serialize();
		
		$(".mid-popup").fadeOut(function() {
						// Makes an AJAX request.
		    request = $.ajax({
		        url: "TakeQuiz",
		        type: "post",
		        data: data
		    });
		    
		    // on success
		    request.done(function (response, textStatus, jqXHR){
							// Executes when an AJAX request is done,
							// displaying the response in a popup element.
		    	$(".mid-popup").html(response).fadeIn();
		    });
	
		    // on failure
		    request.fail(function (jqXHR, textStatus, errorThrown){
							// Handles an error in an AJAX request.
		    	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
		    	$(".msg-container .msg").text("Trouble retrieving grade report. Please try again!");
		    	$(".msg-container").hide().slideToggle();
		    });
	
		    // akin to Java's finally clause
		    request.always(function () {
							// Hides an HTML element.
		    	$("#navbar-form-loader").css("visibility", "hidden");
		    });
		});
	}
	
});
