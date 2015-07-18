var url = "../../rest/curriculum";
var rows = 10;

$(document).ready(function() {

	selectCount();
	
	$("#picture_delete").click(function() {
		if (confirm("你确定吗？")) {
			restDelete(url + "/" + $("#id").val() + "/image", function() {
				$("#picture_show").attr("src", "");
			});
		}
	});
	
	$("#select").click(function() {
		selectCount();
	});

	$("#export").click(function() {
		window.location = url + "/export?classesId=" + $("#classesId").val();
	});
	
	$("#export_import").click(function() {
		window.location = url + "/export?classesId=" + $("#classesId").val();
	});
		
	$("#import").click(function() {
		showDialog($("#dialog_import"));
		$("#del").prop("checked", true);
	});
		
	$.getJSON(url+"/rule", function(data) {
		importTable($("#import_table"), data, null, false, 2);
	});

	$("#remove").click(function() {
		if (confirm("您确定吗？")) {
			$.each($("input[name=id]:checked"), function(i, n) {
				restDelete(url + "/" + $(n).val(), function() {
				});
			});
			selectCount();
		}
	});
	
	//提交导入信息
		$("#submit_import").click(function() {
			var button = $(this);
			button.button('loading');
			
			if ($('#del').is(":checked") == true) {
				if(confirm("确定删除原有数据吗?")){
					restInsert(url + "/imported", {del : 1, classesId : $("#classesId").val()}, $("#form_import"), function(data) {	
						button.button('reset');
						if (data.success) {
							alert("成功导入" + data.message + "条记录");
							hideDialog($("#dialog_import"));
							selectCount();
						} else {
							alert("导入失败\n\n" + data.message);
						}
					});
				} else {
					button.button('reset');
				}
			} else {
				restInsert(url + "/imported", {del : 0, classesId : $("#classesId").val()}, $("#form_import"), function(data) {	
					button.button('reset');
					if (data.success) {
						alert("成功导入" + data.message + "条记录");
						hideDialog($("#dialog_import"));
						selectCount();
					} else {
						alert("导入失败\n\n" + data.message);
					}
				});
			}
		});

	$("#submit").click(function() {
		var button = $(this);
		validate($("#form"), function() {
			button.button('loading');
			if ($("#id").val() == "") {
				restInsert(url, {
					classesId : $("#classesId").val()
				}, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					selectCount();

				});
			} else {
				restUpdate(url + "/" + $("#id").val(), {
					classesId : $("#classesId").val()
				}, $("#form"), function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
					selectCount();
				});
			}
		});

	});
});

function create() {
	showDialog($("#dialog"));
	$("#picture_show").hide();
	$("#picture_delete").hide();
	$("#form")[0].reset();
	$('#id').val("");
	$("#defaultc").prop("checked", true);
}

function selectCount() {
	restSelect(url + "/count", {
		"classesId" : $("#classesId").val()
	}, function(count) {
		refreshPagination($("#pagination"), count, rows, select);
	});
}
function select(page) {
	restSelect(url, {
		"classesId" : $("#classesId").val(),
		"page" : page,
		"rows" : rows
	}, function(json) {
		refreshTable($("#table"), json, [ {
			name : '修改',
			func : "update"
		} ]);
	});
}

function update(id) {
	showDialog($("#dialog"));

	restGet(url + "/" + id, null, $('#form'),
		function(data) {
			$("#picture_show").attr("src",
					"../../files/" + data.schoolId + "/curriculum/" + data.id + ".png?" + data.updateTime);
			$("#picture_show").show();
			$("#picture_delete").show();
			
			$("#defaultc").prop("checked", data.defaultc);
		});
}

// 课表类型
function curType(value, row, index) {
	if (value == 0) {
		return "正常课表";
	} else if (value == 1) {
		return "单周课表";
	} else {
		return "双周课表";
	}
}
