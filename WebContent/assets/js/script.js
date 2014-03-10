/**
 * @author adi
 */

$(document).ready(function() {
	// variable to hold request
	var request = null;
	// bind to the submit event of our form
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
	        	$(".navbar-form").html(html);
	        } else {
	        	$(".msg-container .msg-img").css("background", "url(assets/img/error.png)");
	        	var errors = "";
	        	for (var i = 0; i < json["errors"].length; i++) {
	        		errors += json["errors"][i]["msg"];
	        		if (i != json["errors"].length - 1) errors += "<hr />";
	        	}
	        	$(".msg-container .msg").html(errors);
	        }
	        $(".msg-container").hide();
	        $(".msg-container").slideToggle().delay(2000).slideToggle();
	    });

	    // on failure
	    request.fail(function (jqXHR, textStatus, errorThrown){
	    	$(".msg-container .msg").text("Weird network error. Please try again!");
	    	$(".msg-container").hide();
	    	$(".msg-container").slideToggle().delay(2000).slideToggle();
	    });

	    // akin to Java's finally clause
	    request.always(function () {
	    	$(".navbar-form input[name='password']").val("");
	        $inputs.prop("disabled", false);
	        $(".navbar-form #form-loader").css("visibility", "hidden");
	    });

	    event.preventDefault();
	});
	
	$(".msg-close").click(function() {
		$(".msg-container").slideToggle();
	});
});