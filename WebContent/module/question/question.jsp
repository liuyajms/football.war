<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">留言咨询</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="create"
						onclick="create()">添加</button>
					<button type="button" class="btn btn-default" id="remove">
						删除</button>
				</div>
				<!-- 	<div class="btn-group">
					<button type="button" class="btn btn-default" id="import">
						导入
					</button>
					<button type="button" class="btn btn-default" id="export">
						导出
					</button>
				</div> -->
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin: 0px;"></ul>
			</div>
			<div class="col-md-4">
				<div class="input-group">
					<input type="text" class="form-control" id="keyword" /> <span
						class="input-group-btn">
						<button class="btn btn-default" type="button" id="select">
							查询</button> </span>
				</div>
			</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered"
		cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 30px;"><input type="checkbox" class="rows" />
				</th>
				<th style="width: 50px;"><fmt:message key="operation" />
				</th>
				<th name="title">标题</th>
				<!-- <th name="ord" width="100px">排序</th>
				
				<th name="description">内容</th> -->
				<th name="createTime" width="150px" type="timestamp">提问时间</th>
				<th name="updateTime" width="150px" type="timestamp">回复时间</th>
				<!-- <th name="updateUser" width="100px" >回复人</th> -->
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
				<h4 class="modal-title">信息</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />
					<div class="form-group" style="display: none;">
						<label for="roleId"><fmt:message key="role" />
						</label> <select class="combobox" id="roleId" name="roleId"
							style="height: 33px; width: 100px;">
							<option></option>
						</select>
					</div>

					<div class="form-group">
						<label for="createUser">提问人</label> 
						<select class="form-control" id="createUserId" name="createUserId">
							<option></option>
							</select>
					</div>
					
					<div class="form-group">
						<label for="title">标题</label> <input type="text"
							class="form-control" id="title" name="title" required="true"/>
					</div>

					<div class="form-group">
						<label for="question">咨询内容</label> <textarea type="text"
							class="form-control" id="question" name="question" ></textarea>
					</div>
					
					<div class="form-group">
						<label for="updateUser">回复人（部门）</label> 
						<select class="form-control" id="updateUserId" name="updateUserId">
							<option></option>
							</select>
					</div>
					
					<div class="form-group">
						<label for="answer">回复内容</label> <textarea type="text"
							class="form-control" id="answer" name="answer" ></textarea>
					</div>

					<%-- 					<div class="form-group">
						<label for="picture">头像上传</label> <input type="file" id="image"
							name="image" /> <img style="width: 200px; height: 200px;" class="img-rounded" id="picture_show" /> <span
							id="picture_delete" style="cursor: pointer;"><fmt:message key="teacher.image.remove" /></span>
					</div> --%>

					<!-- <div class="form-group">
						<label for="description">内容</label>
						<textarea class="form-control" id="description" name="description"
							style="width: 100%;"></textarea>
					</div> -->

					<!-- <div class="checkbox">
						<label> <input type="checkbox" name="admin" id="admin" /> 管理员
						</label>
					</div> -->
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<%-- 				<button type="button" class="btn btn-primary" id="resetPwd"
					data-loading-text="<fmt:message key="password.reseting" />">
					<fmt:message key="password.reset" />
				</button> --%>
				<button type="button" class="btn btn-primary" id="submit"
					data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<div id="menuContent" class="menuContent"
	style="display:none; position: absolute;z-index:9999">
	<ul id="treeDemo" class="ztree" style="margin-top:0; "></ul>
</div>


<script type="text/javascript">
	var url = "../../rest/question";
	//var title = "<fmt:message key="teacher" />";
	var rows = 10;

	var zTree;
	var moduleOption = "<option value=''></option>";; 
	var dptUrl="../../rest/department/tree";

	$(document).ready(
			
			function() {

				initRowCheck($("#table"));


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

/* 				$.getJSON("value/list?keyword=teacher,title", function(data) {
					for (var i = 0; i < data.length; i++) {
						$('#title').append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");//('loadData', dataJ);
					}
				}); */

				/*
				$.getJSON("rest/role?page=0&rows=10", function(data) {
					for (var i = 0; i < data.length; i++) {
						$('#roleId').append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
					}
				});
				 */

/* 				$.getJSON("rest/teacher/rule", function(data) {
					importTable($("#import_table"), data, null, false, null);
				}); */

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
				
				restSelect("../../rest/teacher", {
					"page" : 0,
					"rows" : 99
				}, function(data) {
					fpId = data[0].id;
					for (var i = 0; i < data.length; i++) {
						$('#createUserId').append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");//('loadData', dataJ);
						$('#updateUserId').append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");//('loadData', dataJ);
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
		console.log(json);
			refreshTable($("#table"), json, [ {
				name : '<fmt:message key="update" />',
				func : "update"
			} ]);
		});
	}

	function create() {
		showDialog($("#dialog"));
		
		$("#picture_show").hide();
		$("#picture_delete").hide();
		$("#resetPwd").hide();
		$("#form")[0].reset();
		$("#id").val("");
/*  		$.getJSON("../../rest/global/parents.password", function(data) {
			console.log(data);
			$("#password").val(data.value);
		});  */
	}
	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			//$("#roleId").val(data.roleId);
			console.log(data);
			//$("#picture_show").attr("src", "files/users/" + data.id + ".png");
			//$("#picture_show").show();
			//$("#picture_delete").show();
			$("#resetPwd").show();
			//console.log(data);
			/* if (1 == data.admin) {
				$("#admin").prop("checked", true);
			} */
		});

	}
	
	function eachJson(obj, n) {
		for (var i = 0; i < obj.length; i++) {
			var o = obj[i];
			var seperate = "";
			for (var j = 0; j < n; j++) {
				seperate += "&#12288;";
			}
			moduleOption += "<option value='" + o.id + "'>" + seperate
					+ o.name + "</option>";
			if (o.nodes != null && o.nodes != "") {
				eachJson(o.nodes, (n + 1));
			}
		}
	}

</script>


<%@ include file="../../footer.jsp"%>