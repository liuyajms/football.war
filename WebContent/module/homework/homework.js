var title = "作业";
var url = "../../rest/homework";
var rows = 10;
var richedit;
var lastErrors;
var classesId = <%=request.getParameter("classesId")%>;
var courseId = <%=request.getParameter("courseId")%>;

$(document).ready(function() {alert(0);
	title = $("#title").html();
	initRowCheck($("#table"));
	richedit = initRichedit("description");

	initCourseBox();
	
	$("#export").click(function() {
		window.location = url + "/export?classesId=" + classesId  + "&courseId=" + courseId + "&keyword=" + $("#keyword").val();
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
	
	$("#submit").click(function() {
		var button = $(this);
		richedit.sync();
		validate($("#form"), function() {
			button.button('loading');
			if ($("#id").val() == "") {
				restInsert(url, null, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					selectCount();
				});
			} else {
				restUpdate(url + "/" + $("#id").val(), null, $("#form"), function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
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
}

function update(id) {
	showDialog($("#dialog"));

	restGet(url + "/" + id, null, $('#form'), function(data) {
		richedit.html(data.description);
	});
}

function selectCount() {
	restSelect(url + "/count", {
		"classesId" : classesId,
		"courseId" : courseId,
		"keyword" : $("#keyword").val()
	}, function(count) {
		refreshPagination($("#pagination"), count, rows, select);
	});
}
function select(page) {
	restSelect(url, {
		"classesId" : classesId,
		"courseId" : courseId,
		"keyword" : $("#keyword").val(),
		"page" : page,
		"rows" : rows
	}, function(json) {
		refreshTable($("#table"), json, [ {
			name : '修改',
			func : "update"
		} ]);
	});
}


//加载任课教师下拉框数据
function initCourseBox(){
	$.getJSON("../../rest/homework/course", function(data){
		for(var i = 0; i < data.length; i ++){
			$('#courseId').append("<option value='"+ data[i].id + "'>" + data[i].name + "</option>");
		}
	});
}
