<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="broadcast.grade" bundle="${broadcast}" />
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
				<th style="width: 80px;"><fmt:message key="broadcast.remark" bundle="${broadcast}" /></th>
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
					<fmt:message key="broadcast.grade" bundle="${broadcast}" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />

					<div class="form-group">
						<label><fmt:message key="broadcast.grade" bundle="${broadcast}" /></label>
						<div id="gradeDiv"></div>
						<input type="hidden" id="grade" name="grade" />
					</div>

					<div class="form-group">
						<label for="title">
							<fmt:message key="broadcast.title" bundle="${broadcast}" />
						</label> <input type="text" class="form-control" id="title" name="title" required="true" />
					</div>

					<div class="form-group">
						<label for="description">
							<fmt:message key="broadcast.description" bundle="${broadcast}" />
						</label>
						<textarea class="form-control" id="description" name="description" style="width: 100%; height: 160px;"></textarea>
					</div>

					<div class="form-group">
						<label for="pic">
							<fmt:message key="broadcast.pic" bundle="${broadcast}" />
						</label> <input type="file" id="pic" name="pic" multiple="true" /> <img
							style="width: 200px; height: 200px;" class="img-rounded" id="picture_show" /> <span id="picture_delete"
							style="cursor: pointer;"><fmt:message key="broadcast.delPic" bundle="${broadcast}" /></span>
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
		getData();

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
		
		$("#export").click(function() {
			window.location = url + "/grade/export?keyword=" + $("#keyword").val();
		});
		
		$("#import").click(function() {
			showDialog($("#dialog_import"));
			$("#del").prop("checked", true);
		});
		
		$("#export_import").click(function() {
			window.location = url + "/grade/export";
		});
		
		//提交导入信息
		$("#submit_import").click(function() {
			var button = $(this);
			button.button('loading');
			
			if ($('#del').is(":checked") == true) {
				if (confirm("<fmt:message key="import.clear.confirm" />")) {
					restInsert(url + "/imported", {del : 1}, $("#form_import"), function(data) {	
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
				restInsert(url + "/imported", {del : 0}, $("#form_import"), function(data) {	
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
			if (!($("#description").val == "" && $("#pic").val() == "" && $("#voice").val() == "")) {
				validate($("#form"), function() {
					button.button('loading');
					var grade = 0;
					for (var i = 0; i < 8; i++) {
						if ($("#grade" + i).is(":checked")) {
							grade += (1 << i);
						}
					}
					$("#grade").val(grade);
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
			} else {
				alert('<fmt:message key="broadcast.message" bundle="${broadcast}" />');
			}

		});
	});

	function getData() {
		$.getJSON(url + "/rulesGrade", function(data) {
			importTable($("#import_table"), data, null, false, 2);
		});
	
		$.getJSON(
						"../../rest/global/elective.grade",
						function(data) {
							var grade = parseInt(data.value);
							var gradeDiv = $("#gradeDiv");
							for (var i = 0; i < grade; i++) {
								gradeDiv.append('<label class="checkbox-inline">');
								gradeDiv.append('<input type="checkbox" name="grade' + i + '" id="grade' + i + '" value="1" /> ' + (i + 1) + '年级');
								gradeDiv.append('</label>');
							}
						});
	}

	function selectCount() {
		restSelect(url + "/grade/count", {
			"keyword" : $("#keyword").val()
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url + "/grade", {
			"keyword" : $("#keyword").val(),
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
		for (var i = 0; i < 8; i++) {
			$("#grade" + i).prop("checked", false);
		}
		$("#picture_show").hide();
		$("#picture_delete").hide();
		$("#voice_show").hide();
		$("#voice_delete").hide();
	}

	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			var grade = data.grade;
			for (var i = 0; i < 8; i++) {
				$("#grade" + i).prop("checked", (grade & (1 << i)));
			}
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