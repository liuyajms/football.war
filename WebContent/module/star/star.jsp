<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="star" bundle="${star}" />
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="create"
						onclick="create()">
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
	<table id="table" class="table table-striped table-bordered"
		cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th name="name"><fmt:message key="star.name" bundle="${star}" /></th>
				<th name="description"><fmt:message key="star.description" bundle="${star}" /></th>
				<th name="createTime" style="width: 150px;" align="center"
					type="timestamp"><fmt:message key="star.createTime" bundle="${star}" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog"
	aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title"><fmt:message key="star" bundle="${star}" /></h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post"
					enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />

					<div class="form-group">
						<label for="name"><fmt:message key="star.name" bundle="${star}" />
						</label> <input type="text"
							class="form-control" id="name" name="name" required="true" />
					</div>

					<div class="form-group">
						<label for="description"><fmt:message key="star.description" bundle="${star}" /></label>
						<textarea class="form-control" id="description" name="description"
							style="width: 100%; height:150px;"></textarea>
					</div>

					<div class="form-group">
						<label for="picture">
						<fmt:message key="star.picture" bundle="${star}" />
						</label> <input type="file" id="picture"
							name="picture" /> <img style="width: 200px; height: 200px;"
							class="img-rounded" id="picture_show" /> <span
							id="picture_delete" style="cursor: pointer;">
<fmt:message key="star.deletePicture" bundle="${star}" />
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

<div class="modal fade" id="dialog_import" tabindex="-1" role="dialog"
	aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title"><fmt:message key="star.dialog.title" bundle="${star}" /></h4>
			</div>
			<div class="modal-body">
				<form role="form_import" id="form_import" method="post">
					<div class="form-group">
						<label><fmt:message key="star.export.label" bundle="${star}" /></label>
						<button type="button" class="btn btn-default" id="export_import">
						<fmt:message key="star.export" bundle="${star}" />
						</button>
						<p class="help-block"><fmt:message key="star.export.help" bundle="${star}" /></p>
					</div>

					<div class="form-group">
						<label for="picture">
						<fmt:message key="star.import.label" bundle="${star}" />
						</label> <input type="file" id="import"
							name="import" />
						<p class="help-block"><fmt:message key="star.import.help" bundle="${star}" /></p>
					</div>

					<div class="form-group">
						<label style="color: red;">
						<fmt:message key="star.isDeleteData" bundle="${star}" />
						</label> <input type="checkbox"
							id="del" value="1" onclick="fnClick('del')" />
					</div>
				</form>
				<table id="import_table" class="table table-striped table-bordered"
					cellspacing="0" width="100%">
					<thead>
						<tr>
							<th name="name" width="100px"><fmt:message key="star.columnName" bundle="${star}" /></th>
							<th name="type" width="90px"><fmt:message key="star.columnType" bundle="${star}" /></th>
							<th name="value"><fmt:message key="star.isDeleteData" bundle="${star}" /></th>
						</tr>
					</thead>
					<tbody id="tBody" />
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="submit_import"
					data-loading-text="<fmt:message key="importing" />"><fmt:message key="import" /></button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var title = "<fmt:message key="star" bundle="${star}" />";
	var url = "../../rest/star";
	var rows = 10;
	//var richedit;
	var lastErrors;

	$(document).ready(
			function() {
				initRowCheck($("#table"));

				//richedit = initRichedit("description");

				$("#select").click(function() {
					selectCount();
				});

				$("#export").click(
						function() {
							window.location = url + "/export?classesId="
									+ $("#classesId").val();
						});

				$("#export_import").click(
						function() {
							window.location = url + "/export?classesId="
									+ $("#classesId").val();
						});

				$("#import").click(function() {
					showDialog($("#dialog_import"));
					$("#del").prop("checked", true);
				});

				$.getJSON(url + "/rule", function(data) {
					importTable($("#import_table"), data, null, false, 2);
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
							restInsert(url + "/imported", {
								del : 1,
								classesId : $("#classesId").val()
							}, $("#form_import"), function(data) {
								button.button('reset');
								if (data.success) {
									alert("<fmt:message key="import.success.0" />" + data.message + "<fmt:message key="import.success.1" />");
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
							del : 0,
							classesId : $("#classesId").val()
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
					//richedit.sync();
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
							restUpdate(url + "?id=" + $("#id").val(), {
								classesId : $("#classesId").val()
							}, $("#form"), function(data) {
								button.button('reset');
								hideDialog($('#dialog'));
								selectCount();
							});
						}
					});

				});

				selectCount();

				$("#picture_delete").click(
						function() {
							if (confirm("<fmt:message key="remove.confirm" />")) {
								restDelete(url + "/" + $("#id").val()
										+ "/image", function() {
									$("#picture_show").attr("src", "");
								});
							}
						});

			});

	function create() {
		showDialog($("#dialog"));
		//richedit.html("");
		$("#picture_show").hide();
		$("#picture_delete").hide();
		$("#form")[0].reset();
		$("#id").val("");
	}

	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			//richedit.html(data.description);
			$("#picture_show").attr(
					"src",
					"../../files/" + data.schoolId + "/star/" + data.id
							+ ".png?" + data.updateTime);
			$("#picture_show").show();
			$("#picture_delete").show();
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
				name : '<fmt:message key="update" />',
				func : "update"
			} ]);
		});
	}
</script>

<%@ include file="../../footer.jsp"%>