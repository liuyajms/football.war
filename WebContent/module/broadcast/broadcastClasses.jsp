<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="broadcast.classes" bundle="${broadcast}" />
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
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th style="width: 80px;"><fmt:message key="broadcast.remark" bundle="${broadcast}" /></th>
				<th name="classesName" style="width:100px;">
					<fmt:message key="broadcast.classesName" bundle="${broadcast}" />
				</th>
				<th name="title" style="width:200px;">
					<fmt:message key="broadcast.title" bundle="${broadcast}" />
				</th>
				<th name="description">
					<fmt:message key="broadcast.description" bundle="${broadcast}" />
				</th>
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
				<h4 class="modal-title">
					<fmt:message key="broadcast.classes" bundle="${broadcast}" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />

					<div class="form-group">
						<label for="title">
							<fmt:message key="broadcast.title" bundle="${broadcast}" />
						</label> <input type="text" class="form-control" id="title" name="title" required="true" />
					</div>
					<div class="form-group">
						<label for="description">
							<fmt:message key="broadcast.description" bundle="${broadcast}" />
						</label>
						<textarea class="form-control" id="description" name="description" style="width: 100%; height: 160px;"
							required="true"></textarea>
					</div>

					<div class="form-group">
						<label for="pic">
							<fmt:message key="broadcast.pic" bundle="${broadcast}" />
						</label> <input type="file" id="pic" name="pic" multiple="true" />
						<img style="width:200px;height:200px;" class="img-rounded" id="picture_show" />
						<span id="picture_delete" style="cursor:pointer;">
							<fmt:message key="broadcast.delPic" bundle="${broadcast}" />
						</span>
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
	var url = "../../rest/broadcast";
	var rows = 10;

	$(document).ready(function() {
		initRowCheck($("#table"));
		selectCount();
		
		$.getJSON(url + "/rulesClasses", function(data) {
			importTable($("#import_table"), data, null, false, 2);
		});
		
		$("#export").click(function() {
			window.location = url + "/classes/export?keyword=" + $("#classesId").val();
		});
		
		$("#export_import").click(function() {
			window.location = url + "/classes/export?keyword=" + $("#classesId").val();
		});
		
		$("#import").click(function() {
			showDialog($("#dialog_import"));
			$("#del").prop("checked", true);
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

		$("#select").click(function() {
			selectCount();
		});
		
		$("#picture_delete").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				restDelete(url + "/" + $("#id").val() + "/image", function() {
					$("#picture_show").attr("src", "");
				});
			}
		});
		
		$("#voice_delete").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				restDelete(url + "/" + $("#id").val() + "/image", function() {
					$("#picture_show").attr("src", "");
				});
			}
		});
		
		//提交导入信息
		$("#submit_import").click(function() {
			var button = $(this);
			button.button('loading');
			
			if ($('#del').is(":checked") == true) {
				if (confirm("<fmt:message key="import.clear.confirm" />")) {
					restInsert(url + "/imported", {del : 1, classesId : $("#classesId").val()}, $("#form_import"), function(data) {	
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
				restInsert(url + "/imported", {del : 0, classesId : $("#classesId").val()}, $("#form_import"), function(data) {	
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
			validate($("#form"), function() {
				button.button('loading');
				if ($("#id").val() == "") {
					restInsert(url, {classesId : $("#classesId").val()}, $("#form"), function(data) {
						button.button('reset');
						hideDialog($("#dialog"));
						selectCount();

					});
				} else {
					restUpdate(url + "/" + $("#id").val(), {classesId : $("#classesId").val()}, $("#form"), function(data) {
						button.button('reset');
						hideDialog($('#dialog'));
						selectCount();
					});
				}
			});

		});
	});

	function selectCount() {
		restSelect(url + "/classes/count", {
			"keyword" : $("#classesId").val()
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url + "/classes", {
			"keyword" : $("#classesId").val(),
			"page" : page,
			"rows" : rows
		}, function(json) {
			refreshTable($("#table"), json, [ {
				name : '<fmt:message key="update" />',
				func : "update"
			}, {
				name : '<fmt:message key="broadcast.remark" bundle="${broadcast}" />',
				func : "viewComment"
			} ]);
		});
	}

	function viewComment(id) {
		window.open("broadcastSchoolComment.jsp?id=" + id,"_self");
	}

	function create() {
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#id").val("");
		$("#picture_show").hide();
		$("#picture_delete").hide();
		$("#voice_show").hide();
		$("#voice_delete").hide();
	}

	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			$("#picture_show").attr("src", "../../files/" + data.schoolId + "/broadcast/" + data.id + "/0.png?" + data.updateTime);
			$("#picture_show").show();
			$("#picture_delete").show();
			$("#voice_show").attr("src", "../../files/" + data.schoolId + "/broadcast/" + data.id + "/voice.amr?" + data.updateTime);
			$("#voice_show").show();
			$("#voice_delete").show();
			return;
		});
	}
</script>
<%@ include file="../../footer.jsp"%>