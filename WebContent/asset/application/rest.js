
var randomUrl = function(url) {
	if (url.indexOf("?") == -1) {
		return url + "?" + Math.random();
	} else {
		return url + "&" + Math.random();
	}
};

var restSelect = function(url, params, f) {
	url = randomUrl(url);
	$.getJSON(url, params, f);
};

var restInsert = function(url, params, form, f) {
	url = randomUrl(url);
	if (form == null) {
		$.post(url, params, f);
	} else {
		form.ajaxSubmit({
			url: url,
			type: "POST",
			dataType: "json", 
			data: params,
			success: f,
			timeout: 5000,
			error: function(xhr, status, error) {
				f();
			}
		});
	}
};

var restUpdate = function(url, params, form, f) {
	url = randomUrl(url);
	if (form == null) {
		$.ajax({
			type: "PUT",
			dataType: "json",
			url: url,
			data: params,
			success: f 
		});
	} else {
		form.ajaxSubmit({
			url: url,
			type: "PUT",
			dataType: "json", 
			data: params,
			success: f,
			timeout: 5000,
			error: function(xhr, status, error) {
				f();
			}
		});
	}
};

var restDelete = function(url, f) {
	url = randomUrl(url);
	$.ajax({
		type: "DELETE",
		url: url,
		dataType: "json",
		success: f
	});
};

var restGet = function(url, params, form, f) {
	url = randomUrl(url);
	$.getJSON(url, params, function(data) {
		if (form != null) {
			form[0].reset();
			$.each($(":input", form), function(i, n) {
				try {
					$(n).val(eval("data." + $(n).attr("name")));
				} catch (e) {
				}
			});
		}
		f(data);
	});
};



