<%@page import="java.util.Locale"%>
<%@page import="java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>
<%
	String type = request.getParameter("type");
	if (type == null || "".equals(type)) {
		type = "1";
	}

	ResourceBundle bundle = ResourceBundle.getBundle("cn.com.weixunyun.child.module.news.news", Locale.CHINA);
	
	String title = null;
	if ("3".equals(type)) {
		title = "　";
	} else if (Integer.parseInt(type) < 16) {
		title = bundle.getString("news" + type);
	} else {
		title = type;
	}
%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title"><%=title%></h3>
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
				<th name="title"><fmt:message key="news.title" bundle="${news}" /></th>
				<th name="descriptionSummary"><fmt:message key="news.descriptionSummary" bundle="${news}" /></th>
				<th name="updateTime" style="width: 150px;" type="timestamp"><fmt:message key="news.updateTime"
						bundle="${news}" /></th>
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
				<h4 class="modal-title" id="title"><%=title%></h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" /> <input type="hidden" name="type" id="type" value="<%=type%>" />
					<div class="form-group">
						<label for="title"><fmt:message key="news.title" bundle="${news}" /></label> <input type="text"
							class="form-control" id="title" name="title" required="true" />
					</div>

					<div class="form-group" style="display: none;">
						<label for="titleShort"><fmt:message key="news.titleShort" bundle="${news}" /></label> <input type="text"
							class="form-control" id="titleShort" name="titleShort" />
					</div>

					<div class="form-group">
						<label for="descriptionSummary"><fmt:message key="news.descriptionSummary" bundle="${news}" /></label> <input
							type="text" class="form-control" id="descriptionSummary" name="descriptionSummary" />
					</div>
					<div class="form-group">
						<label for="description"><fmt:message key="news.description" bundle="${news}" /></label>
						<textarea class="form-control" id="description" name="description" style="width: 100%;"></textarea>
					</div>
					<div class="form-group">
						<label for="picture"><fmt:message key="news.image" bundle="${news}" /></label> <input type="file"
							id="picture" name="picture" /> <img style="width: 200px; height: 200px;" class="img-rounded" id="picture_show" />
						<span id="picture_delete" style="cursor: pointer;"><fmt:message key="news.image.remove" bundle="${news}" /></span>
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
	var title = "";
	var url = "../../rest/news";
	var rows = 10;
	var richedit;
	var lastErrors;

	$(document).ready(
			function() {
				title = $("#title").html();
				initRowCheck($("#table"));
				richedit = initRichedit("description");

				$.getJSON(url + "/rule", function(data) {
					importTable($("#import_table"), data, null, false, 2);
				});

				$("#export").click(function() {
					window.location = url + "/export?type=" + $("#type").val() + "&keyword=" + $("#keyword").val();
				});

				$("#export_import").click(function() {
					window.location = url + "/export?type=" + $("#type").val();
				});

				$("#import").click(function() {
					showDialog($("#dialog_import"));
					$("#del").prop("checked", true);
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

				//提交导入信息
				$("#submit_import").click(
						function() {
							var button = $(this);
							button.button('loading');

							/*if ($('#import').val() == "") {
								alert("请选择导入文件");
								button.button('reset');
								return ;
							}*/

							if ($('#del').is(":checked") == true) {
								if (confirm("<fmt:message key="import.clear.confirm" />")) {
									restInsert(url + "/imported", {
										del : 1,
										type : $("#type").val()
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
									del : 0,
									type : $("#type").val()
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
							restUpdate(url + "?id=" + $("#id").val(), null, $("#form"), function(data) {
								button.button('reset');
								hideDialog($('#dialog'));
								selectCount();
							});
						}
					});

				});

				selectCount();

				$("#picture_delete").click(function() {
					if (confirm("<fmt:message key="remove.confirm"  />")) {
						restDelete(url + "/" + $("#id").val() + "/image", function() {
							$("#picture_show").attr("src", "");
						});
					}
				});

			});

	function create() {
		showDialog($("#dialog"));
		richedit.html("");
		$("#picture_show").hide();
		$("#picture_delete").hide();
		$("#form")[0].reset();
		$("#id").val("");
	}

	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			richedit.html(data.description);
			$("#picture_show").attr("src",
					"../../files/" + data.schoolId + "/news/" + data.id + ".png?" + data.updateTime);
			$("#picture_show").show();
			$("#picture_delete").show();
			if (data.up) {
				$("#up").attr("checked", "checked");
				$("#up").val("1");
			}

			if (data.comment) {
				$("#comment").attr("checked", "checked");
				$("#comment").val("1");
			}
		});
	}

	function selectCount() {
		restSelect(url + "/count", {
			"type" : $("#type").val(),
			"keyword" : $("#keyword").val()
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {
			"type" : $("#type").val(),
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

	function fnClick(str) {
		if ($('#' + str).is(":checked") == true) {
			$('#' + str).val("1");
		} else {
			$('#' + str).val("2");
		}
	}
</script>

<%@ include file="../../footer.jsp"%>