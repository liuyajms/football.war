<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="notice"  />
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
				<th name="description"><fmt:message key="notice.description" bundle="${notice}" /></th>
				<th name="createUserName"><fmt:message key="notice.createUserName" bundle="${notice}" /></th>
				<th name="createTime" style="width: 150px;" align="center" type="timestamp"><fmt:message
						key="notice.createTime" bundle="${notice}" /></th>
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
					<fmt:message key="notice" bundle="${notice}" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="description"><fmt:message key="notice.description" bundle="${notice}" /></label>
						<textarea class="form-control" id="description" name="description" style="width: 100%; height: 160px;"
							required="true"></textarea>
					</div>

					<div class="form-group">
						<label for="pic"><fmt:message key="notice.image" bundle="${notice}" /></label> <input type="file" id="pic"
							name="pic" multiple="true" /> <img style="width: 200px; height: 200px;" class="img-rounded" id="picture_show" />
						<span id="picture_delete" style="cursor: pointer;"><fmt:message key="notice.image.remove"
								bundle="${notice}" /></span>
					</div>

					<div class="checkbox">
						<label for="pushTeacher"><fmt:message key="notice.pushTeacher" bundle="${notice}" /></label> <input
							type="checkbox" name="pushTeacher" id="pushTeacher" value="2" onclick="fnClick('pushTeacher')" />
					</div>

					<div class="checkbox">
						<label for="pushParents"><fmt:message key="notice.pushParents" bundle="${notice}" /></label> <input
							type="checkbox" name="pushParents" id="pushParents" value="2" onclick="fnClick('pushParents')" />
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

<script>
	var url = "../../rest/notice";
	var rows = 10;

	$(document).ready(
			function() {
				initRowCheck($("#table"));
				selectCount();

				$.getJSON(url + "/rule", function(data) {
					importTable($("#import_table"), data, null, false, 2);
				});

				$("#remove").click(function() {
					if (confirm("<fmt:message key="remove.confirm" />")) {
						$.each($("input[name=id]:checked"), function(i, n) {
							restDelete(url + "/" + $(n).val(), function() {
								selectCount();
							});
						});
					}
				});

				$("#select").click(function() {
					selectCount();
				});

				$("#import").click(function() {
					showDialog($("#dialog_import"));
					$("#del").prop("checked", true);
				});

				$("#export").click(function() {
					window.location = url + "/export?keyword=" + $("#keyword").val();
				});

				$("#export_import").click(function() {
					window.location = url + "/export";
				});

				//提交导入信息
				$("#submit_import").click(
						function() {
							var button = $(this);
							button.button('loading');

							if ($('#del').is(":checked") == true) {
								if (confirm("<fmt:message key="import.clear.confirm" />")) {
									restInsert(url + "/imported", {
										del : 1
									}, $("#form_import"), function(data) {
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
								restInsert(url + "/imported", {
									del : 0
								}, $("#form_import"), function(data) {
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
					if (!($("#pushTeacher").val() == "2" && $("#pushParents").val() == "2")) {
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
					} else {
						alert('<fmt:message key="notice.push.validate" />');
					}
				});

				$("#picture_delete").click(function() {
					if (confirm("<fmt:message key="remove.confirm" />")) {
						restDelete(url + "/" + $("#id").val() + "/image", function() {
							$("#picture_show").attr("src", "");
						});
					}
				});

			});

	function selectCount() {
		restSelect(url + "/count", {
			"keyword" : $("#keyword").val()
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {
			"keyword" : $("#keyword").val(),
			"page" : page,
			"rows" : rows
		}, function(json) {
			refreshTable($("#table"), json, [ {
				name : '<fmt:message key="update" />',
				func : "update"
			} ]);
		});
	}

	function viewComment(id) {
		menu("weiboComment.jsp?id=" + id);
	}

	function create() {
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#id").val("");
		$("#pushTeacher").attr("checked", "checked");
		$("#pushTeacher").val("1");
		$("#pushParents").attr("checked", "checked");
		$("#pushParents").val("1");

		$("#picture_show").hide();
		$("#picture_delete").hide();
	}

	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			$("#picture_show").attr("src",
					"../../files/" + data.schoolId + "/notice/" + data.id + "/0.png?" + data.updateTime);
			$("#picture_show").show();
			$("#picture_delete").show();
			if (data.pushTeacher) {
				$("#pushTeacher").prop("checked", true);
				$("#pushTeacher").val("1");
			}
			if (data.pushParents) {
				$("#pushParents").prop("checked", true);
				$("#pushParents").val("1");
			}

		});
	}

	function fnClick(str) {
		if ($('#' + str).is(":checked") == true) {
			$('#' + str).val("1");
		} else {
			$('#' + str).val("2");
		}
	}
</script>

<%@ include file="../../footer.jsp"%>