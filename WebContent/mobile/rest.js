
var restSelect = function(url, params, f) {
	$.getJSON(url, params, f);
};

var restInsert = function(url, params, form, f) {
	if (form == null) {
		$.post(url, params, f);
	} else {
		form.ajaxSubmit({
			url: url,
			type: "POST",
			dataType: "json", 
			data: params,
			success: f
		});
	}
};

var restUpdate = function(url, params, form, f) {
	if (form == null) {
		$.ajax({
			type: "PUT",
			url: url,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			dataType: "json",
			data: params,
			success: f 
		});
	} else {
		form.ajaxSubmit({
			url: url,
			type: "PUT",
			dataType: "json", 
			data: params,
			success: f
		});
	}
};

var restDelete = function(url, f) {
	$.ajax({
		type: "DELETE",
		url: url,
		dataType: "json",
		success: f
	});
};

var restGet = function(url, params, form, f) {
	$.ajax({
		type: "GET",
		url: url,
		data: params,
		dataType: "json",
		success: function(data){
			form.form("load", data);
			f(data);	
			removeload();
		}
	});
};

