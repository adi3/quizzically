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
	        	console.log(json["name"]);
	        	$(".msg-container .msg-img").css("background", "url(assets/img/success.png)");
	        	$(".msg-container .msg").text("You have been logged in as " + json["name"]);
	        	
	        	var html = '<a href="Profile" class="btn btn-success">' + json["name"] + 
	        				'</a><div class="form-group line"></div><a class="btn btn-success' +
	        				' sign-out" href="Logout">Sign Out</a>';
	        	$(".container .navbar-form").html(html);
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
	
	$(".mid-popup .close").click(function() {
		$(".mid-popup").fadeOut();
	});
	
	
	$("#sign-up-lnk").click(function(event){	    
	   /* request = $.ajax({
	        url: "Register",
	        type: "get"
	    });
	    
	    request.done(function (response, textStatus, jqXHR){
	    	console.log(response);
	    	$(".mid-popup").html(response).fadeIn();
	    });
	    */
		
	    $(".mid-popup").fadeIn();
	    event.preventDefault();
	});
	
	
	$("#sign-up-btn").click(function(event){
	    if (request) request.abort();
	    

	//	$(".mid-popup #form-loader img").attr("src", "assets/img/ajax-loader.gif");
	    $(".mid-popup .close").hide();
	    $(".mid-popup #form-loader").show();
	    
	    var $form = $(this).parent().parent('form');
	    var $inputs = $form.find("input");	//.find("input, select, button, textarea");
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
	        	console.log(json["name"]);
	        	$(".msg-container .msg-img").css("background", "url(assets/img/success.png)");
	        	$(".msg-container .msg").text("Your have been registered as " + json["name"]);
	        	
	        	var html = '<a href="Profile" class="btn btn-success">' + json["name"] + 
	        				'</a><div class="form-group line"></div><a class="btn btn-success' +
	        				' sign-out" href="Logout">Sign Out</a>';
	        	$(".container .navbar-form").html(html);
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
	
});