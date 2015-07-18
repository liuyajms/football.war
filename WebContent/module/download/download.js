var url = "../../rest/download";
var rows = 10;
var richedit;

$(document).ready(function() {

	initRowCheck($("#table"));
	richedit = initRichedit("remark");
	
	$.getJSON(url + "/rule", function(data) {
		importTable($("#import_table"), data, null, false, 2);
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

	$("#select").click(function() {
		selectCount();
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
			
			/*if ($('#import').val() == "") {
				alert("请选择导入文件");
				button.button('reset');
				return ;
			}*/
			
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
		richedit.sync();
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
			}
		});

	});

	selectCount();

});

function create() {
	showDialog($("#dialog"));
	richedit.html("");
	$("#form")[0].reset();
	$("#id").val("");
	$("#submit").show();
}

function update(id) {
	showDialog($("#dialog"));

	restGet(url + "/" + id, null, $('#form'), function(data) {
		richedit.html(data.description);
		$("#submit").hide();
	});
}

function formatDownload(id) {
	// window.location =
	// 'http://125.70.9.211:5080/index.php/site/nulllogin?userName=ym1010&password=123456';
	restGet(url + "/" + id, null, null, function(data) {
		window.location = "../../files/" + data.schoolId + "/download/" + id + "/" + data.name;
	});
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
			name : '查看',
			func : "update"
		}, {
			name : '下载',
			func : "formatDownload"
		} ]);
	});
}