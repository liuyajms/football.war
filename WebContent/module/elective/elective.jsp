<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="elective" />
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
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered"
		cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 10px;"><input type="checkbox" class="rows" /></th>
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th style="width: 100px;"><fmt:message key="elective.th3"
						bundle="${elective}" /></th>
				<th name="name"><fmt:message key="elective.name"
						bundle="${elective}" /></th>
				<th name="teacherName"><fmt:message key="elective.teacherName"
						bundle="${elective}" /></th>
				<th name="date"><fmt:message key="elective.date"
						bundle="${elective}" /></th>
				<th name="num" width="130px;"><fmt:message key="elective.num"
						bundle="${elective}" /></th>
				<th name="choosedCount" width="130px;"><fmt:message
						key="elective.choosed" bundle="${elective}" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog"
	aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<fmt:message key="elective" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />

					<div class="form-group">
						<label for="name"><fmt:message key="elective.name"
								bundle="${elective}" /></label> <input type="text" class="form-control"
							id="name" name="name" required="true" />
					</div>

					<div class="form-group">
						<label for="teacherId"> <fmt:message
								key="elective.teacherName" bundle="${elective}" /> <fmt:message
								key="elective.required" bundle="${elective}" />
						</label> <select class="combobox form-control" id="teacherId"
							name="teacherId" required="true">
							<option></option>
						</select>
					</div>

					<div class="form-group">
						<label for="name"> <fmt:message key="elective.date" bundle="${elective}" />
						</label> <input type="text" class="form-control" id="date" name="date"
							required="true" />
					</div>

					<div class="form-group">
						<label for="name"> <fmt:message key="elective.num"
								bundle="${elective}" />
						</label> <input type="text" class="form-control" id="num" name="num"
							required="true" />
					</div>

					<div class="form-group">
						<label for="remark"> <fmt:message key="elective.remark"
								bundle="${elective}" />
						</label>
						<textarea type="text" class="form-control" id="remark"
							name="remark" style="width: 100%"></textarea>
					</div>

					<div class="form-group">
						<div class="col-sm-12">
							<label><fmt:message key="elective.grade"
									bundle="${elective}" /></label>
							<div id="gradeDiv"></div>
							<input type="hidden" id="grade" name="grade" />
						</div>
					</div>

				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="submit"
					data-loading-text="<fmt:message key="submiting" />">
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
						<label for="import"><fmt:message key="import.step2" /></label> <input
							type="file" id="import" name="import" />
						<p class="help-block">
							<fmt:message key="import.step2.description" />
						</p>
					</div>

					<div class="form-group">
						<label style="color: red;"><fmt:message key="import.clear" /></label>
						<input type="checkbox" id="del" value="1" onclick="fnClick('del')" />
					</div>
				</form>
				<table id="import_table" class="table table-striped table-bordered"
					cellspacing="0" width="100%">
					<thead>
						<tr>
							<th name="name" width="100px"><fmt:message
									key="import.field.name" /></th>
							<th name="type" width="90px"><fmt:message
									key="import.field.type" /></th>
							<th name="value"><fmt:message key="import.field.description" /></th>
						</tr>
					</thead>
					<tbody id="tBody" />
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="submit_import"
					data-loading-text="<fmt:message key="importing" />">
					<fmt:message key="import" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	
	var url = "../../rest/elective";
	var action;
	var rows = 10;
	var richedit;
	$(document).ready(function(){
		initRowCheck($("#table"));
		richedit = initRichedit("remark");
		
		$.getJSON(url + "/rule", function(data) {
			importTable($("#import_table"), data, null, false, 2);
		});
		
		$("#export").click(function() {
			window.location = url + "/export";
		});
		
		$("#export_import").click(function() {
			window.location = url + "/export";
		});
		
		$("#import").click(function() {
			showDialog($("#dialog_import"));
			$("#del").prop("checked", true);
		});
		
		selectCount();
		getTeacherData();
		
		$("#select").click(function() {
			selectCount();
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
		
		//提交按钮
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');
				richedit.sync();
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

		});
	});
	
	function selectCount() {
		restSelect(url + "/list/count", {}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url + "/list", {"page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, [{name: '<fmt:message key="update" />', func: "update"}, {name: '<fmt:message key="elective.score" bundle="${elective}" />', func: "score"}]);
		});
	}
	
	function create() {
		showDialog($("#dialog"));
		richedit.html("");
		$("#form")[0].reset();
		$("#id").val("");
		for (var i = 0; i < 8; i++) {
			$("#grade" + i).prop("checked", false);
		}
	}
	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			richedit.html(data.description);
			var grade = data.grade;
			for (var i = 0; i < 8; i++) {
				$("#grade" + i).prop("checked", (grade & (1 << i)));
			}
		});
		
	}
	
	function score(id) {
		//menu('electiveStudent.jsp?electiveId=' + id);
		window.open('electiveStudent.jsp?electiveId=' + id,"_self");
	}
	
	//加载任课教师下拉框数据
	function getTeacherData(){
		$.getJSON("../../rest/teacher/classes", function(data){
			for(var i = 0; i < data.length; i ++){
				$('#teacherId').append("<option value='"+ data[i].id + "'>" + data[i].name + "</option>");
			}
		});
	}
	
	function fnClick(str){
		if($("#" + str).is(":checked") == true){
			$("#" + str).val("1");
		}else{
			$("#" + str).val("2");
		}
	}
</script>

<%@ include file="../../footer.jsp"%>