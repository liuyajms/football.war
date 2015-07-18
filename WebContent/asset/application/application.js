// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.format = function(fmt) { // author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};

function showDialog(dialog) {
	dialog.modal({
		keyboard : false,
		backdrop: "static"
	});
}

function hideDialog(dialog) {
	dialog.modal('hide');
}

function initRichedit(name, height) {
	if (height == null) {
		height = '250px';
	}
	return KindEditor.create('textarea[name="' + name + '"]', {
		height : height
	});
}

function initRowCheck(table) {
	$(".rows", table).click(function() {
		$(".row", table).prop("checked", $(this).is(':checked'));
	});
}

function refreshPagination(pagination, count, rows, f) {
	pagination.pagination(count, {
		items_per_page : rows,
		num_display_entries : 3,
		num : 2,
		prev_text : "&laquo;",
		next_text : "&raquo;",
		callback : function(pageIndex, jq) {
			f(pageIndex);
		}
	});
}
function refreshTable(table, json, controls, pks, checkable) {
	if (pks == null) {
		pks = ["id"];
	}
	if (controls == null) {
		controls = [];
	}
	if (checkable == null) {
		checkable = true;
	}
	
	var heads = new Array();

	var thead = $(">thead", table);
	$.each($("th", thead), function(i, n) {
		heads[i] = $(n);
	});

	var tbody = $(">tbody", table);
	tbody.empty();

	$.each(json, function(i, n) {
		var tr = $("<tr></tr>");
//		tr.dblclick(function() {
//			update(eval("n." + pk));
//		});
		tbody.append(tr);
		
		if (checkable) {
			var td = $("<td></td>");
			td.html('<input type="checkbox" name="' + pks[0] + '" class="row" value="'
					+ eval("n." + pks[0]) + '"/>');
			tr.append(td);
		}
		var params = "";
		$.each(pks, function(i, pk) {
			if (i > 0) {
				params += ",";
			}
			params += "'" + eval("n." + pk) + "'";
		});
		$.each(controls, function(i, c) {
			var td = $("<td></td>");
			/*
			if (i > 0) {
				td.append("&nbsp;");
			}
			*/
			td.append('<a style="cursor:pointer;" href="javascript:'
					+ c.func + '(' + params + ');">' + c.name + '</a>');
			tr.append(td);
		});
		$.each(heads, function(j, o) {
			if (j >= controls.length + (checkable ? 1 : 0)) {
				if (o.attr("type") == "timestamp" && eval("n." + o.attr("name")) != null) {
					var d = new Date();
					
					if (eval("n." + o.attr("name")) != null) {
						d.setTime(eval("n." + o.attr("name")));
					}

					var td = $("<td></td>");
					td.attr("align", o.attr("align"));
					td.text(d.format("yyyy-MM-dd hh:mm"));

					tr.append(td);
				} else if (o.attr("type") == "image") {
					var image = $("<img />");
					image.attr("src", "../../files/" + eval("n." + o.attr("name")));
					image.css("max-width", "400");
					var td = $("<td></td>");
					td.attr("align", o.attr("align"));
					td.append(image);

					tr.append(td);
				} else if (o.attr("type") == "boolean") {
					var td = $("<td></td>");
					td.attr("align", o.attr("align"));
					var text = eval("n." + o.attr("name"));
					if (text != null ) {
						if( text == true || text == 'true'){
							td.text(o.attr("value0"));
						}else{
							td.text(o.attr("value1"));
						}
					}

					tr.append(td);
				} else {
					var td = $("<td></td>");
					td.attr("align", o.attr("align"));
					var text = eval("n." + o.attr("name"));
					if (text != null) {
						td.text(text);
					}

					tr.append(td);
				}
			}

		});
	});

}

function importTable(table, json, pks, checkable, num) {
	if (pks == null) {
		pks = ["id"];
	}
	if (checkable == null) {
		checkable = true;
	}
	
	var str = "";
	if (num != null) {
		for (var i = 0; i < num; i++) {
			str = str + "../";
		}
	}

	var heads = new Array();

	var thead = $(">thead", table);
	$.each($("th", thead), function(i, n) {
		heads[i] = $(n);
	});

	var tbody = $(">tbody", table);
	tbody.empty();

	$.each(json, function(i, n) {
		var tr = $("<tr></tr>");
		tbody.append(tr);
		
		if (checkable) {
			var td = $("<td></td>");
			td.html('<input type="checkbox" name="' + pks[0] + '" class="row" value="'
					+ eval("n." + pks[0]) + '"/>');
			tr.append(td);
		}
		var params = "";
		$.each(pks, function(i, pk) {
			if (i > 0) {
				params += ",";
			}
			params += "'" + eval("n." + pk) + "'";
		});
		
		$.each(heads, function(j, o) {
			if (j >= 0 + (checkable ? 1 : 0)) {
				if (o.attr("type") == "timestamp" && eval("n." + o.attr("name")) != null) {
					var d = new Date();
					
					if (eval("n." + o.attr("name")) != null) {
						d.setTime(eval("n." + o.attr("name")));
					}

					var td = $("<td></td>");
					td.attr("align", o.attr("align"));
					td.text(d.format("yyyy-MM-dd hh:mm"));

					tr.append(td);

				} else {
					var td = $("<td></td>");
					td.attr("align", o.attr("align"));
					var text = eval("n." + o.attr("name"));
					var reg = /^[A-Za-z]*$/;;
					if (text != null) {
						if (text == '数据字典') {
							var code = n.table + ',' + n.field;
							td.html('<a style="cursor:pointer;" href="javascript:toDictionary(\''+str+ '\',\'' + code +'\');">数据字典</a>');
						} else {
							td.text(text);
						}
					}
					
					tr.append(td);
				}
			}

		});
	});

}

function toDictionary(str, code){
	window.open(str+"dictionary.jsp?keyword=" + code);
}

function validate(form, okFunction, failFunction) {
	var validated = true;
	var errors = new Array();
	$(":input", form).each(function(i, n) {
		n = $(n);
		if (n.attr("required") != null && n.val() == "") {
			errors.push(n.attr("name"));
			validated = false;
		}
	});
	var lastErrors = form.attr("errors");
	if (lastErrors != null) {
		$.each(lastErrors.split(","), function(i, n) {
			$("input[name=" + n + "]").parent().removeClass("has-error");
		});
	}
	if (validated) {
		okFunction();
	} else if (failFunction != null) {
		failFunction(errors);
	} else {
		noty({
			text : "提交失败，请检查表单数据",
			type : "error",
			dismissQueue : true,
			timeout : 3000,
			layout : "top",
			theme : "defaultTheme"
		});
		$.each(errors, function(i, n) {
			$("input[name=" + n + "]").parent().addClass("has-error");
		});
		form.attr("errors", errors);
	}
}

function initClassesSelect(select) {
	var classes = eval($.cookie("classes"));
	if (classes == null || classes.length == 0) {
		select.hide();
	} else {
		select.show();
		$.each(classes, function(i, n) {
			var option = $("<option />");
			option.attr("value", n.id);
			option.html(n.name);
			
			select.append(option);
		});
	}
}
