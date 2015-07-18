<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header_dialog.jsp" %>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">作业</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
				</div>
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="export">导出</button>
				</div>
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin: 0px;"></ul>
			</div>
			<div class="col-md-4">
				<div class="input-group">
				</div>
			</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
				<th style="width: 50px;">操作</th>
				<th name="studentName">学生</th>
				<th name="checked==true?'是':''">是否检查</th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<script type="text/javascript">
	var title = "作业";
	var url = "../../rest/homeworkCheck";
	var rows = 10;
	var richedit;
	var lastErrors;
	var classesId = <%=request.getParameter("classesId")%>;
	var homeworkId = <%=request.getParameter("homeworkId")%>;

$(document).ready(function() {
	title = $("#title").html();
	initRowCheck($("#table"));

	$("#export").click(function() {
		window.location = url + "/teacher/export?classesId=" + classesId  + "&homeworkId=" + homeworkId;
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
	
	selectCount();

});

function create() {
	showDialog($("#dialog"));
	$("#form")[0].reset();
	$("#id").val("");
}

function update(id) {
	showDialog($("#dialog"));

	restGet(url + "/" + id, null, $('#form'), function(data) {
		
	});
}

function selectCount() {
	restSelect(url + "/teacher/count", {
		"classesId" : classesId,
		"homeworkId" : homeworkId,
		"date" : '20140926'
	}, function(count) {
		refreshPagination($("#pagination"), count, rows, select);
	});
}
function select(page) {
	restSelect(url + "/teacher", {
		"classesId" : classesId,
		"homeworkId" : homeworkId,
		"date" : '20140926',
		"page" : page,
		"rows" : rows
	}, function(json) {
		refreshTable($("#table"), json, [ {
			name : '修改',
			func : "update"
		} ]);
	});
}

</script>

<%@ include file="../../footer.jsp"%>