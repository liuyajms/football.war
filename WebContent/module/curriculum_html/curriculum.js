var url = "../../rest/curriculum";
var rows = 10;

$(document).ready(function() {

	selectCount();

	$("#select").click(function() {
		selectCount();
	});

	$("#export").click(function() {
		window.location = url + "/export?classesId=" + $("#classesId").val();
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

	$("#submit").click(function() {
		var button = $(this);
		validate($("#form"), function() {
			button.button('loading');
			if ($("#id").val() == "") {
				restInsert(url, {
					remark : $("#tdId").html(),
					classesId : $("#classesId").val()
				}, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					selectCount();

				});
			} else {
				restUpdate(url + "/" + $("#id").val(), {
					remark : $("#tdId").html(),
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
	$("#form")[0].reset();
	$('#id').val("");
	$('#tdId').html("");
	$('#tdId').load("tableAdd.jsp");
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

	restGet(url + "/" + id, null, $('#form'), function(data) {
		$('#tdId').html("");
		$("#tdId").html(data.description);
		if (data.defaultc) {
			$("#defaultc").attr("checked", "checked");
			$("#defaultc").val("1");
		}

		var tabAdd = $("#tabAdd").get(0);

		$('#serId').attr("editType", "");

		// 设置表格可编辑
		EditTables(tabAdd);

		$('#tabAdd tr td').bind('mousemove', function() {
			var trSeq = $(this).parent().parent().find("tr").index($(this).parent()[0]);
			$('#trId').val(trSeq);
		});

		// 绑定右键菜单
		$('#tabAdd tr').contextMenu('myMenu1', {
			bindings : {
				'addOneUp' : function(t) {
					AddRow($('#tabAdd').get(0), parseInt($('#trId').val()), "before");
				},
				'addOneDown' : function(t) {
					AddRow($('#tabAdd').get(0), parseInt($('#trId').val()) + 1, "after");
				},
				'delete' : function(t) {
					DeleteRow($('#tabAdd').get(0), parseInt($('#trId').val()));
				}
			}

		});
	});
}

function divClick(id) {
	if (id == 1) {
		AddRow($('#tabAdd').get(0), 0);
	} else if (id == 3) {
		DeleteRow($('#tabAdd').get(0), 0)
	}

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

function fnClick() {
	if ($('#defaultc').is(":checked") == true) {
		$('#defaultc').val("1");
	} else {
		$('#defaultc').val("2");
	}
}