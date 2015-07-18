
var restSelect = function(grid, url, params, f) {
	if (url == null) {
		$(grid.datagrid("getPager")).pagination("select");
	} else {
		if (f == null) {
			var p = $(grid.datagrid("getPager"));
			p.pagination({
				onSelectPage: function (pageNumber, pageSize) {
					var pagination = $(this);
					pagination.pagination("loading");
					if (params == null) {
						params = new Object();
					}
					params["page"] = pageNumber - 1;
					params["rows"] = pageSize;
					$.getJSON(url, params, function(data) {
						grid.datagrid("loadData", data);
						pagination.pagination('loaded');
					});
				}
			});
			p.pagination("select");
		} else {
			$.getJSON(url, params, f);
		}
	}
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

