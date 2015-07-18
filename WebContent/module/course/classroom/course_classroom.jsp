<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../header_dialog.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="course_my.th2" bundle="${course}" />
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
					<button type="button" class="btn btn-default" id="import">
						<fmt:message key="import" />
					</button>
					<button type="button" class="btn btn-default" id="export">
						<fmt:message key="export" />
					</button>
				</div>
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin: 0px;"></ul>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
				<th style="width: 50px;"><fmt:message key="course_my.th2.view" bundle="${course}" /></th>
				<th style="width: 50px;" formatter="formatDownload"><fmt:message key="course_my.th2.down" bundle="${course}" /></th>
				<th name="name"><fmt:message key="course_my.th2.name" bundle="${course}" /></th>
				<th name="size"><fmt:message key="course_my.th2.size" bundle="${course}" /></th>
				<th name="createTeacherName"><fmt:message key="course_my.th2.uploadName" bundle="${course}" /></th>
				<th name="createTime" type="timestamp"><fmt:message key="course_my.th2.createTime" bundle="${course}" /></th>
				<th name="description" style="width: 150px;"><fmt:message key="course_my.th2.description" bundle="${course}" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><fmt:message key="course_my.th2" bundle="${course}" /></h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="remark">
							<fmt:message key="course_my.th2.remark" bundle="${course}" />
						</label>
						<textarea class="form-control" id="remark" name="remark" style="width: 100%;"></textarea>
					</div>
					<div class="form-group">
						<label for="fileDiv">
							<fmt:message key="course_my.th2.chooseFile" bundle="${course}" />
						</label> <input type="file" id="fileDiv" name="fileDiv" required="true" />
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

<div class="modal fade" id="dialog_import" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<fmt:message key="import" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form_import" id="form_import" method="post">
					<div class="form-group">
						<label><fmt:message key="import.step1" /></label>
						<button type="button" class="btn btn-default" id="export_import">
							<fmt:message key="export" />
						</button>
						<p class="help-block">
							<fmt:message key="import.step1.description" />
						</p>
					</div>

					<div class="form-group">
						<label for="import"><fmt:message key="import.step2" /></label> <input type="file" id="import" name="import" />
						<p class="help-block">
							<fmt:message key="import.step2.description" />
						</p>
					</div>

					<div class="form-group">
						<label style="color: red;"><fmt:message key="import.clear" /></label> <input type="checkbox" id="del" value="1"
							onclick="fnClick('del')" />
					</div>
				</form>
				<table id="import_table" class="table table-striped table-bordered" cellspacing="0" width="100%">
					<thead>
						<tr>
							<th name="name" width="100px"><fmt:message key="import.field.name" /></th>
							<th name="type" width="90px"><fmt:message key="import.field.type" /></th>
							<th name="value"><fmt:message key="import.field.description" /></th>
						</tr>
					</thead>
					<tbody id="tBody" />
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="submit_import" data-loading-text="<fmt:message key="importing" />">
					<fmt:message key="import" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var url = "../../../rest/classroom";
	var rows = 10;
	var richedit;
	var classesId = <%=request.getParameter("classesId")%>;
	var courseId = <%=request.getParameter("courseId")%>;
	
	$(document).ready(function() {
	
		initRowCheck($("#table"));
		richedit = initRichedit("remark");
	
		$("#select").click(function() {
			selectCount();
		});
		
		$("#export").click(function() {
			window.location = url + "/export?classesId=" + classesId + "&courseId=" + courseId;
		});
		
		$("#export_import").click(function() {
			window.location = url + "/export?classesId=" + classesId + "&courseId=" + courseId;
		});
		
		$("#import").click(function() {
			showDialog($("#dialog_import"));
			$("#del").prop("checked", true);
		});
		
		$.getJSON(url + "/rule", function(data) {
			importTable($("#import_table"), data, null, false, 3);
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
		
		//提交导入信息
		$("#submit_import").click(function() {
			var button = $(this);
			button.button('loading');
			
			if ($('#del').is(":checked") == true) {
				if (confirm("<fmt:message key="import.clear.confirm" />")) {
					restInsert(url + "/imported", {del : 1, classesId : classesId, courseId : courseId}, $("#form_import"), function(data) {	
						button.button('reset');
						if (data.success) {
							alert("<fmt:message key="import.success.0" />" + data.message
									+ "<fmt:message key="import.success.1" />");
							hideDialog($("#dialog_import"));
							selectCount();
						} else {
							alert("<fmt:message key="import.failure" />\n\n" + data.message);
						}
					});
				} else {
					button.button('reset');
				}
			} else {
				restInsert(url + "/imported", {del : 0, classesId : classesId, courseId : courseId}, $("#form_import"), function(data) {	
					button.button('reset');
					if (data.success) {
						alert("<fmt:message key="import.success.0" />" + data.message
								+ "<fmt:message key="import.success.1" />");
						hideDialog($("#dialog_import"));
						selectCount();
					} else {
						alert("<fmt:message key="import.failure" />\n\n" + data.message);
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
						classesId : classesId,
						courseId : courseId
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
		restGet(url + "/" + id, null, null, function(data) {
			window.location = "../../../files/" + data.schoolId + "/course/classroom/" + id + "/" + data.name;
		});
	}
	
	function selectCount() {
		restSelect(url + "/count", {
			"classesId" : classesId,
			"courseId" : courseId
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {
			"classesId" : classesId,
			"courseId" : courseId,
			"page" : page,
			"rows" : rows
		}, function(json) {
			refreshTable($("#table"), json, [ {
				name : '<fmt:message key="course_my.th2.view" bundle="${course}" />',
				func : "update"
			}, {
				name : '<fmt:message key="course_my.th2.down" bundle="${course}" />',
				func : "formatDownload"
			} ]);
		});
	}

</script>
<%@ include file="../../../footer.jsp" %>