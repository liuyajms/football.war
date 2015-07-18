<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="security" bundle="${security}" />
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="export">
						<fmt:message key="export" />
					</button>
				</div>
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin: 0px;"></ul>
			</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered"
		cellspacing="0" width="100%">
		<thead>
			<tr>
				<th name="studentName"><fmt:message key="security.studentName" bundle="${security}" /></th>
				<th name="date"><fmt:message key="security.date" bundle="${security}" /></th>
				<th name="reachOver==true?'迟到':''">
				<fmt:message key="security.reachOver" bundle="${security}" />
				</th>
				<th name="leaveOver==true?'早退':''">
				<fmt:message key="security.leaveOver" bundle="${security}" />
				</th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<script type="text/javascript">
	var url = "../../rest/security";
	var action;
	var rows = 10;
	var richedit;
	$(document)
			.ready(
					function() {
						initRowCheck($("#table"));
						richedit = initRichedit("remark");

						$("#export").click(function() {
							window.location = url + "/export";
						});

						selectCount();
						getTeacherData();

						$("#select").click(function() {
							selectCount();
						});

						$
								.getJSON(
										"rest/global/elective.grade",
										function(data) {
											for (var i = 0; i < 6; i++) {
												$('#grade')
														.append(
																'<label class="checkbox-inline">');
												$('#grade')
														.append(
																'<input type="checkbox" name="grade'
																		+ i
																		+ '" id="grade'
																		+ i
																		+ '" value="2" onclick="fnClick(\'grade'
																		+ i
																		+ '\')" /> '
																		+ (i + 1)
																		+ '<fmt:message key="security.grade" bundle="${security}" />');
												$('#grade').append('</label>');
											}
										});

						$("#remove").click(
								function() {
									if (confirm("<fmt:message key="remove.confirm" />")) {
										$.each($("input[name=id]:checked"),
												function(i, n) {
													restDelete(url + "/"
															+ $(n).val(),
															function() {
															});
												});
										selectCount();
									}
								});

						// 提交按钮
						$("#submit")
								.click(
										function() {
											var button = $(this);
											richedit.sync();
											validate(
													$("#form"),
													function() {
														button
																.button('loading');
														if ($("#id").val() == "") {
															restInsert(
																	url,
																	null,
																	$("#form"),
																	function(
																			data) {
																		button
																				.button('reset');
																		hideDialog($("#dialog"));
																		selectCount();
																	});
														} else {
															restUpdate(
																	url
																			+ "/"
																			+ $(
																					"#id")
																					.val(),
																	null,
																	$("#form"),
																	function(
																			data) {
																		button
																				.button('reset');
																		hideDialog($('#dialog'));
																		selectCount();
																	});
														}
													});

										});
					});

	function selectCount() {
		restSelect(url + "/classes/count", {}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url + "/classes", {
			"page" : page,
			"rows" : rows
		}, function(json) {
			refreshTable($("#table"), json, [], null, false);
		});
	}

	function create() {
		showDialog($("#dialog"));
		richedit.html("");
		$("#form")[0].reset();
		$("#id").val("");
	}
	function update(id) {
		showDialog($("#dialog"));
		$("#form")[0].reset();

		restGet(url + "/" + id, null, $('#form'), function(data) {
			richedit.html(data.description);
			if (data.grade0) {
				$("#grade0").attr("checked", "checked");
				$("#grade0").val("1");
			}
			if (data.grade1) {
				$("#grade1").attr("checked", "checked");
				$("#grade1").val("1");
			}
			if (data.grade2) {
				$("#grade2").attr("checked", "checked");
				$("#grade2").val("1");
			}
			if (data.grade3) {
				$("#grade3").attr("checked", "checked");
				$("#grade3").val("1");
			}
			if (data.grade4) {
				$("#grade4").attr("checked", "checked");
				$("#grade4").val("1");
			}
			if (data.grade5) {
				$("#grade5").attr("checked", "checked");
				$("#grade5").val("1");
			}
			if (data.grade6) {
				$("#grade6").attr("checked", "checked");
				$("#grade6").val("1");
			}
			if (data.grade7) {
				$("#grade7").attr("checked", "checked");
				$("#grade7").val("1");
			}
		});

	}

	function score(id) {
		menu('electiveStudent.jsp?electiveId=' + id);
	}

	// 加载任课教师下拉框数据
	function getTeacherData() {
		$.getJSON("rest/teacher/classes", function(data) {
			for (var i = 0; i < data.length; i++) {
				$('#teacherId').append(
						"<option value='" + data[i].id + "'>" + data[i].name
								+ "</option>");
			}
		});
	}

	function fnClick(str) {
		if ($("#" + str).is(":checked") == true) {
			$("#" + str).val("1");
		} else {
			$("#" + str).val("2");
		}
	}
</script>

<%@ include file="../../footer.jsp"%>