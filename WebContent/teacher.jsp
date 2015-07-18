<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="teacher" />
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
				<th name="name" width="100px"><fmt:message key="teacher.name" /></th>
				<th name="code"><fmt:message key="teacher.code" /></th>
				<th name="card"><fmt:message key="teacher.card" /></th>
				<th name="mobile"><fmt:message key="teacher.mobile" /></th>
				<th name="email"><fmt:message key="teacher.email" /></th>
				<th name="idDisk==null?'':'是'" style="width:80px;" align="center"><fmt:message key="teacher.disk" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
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
						<label for="picture"><fmt:message key="import.step2" /></label> <input type="file" id="import" name="import" />
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

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<fmt:message key="teacher" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />
					<div class="form-group" style="display: none;">
						<label for="roleId"><fmt:message key="role" /></label> <select class="combobox" id="roleId" name="roleId"
							style="height: 33px; width: 100px;">
							<option></option>
						</select>
					</div>

					<div class="form-group">
						<label for="name"><fmt:message key="teacher.name" /></label> <input type="text" class="form-control" id="name"
							name="name" required="true" />
					</div>
					
					<div class="form-group">
						<label for="gender">性别</label>
						<select class="form-control combobox" id="gender" name="gender">
							<!-- <option></option>
							<option value="0">男</option>
							<option value="1">女</option> -->
						</select>
					</div>

					<div class="form-group">
						<label for="username"><fmt:message key="teacher.username" /></label> <input type="text" class="form-control"
							id="username" name="username" />
					</div>

					<div class="form-group">
						<label for="password"><fmt:message key="teacher.password" /></label> <input type="text" class="form-control"
							id="password" name="password" required="true" />
					</div>

					<div class="form-group">
						<label for="mobile"><fmt:message key="teacher.mobile" /></label> <input type="text" class="form-control"
							id="mobile" name="mobile" />
					</div>

					<div class="form-group">
						<label for="mobile"><fmt:message key="teacher.code" /></label> <input type="text" class="form-control" id="code"
							name="code" />
					</div>

					<div class="form-group">
						<label for="mobile"><fmt:message key="teacher.card" /></label> <input type="text" class="form-control" id="card"
							name="card" />
					</div>

					<div class="form-group">
						<label for="mobile"><fmt:message key="teacher.email" /></label> <input type="text" class="form-control"
							id="email" name="email" />
					</div>

					<div class="form-group">
						<label for="title"><fmt:message key="teacher.title" /></label> <select class="combobox" id="title" name="title"
							style="height: 33px; width: 100px;">
							<option></option>
						</select>
					</div>

					<div class="form-group">
						<label for="picture"><fmt:message key="teacher.image.create" /></label> <input type="file" id="image"
							name="image" /> <img style="width: 200px; height: 200px;" class="img-rounded" id="picture_show" /> <span
							id="picture_delete" style="cursor: pointer;"><fmt:message key="teacher.image.remove" /></span>
					</div>

					<div class="form-group">
						<label for="remark"><fmt:message key="teacher.remark" /></label>
						<textarea class="form-control" id="remark" name="remark" style="width: 100%;"></textarea>
					</div>

					<div class="form-group">
						<label for="description"><fmt:message key="teacher.description" /></label>
						<textarea class="form-control" id="description" name="description" style="width: 100%;"></textarea>
					</div>
					
					<div class="checkbox">
						<label> <input type="checkbox" name="type" id="type" /> <fmt:message key="teacher.admin" />
						</label>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="disk" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="teacher.disk" />
				</button>
				<button type="button" class="btn btn-primary" id="c_disk" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="teacher.c_disk" />
				</button>
				<button type="button" class="btn btn-primary" id="resetPwd"
					data-loading-text="<fmt:message key="password.reseting" />">
					<fmt:message key="password.reset" />
				</button>
				<button type="button" class="btn btn-primary" id="submit" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var url = "rest/teacher";
	var title = "<fmt:message key="teacher" />";
	var rows = 10;
	var richedit;

	$(document).ready(
			function() {

				initRowCheck($("#table"));
				richedit = initRichedit("remark");

				$("#select").click(function() {
					selectCount();
				});

				selectCount();

				$("#export").click(function() {
					window.location = url + "/export?keyword=" + $("#keyword").val();
				});

				$("#export_import").click(function() {
					window.location = url + "/export";
				});

				$("#import").click(function() {
					showDialog($("#dialog_import"));
					$("#del").prop("checked", true);
				});

				$("#mobile").bind("change", function() {
					if (!(/^\d{11}$/g).test($("#mobile").val())) {
						alert("<fmt:message key="teacher.mobile.validate" />");
						$("#mobile").focus();
					}
				});

				$("#picture_delete").click(function() {
					if (confirm("<fmt:message key="remove.confirm" />")) {
						restDelete(url + "/delete/image/" + $("#id").val(), function() {
							$("#picture_show").attr("src", "");
						});
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
				
				$("#disk").click(function() {
					var button = $(this);
					if (confirm("<fmt:message key="teacher.disk_conform" />")) {
						button.button('loading');
						restUpdate(url + "/" + $("#id").val() + "/disk/1", null, null, function(data) {
							button.button('reset');
							hideDialog($('#dialog'));
							selectCount();
						});
					}
				});
				
				$("#c_disk").click(function() {
					var button = $(this);
					if (confirm("<fmt:message key="teacher.c_disk_conform" />")) {
						button.button('loading');
						restUpdate(url + "/" + $("#id").val() + "/disk/0", null, null, function(data) {
							button.button('reset');
							hideDialog($('#dialog'));
							selectCount();
						});
					}
				});

				$.getJSON("rest/value/list?keyword=teacher,title", function(data) {
					for (var i = 0; i < data.length; i++) {
						$('#title').append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");//('loadData', dataJ);
					}
				});
				
				$.getJSON("rest/value/list?keyword=student,gender", function(data) {
					console.log(data);
					for (var i = 0; i < data.length; i++) {
						$('#gender').append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");//('loadData', dataJ);
					}
				});

				/*
				$.getJSON("rest/role?page=0&rows=10", function(data) {
					for (var i = 0; i < data.length; i++) {
						$('#roleId').append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
					}
				});
				 */

				$.getJSON("rest/teacher/rule", function(data) {
					importTable($("#import_table"), data, null, false, null);
				});

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

				//重设密码按钮
				$("#resetPwd").click(function() {
					var button = $(this);
					restUpdate(url + "/" + $("#id").val() + "/resetPwd", null, null, function(data) {
						button.button('reset');
						alert("<fmt:message key="password.reset.success" />");
						hideDialog($("#dialog"));
						selectCount();
					});
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

	function create() {
		showDialog($("#dialog"));
		richedit.html("");
		$("#picture_show").hide();
		$("#picture_delete").hide();
		$("#resetPwd").hide();
		$("#disk").hide();
		$("#c_disk").hide();
		$("#form")[0].reset();
		$("#id").val("");
		$.getJSON("rest/global/parents.password", function(data) {
			$("#password").val(data.value);
		});
	}
	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			$("#roleId").val(data.roleId);
			richedit.html(data.remark);
			$("#picture_show").attr("src", "file/" + data.schoolId + "/teacher/" + data.id + ".png");
			$("#picture_show").show();
			$("#picture_delete").show();
			$("#resetPwd").show();
			
			if (data.idDisk == null) {
				$("#disk").show();
				$("#c_disk").hide();
			} else {
				$("#c_disk").show();
				$("#disk").hide();
			}
			
			if (data.gender == "0") {
				//$("input[name='gender'][value='0']").attr("checked",true);
				$("input[name='gender']").eq(0).attr('checked', 'true');
			} else {
				//$("input[name='gender'][value='1']").attr("checked",true);
				$("input[name='gender']").eq(1).attr('checked', 'true');
			}
			if (data.type == 6) {
				$("#type").prop("checked", true);
			}
		});

	}

function test() {
	 // 页面加载，赋值
    var mark = $('input:radio:checked').val();
          
	    $("input:radio").on("click", function() {
	        // 这里需要更新
	        mark = $(this).val();
	    });
          
    $("#check").on("click", function() {
        alert(mark);
    });
} 
</script>


<%@ include file="footer.jsp"%>