<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header_dialog.jsp" %>	
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="schoolManage.title" />	
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
                <button type="button" class="btn btn-default" id="export">
                    <fmt:message key="export" />
                </button>
            </div>
			<div class="btn-group">
				<button type="button" class="btn btn-default" id="manager" onclick="manager()">
					<fmt:message key="schoolManage.set" />
				</button>
			</div>
		</div>
		<div class="col-md-4">
			<ul class="pagination" id="pagination" style="margin:0px;"></ul>
		</div>
		<div class="col-md-4">
			<div class="input-group">
				<input type="text" class="form-control" id="keyword" />
				<span class="input-group-btn">
					<button class="btn btn-default" type="button" id="select">
						<fmt:message key="select" />
					</button>
				</span>
			</div>
		</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead >
			<tr>
				<th style="width:30px;"><input type="checkbox" class="rows"/></th>
				<th style="width:50px;"><fmt:message key="operation" /></th>
				<th name="name"><fmt:message key="teacher.name" /></th>
				<th name="mobile"><fmt:message key="phone" /></th>
				<th name="description"><fmt:message key="schoolManage.description" /></th>
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
					<fmt:message key="schoolManage.title" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="name">
							<fmt:message key="teacher.name" />
						</label>
						<input type="text" class="form-control" id="name" name="name" required="true" />
					</div>
					<div class="form-group">
						<label for="remark">
							<fmt:message key="schoolManage.form1" />
						</label>
						<input type="text" class="form-control" id="mobile" name="mobile" required="true" />
					</div>
					<div class="form-group">
						<label for="remark">
							<fmt:message key="password" />
						</label>
						<input type="password" class="form-control" id="password" name="password" required="true" />
					</div>
					<div class="form-group">
						<label for="remark">
							<fmt:message key="schoolManage.description" />
						</label>
						<input type="text" class="form-control" id="description" name="description" />
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="submit_manager_cancel" data-loading-text="<fmt:message key="canceling" />">
					<fmt:message key="schoolManage.form.btn" />
				</button>
				<button type="button" class="btn btn-primary" id="submit" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="dialog_manager" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<fmt:message key="schoolManage.title" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form_manager" method="post">
					<div class="form-group">
						<label for="name"><fmt:message key="teacher" /></label>
						<select class="combobox form-control" id="teacherId" name="teacherId" required="true">
							<option></option>
						</select>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="submit_manager" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

	var url = "rest/teacher";
	var rows = 10;
	var schoolId = <%=request.getParameter("schoolId")%>;

	$(document).ready(function() {
		initRowCheck($("#table"));
		selectCount();
		
		getTeacherData();
		
		$("#mobile").bind("change", function(){
			if(!(/^\d{11}$/g).test($("#mobile").val())){
				alert("<fmt:message key="schoolManage.tip1" />");
				$("#mobile").focus();
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
		
		$("#select").click(function() {
			selectCount();
		});
		
		$("#submit_manager").click(function() {
			var button = $(this);
			validate($("#form_manager"), function() {
				button.button('loading');
				restUpdate(url + "/admin", null, $("#form_manager"), function(data) {
					button.button('reset');
					hideDialog($('#dialog_manager'));
					alert('<fmt:message key="operate.success" />');
					selectCount();
				});
			});
		});
		
		$("#submit_manager_cancel").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');
				restUpdate(url + "/admin", {"id" : $("#id").val()}, null, function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
					alert('<fmt:message key="operate.success" />');
					selectCount();
				});
			});
		});
		
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');

				if ($("#id").val() == "") {
					restInsert(url, {"schoolId" : schoolId}, $("#form"), function(data) {	
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
	
	//加载任课教师下拉框数据
		function getTeacherData() {
			$.getJSON("rest/teacher/notAdmin?schoolId=" + schoolId, function(data) {
				for (var i = 0; i < data.length; i++) {
					$('#teacherId').append("<option value='"+ data[i].id + "'>" + data[i].name + "</option>");
				}
			});
		}
	
	function selectCount() {
		restSelect(url + "/count", {"keyword" : $("#keyword").val(), "schoolId" : schoolId}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {"keyword" : $("#keyword").val(), "schoolId" : schoolId, "page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, [{name: '<fmt:message key="update" />', func: "update"}]);
		});
	}
	
	function create(){
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#id").val("");
		$("#submit_manager_cancel").hide();
	}
	
	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			if(data.available) {
				$("#available").attr("checked", "checked");
				$("#available").val("1");
				$("#submit_manager_cancel").show();
			}
		});
	}
	
	function manager(){
		showDialog($("#dialog_manager"));
		$("#form_manager")[0].reset();
	}

</script>
</div>

<%@ include file="footer.jsp" %>
