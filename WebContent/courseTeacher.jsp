<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header_dialog.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="teacher.manage" />
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
				<th name="classesName" style="width: 100px;">
					<fmt:message key="classesName" />
				</th>
				<th name="courseName" style="width: 100px;"><fmt:message key="courseName" /></th>
				<th name="teacherName">
					<fmt:message key="classes.teacher" />
				</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
</div>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<fmt:message key="teacher.manage" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<div class="form-group">
						<label for="courseId">
							<fmt:message key="course" />
							<fmt:message key="required" />
						</label> <select class="combobox form-control" id="courseId" name="courseId"
							required="true">
							<option></option>
						</select>
					</div>
					<div class="form-group">
						<label for="teacherId">
							<fmt:message key="classes.teacher" />
							<fmt:message key="required" />
						</label> <select class="combobox form-control" id="teacherId" name="teacherId"
							required="true">
							<option></option>
						</select>
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
	var url = "rest/classesTeacher";
	var action;
	var rows = 10;
	var classesId = <%=request.getParameter("classesId")%>
	
	$(document).ready(function() {

		initRowCheck($("#table"));

		selectCount();
		getTeacherData();
		
		$("#export").click(function() {
			window.location = url + "/export?classesId=" + classesId;
		});
		
		$("#import").click(function() {
			showDialog($("#dialog_import"));
			$("#del").prop("checked", true);
		});

		//删除数据
		$("#remove").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				$.each($("input[name=classesId]:checked"), function(i, n) {
					restDelete(url + "/" + $(n).val(), function() {
						selectCount();
					});
				});

			}
		});

		//查询按钮
		$("#select").click(function() {
			selectCount();
		});
		
		//加载导入时的字段约束信息
		$.getJSON("rest/classesTeacher/rule", function(data) {
			importTable($("#import_table"), data, null, false, null);
		});
		
		$("#submit_import").click(function() {
			var button = $(this);
			button.button('loading');
			if ($('#del').is(":checked") == true) {
				if (confirm("<fmt:message key="import.clear.confirm" />")) {
					restInsert(url + "/imported", {classesId : classesId, del : 1}, $("#form_import"), function(data) {	
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
				restInsert(url + "/imported", {classesId : classesId, del : 0}, $("#form_import"), function(data) {	
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

		//提交按钮
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');

				restInsert(url, {
					classesId : classesId
				}, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					selectCount();

				});
			});

		});
	});

	//加载课程下拉框数据
	function getSelectData() {
		$.getJSON(url + "/residue?classesId=" + classesId, function(data) {
			$("#courseId").append("<option value=''>-- <fmt:message key="option" /> --</option>");
			for (var i = 0; i < data.length; i++) {
				$("#courseId").append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
			}
		});
	}

	//加载任课教师下拉框数据
	function getTeacherData() {
		$.getJSON("rest/teacher/classes", function(data) {
			for (var i = 0; i < data.length; i++) {
				$('#teacherId').append("<option value='"+ data[i].id + "'>" + data[i].name + "</option>");
			}
		});
	}

	function create() {
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$('#courseId').html("");
		getSelectData();
	}

	function selectCount() {
		restSelect(url + "/count", {
			"keyword" : $("#keyword").val(),
			"classesId" : classesId
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {
			"keyword" : $("#keyword").val(),
			"classesId" : classesId,
			"page" : page,
			"rows" : rows
		}, function(json) {
			refreshTables($("#table"), json);
		});
	}

	function refreshTables(table, json, controls) {
		var heads = new Array();

		var thead = $(">thead", table);
		$.each($("th", thead), function(i, n) {
			heads[i] = $(n);
		});

		var tbody = $(">tbody", table);
		tbody.empty();

		$
				.each(
						json,
						function(i, n) {
							var tr = $("<tr></tr>");
							tbody.append(tr);

							$
									.each(
											heads,
											function(j, o) {
												if (j == 0) {
													var td = $("<td></td>");
													td
															.html('<input type="checkbox" name="classesId" id="classesId" class="row" value="' + n.classesId + "," + n.courseId + '"/>');
													tr.append(td);
												} else if (controls != null
														&& j < (controls.length == 1 ? 2 : (controls.length + 1)
																/ controls.length)) {
													var td;
													$.each(controls, function(i, c) {

														td = $("<td></td>");
														if (i > 0) {
															td.append("&nbsp;");
														}
														td.append('<a style="cursor:pointer;" href="javascript:'
																+ c.func + '(' + n.id + ');">' + c.name + '</a>');
														tr.append(td);
													});
												} else if (controls == null ? j > 0 : j > controls.length) {
													if (o.attr("type") == "timestamp") {
														var d = new Date();
														d.setTime(eval("n." + o.attr("name")));

														var td = $("<td></td>");
														td.attr("align", o.attr("align"));
														td.text(d.format("yyyy-MM-dd hh:mm"));

														tr.append(td);

													} else {
														var td = $("<td></td>");
														td.attr("align", o.attr("align"));
														td.text(eval("n." + o.attr("name")));

														tr.append(td);
													}
												}

											});
						});

	}

	function back() {
		menu("classes.jsp");
	}
</script>
<%@ include file="footer.jsp"%>