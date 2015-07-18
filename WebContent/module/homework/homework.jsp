<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../header_dialog.jsp" %>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="homework" bundle="${homework}" />
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="create" onclick="create()">
						<fmt:message key="create" />
					</button>
					<button type="button" class="btn btn-default" id="remove">
						<fmt:message key="remove" />
					</button>
				</div>
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="export">
						<fmt:message key="export" />
					</button>
				</div>
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin: 0px;"></ul>
			</div>
			<div class="col-md-4">
				<div class="input-group">
					<input type="text" class="form-control" id="keyword" /> <span class="input-group-btn">
						<button class="btn btn-default" type="button" id="select">
							<fmt:message key="select" />
						</button>
					</span>
				</div>
			</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th style="width: 80px;"><fmt:message key="homework.situation" bundle="${homework}" /></th>
				<th name="courseName" style="width: 150px;"><fmt:message key="homework.courseName" bundle="${homework}" /></th>
				<th name="description"><fmt:message key="homework.description" bundle="${homework}" /></th>
				<th name="updateTime" style="width: 150px;" type="timestamp">
					<fmt:message key="homework.updateTime" bundle="${homework}" />
				</th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="title"><fmt:message key="homework.situation" bundle="${homework}" /></h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />
					
					<div class="form-group">
						<label for="description"><fmt:message key="homework.description" bundle="${homework}" /></label>
						<textarea class="form-control" id="description" name="description" style="width: 100%;"></textarea>
					</div>

				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="submit" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var title = "<fmt:message key="homework" bundle="${homework}" />";
	var url = "../../rest/homework";
	var rows = 10;
	var richedit;
	var lastErrors;
	var classesId = <%=request.getParameter("classesId")%>;
	var courseId = <%=request.getParameter("courseId")%>;

$(document).ready(function() {
	title = $("#title").html();
	initRowCheck($("#table"));
	richedit = initRichedit("description");

	$("#export").click(function() {
		window.location = url + "/teacher/export?classesId=" + classesId  + "&courseId=" + courseId + "&keyword=" + $("#keyword").val();
	});
	$("#select").click(function() {
		selectCount();
	});
	$("#remove").click(function() {
		if (confirm("<fmt:message key="remove.confirm" />")) {
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
				restInsert(url, {"classesId" : classesId, "courseId" : courseId}, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					selectCount();
				});
			} else {
				restUpdate(url + "/" + $("#id").val(), {"classesId" : classesId, "courseId" : courseId}, $("#form"), function(data) {
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

function check(id) {
	window.open('homework_check.jsp?classesId=' + classesId + '&homeworkId=' + id,"_self");
}

function selectCount() {
	restSelect(url + "/t/count", {
		"classesId" : classesId,
		"courseId" : courseId,
		"keyword" : $("#keyword").val()
	}, function(count) {
		refreshPagination($("#pagination"), count, rows, select);
	});
}
function select(page) {
	restSelect(url + "/t", {
		"classesId" : classesId,
		"courseId" : courseId,
		"keyword" : $("#keyword").val(),
		"page" : page,
		"rows" : rows
	}, function(json) {
		refreshTable($("#table"), json, [ {
			name : '<fmt:message key="update" />',
			func : "update"
		}, {
			name : '<fmt:message key="homework.description" bundle="${homework}" />',
			func : "check"
		}  ]);
	});
}

</script>

<%@ include file="../../footer.jsp"%>