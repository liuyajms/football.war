<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="classes.manage" />
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<%-- <div class="btn-group">
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
				</div> --%>
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
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th style="width: 80px;">
					<fmt:message key="classes.manage" />
				</th>
				<th style="width: 80px;"><fmt:message key="student.manage" /></th>
				<th style="width: 80px;"><fmt:message key="course.manage" /></th>
				<th name="classesName" style="width: 100px;"><fmt:message key="classesName" /></th>
				<th name="teacherName"><fmt:message key="classes.major" /></th>
				<th name="description"><fmt:message key="classes.description" /></th>
				<th name="idDisk==null?'':'是'" width="80px" align="center"><fmt:message key="classes.disk" /></th>
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
					<fmt:message key="classes.manage" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="title">
							<fmt:message key="classes.year" />
						</label> <select class="combobox form-control" id="year" name="year" required="true"
							onchange="fnChange()">
							<option></option>
						</select>
					</div>

					<div class="form-group">
						<label for="num">
							<fmt:message key="classes.num" />
						</label> <input type="text" class="form-control" id="num" name="num" required="true"
							onchange="fnChange()" />
					</div>

					<div class="form-group">
						<label for="name">
							<fmt:message key="classesName" />
						</label> <input type="text" class="form-control" id="name" name="name" required="true" />
					</div>
					<div class="form-group">
						<label for="teacherId">
							<fmt:message key="classes.major" />
						</label> <select class="combobox form-control" id="teacherId" name="teacherId"
							style="width: 100%;">
							<option></option>
						</select>
					</div>

					<div class="form-group">
						<label for="remark">
							<fmt:message key="classes.description" />
						</label>
						<textarea class="form-control" id="remark" name="remark" style="width: 100%;"></textarea>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="disk" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="classes.disk" />
				</button>
				<button type="button" class="btn btn-primary" id="c_disk" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="classes.c_disk" />
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
	var url = "rest/classes";
	var action;
	var rows = 10;
	var richedit;

	$(document).ready(function() {

		initRowCheck($("#table"));
		richedit = initRichedit("remark");

		//加载教师数据
		$.getJSON("rest/teacher/classes", function(data) {
			for (var i = 0; i < data.length; i++) {
				$('#teacherId').append("<option value='"+ data[i].id + "'>" + data[i].name + "</option>");
			}
		});

		//加载班级届数
		selectYear();
		selectCount();
		
		//加载导入时的字段约束信息
		$.getJSON("rest/classes/rule", function(data) {
			importTable($("#import_table"), data, null, false, null);
		});

		//删除数据
		$("#remove").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				$.each($("input[name=id]:checked"), function(i, n) {
					restDelete(url + "/" + $(n).val(), function() {
					});
				});
				selectCount();
			}
		});

		//查询按钮
		$("#select").click(function() {
			selectCount();
		});
		
		$("#export").click(function() {
			window.location = url + "/export?classesName=" + $("#keyword").val();
		});
		
		$("#export_import").click(function() {
			window.location = url + "/export";
		});
		
		$("#import").click(function() {
			showDialog($("#dialog_import"));
			$("#del").prop("checked", true);
		});
		
		$("#submit_import").click(function() {
			var button = $(this);
			button.button('loading');
			if ($('#del').is(":checked") == true) {
				if (confirm("<fmt:message key="import.clear.confirm" />")) {
					restInsert(url + "/imported", {classesName : $("#keyword").val(), del : 1}, $("#form_import"), function(data) {	
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
				restInsert(url + "/imported", {classesName : $("#keyword").val(), del : 0}, $("#form_import"), function(data) {	
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
		
		$("#disk").click(function() {
			var button = $(this);
			if (confirm("<fmt:message key="classes.disk_conform" />")) {
				button.button('loading');
				restUpdate(url + "/" + $("#id").val() + "/disk/1/" + $("#teacherId").val(), null, null, function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
					selectCount();
				});
			}
		});
				
		$("#c_disk").click(function() {
			var button = $(this);
			if (confirm("<fmt:message key="classes.c_disk_conform" />")) {
				button.button('loading');
				restUpdate(url + "/" + $("#id").val() + "/disk/0/" + $("#teacherId").val(), null, null, function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
					selectCount();
				});
			}
		});

		//提交按钮
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
		restSelect("rest//classesTeacher/teacher" + "/count", {
			"classesName" : $("#keyword").val()
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		}); 
	}
	function select(page) {
		restSelect("rest//classesTeacher/teacher", {
			"classesName" : $("#keyword").val(),
			"page" : page,
			"rows" : rows 
		}, function(json) {
			refreshTable($("#table"), json, [ {
				name : '<fmt:message key="update" />',
				func : "update"
			}, {
				name : '<fmt:message key="classes.teacher" />',
				func : "addTeacher"
			}, {
				name : '<fmt:message key="student.manage" />',
				func : "studentM"
			}, {
				name : '<fmt:message key="course.manage" />',
				func : 'curriculum'
			} ],null,false);
		});
	}

	function update(id) {
		showDialog($("#dialog"));
		
		restGet(url + "/" + id, null, $('#form'), function(data) {
			richedit.html(data.description);
			
			if (data.idDisk != null) {
				$("#disk").hide();
				$("#c_disk").show();
			} else {
				$("#disk").show();
				$("#c_disk").hide();
			}
		});

	}

	function fnChange() {
		$('#name').val($('#year').val() + '<fmt:message key="classes.tip1" />' + $('#num').val() + '<fmt:message key="classes.tip2" />');
	}

	function create() {
		showDialog($("#dialog"));
		$("#disk").hide();
		$("#c_disk").hide();
		richedit.html("");
		$("#form")[0].reset();
		$("#id").val("");
	}

	function selectYear() {
		var dateY = new Date().getFullYear();

		for (var i = 20; i > 0; i--) {
			$('#year').append("<option value=" + (dateY - i) + ">" + (dateY - i) + "</option>");
		}
		for (var i = 0; i < 21; i++) {
			if (i == 0) {
				$('#year')
						.append("<option value=" + (dateY + i) + " selected='true'" + ">" + (dateY + i) + "</option>");
			} else {
				$('#year').append("<option value=" + (dateY + i) + ">" + (dateY + i) + "</option>");
			}
		}
	}

	//教师、课程管理
	function addTeacher(id) {
		window.open("courseTeacher.jsp?classesId=" + $("#classesId").val(),"_self");
	}

	//学生管理	
	function studentM(id) {
		window.open("student.jsp?classesId=" + id,"_self");
	}
	
	// 课表管理	
	function curriculum(id) {
		window.open("module/curriculum/curriculum.jsp?classesId=" + id,"_self");
	}
	
</script>
<%@ include file="footer.jsp"%>