<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header_dialog.jsp" %>

<div class="panel panel-default" >
	<div class="panel-heading" >
		<h3 class="panel-title"><span id="parent-title"><fmt:message key="parent.manage" /></span></h3>
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
				<th style="width:80px;">收藏列表</th>
				<th name="name"><fmt:message key="name" /></th>
				<th name="typeName"><fmt:message key="type" /></th>
				<th name="mobile"><fmt:message key="mobile" /></th>
				<th name="idDisk == null ? '' : '是'" width="80px;" align="center"><fmt:message key="p.disk" /></th>
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
					<fmt:message key="parent.manage" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />					
					<div class="form-group">
						<label for="name"><fmt:message key="name" /></label>
						<input type="text" class="form-control" id="name" name="name" required="true" />
					</div>
					
					<div class="form-group">
						<label for="type"><fmt:message key="type" /></label>
						<select class="form-control combobox" id="type" name="type"  required="true">
						</select>
					</div>
					
					<div class="form-group">
						<label for="mobile"><fmt:message key="mobile" /></label>
						<input type="text" class="form-control" id="mobile" name="mobile" />
					</div>
					
					<div class="form-group">
						<label for="username"><fmt:message key="username" /></label>
						<input type="text" class="form-control" id="username" name="username" />
					</div>
					
					<div class="form-group">
						<label for="password"><fmt:message key="password" /></label>
						<input type="text" class="form-control" id="password" name="password" required="true" />
					</div>
					
					<div class="form-group">
						<label for="pta"><fmt:message key="parent.member" /></label>
						<select class="form-control combobox" id="pta" name="pta" >
							<option value="2"><fmt:message key="no" /></option>
							<option value="1"><fmt:message key="yes" /></option>
						</select>
					</div>
					<div class="form-group">
						<label for="picture"><fmt:message key="pic.upload" /></label>
						<input type="file" id="image" name="image" />
						<img style="width:200px;height:200px;" class="img-rounded" id="picture_show" />
						<span id="picture_delete" style="cursor:pointer;">
							<fmt:message key="pic.del" />
						</span>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="disk" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="p.disk" />
				</button>
				<button type="button" class="btn btn-primary" id="c_disk" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="p.c_disk" />
				</button>
				<button type="button" class="btn btn-primary" id="resetPwd" data-loading-text="<fmt:message key="password.reseting" />">
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
	var url = "rest/parents";
	var rows = 10;
	var studentId = <%=request.getParameter("studenId")%>;
	var classesId = <%=request.getParameter("classesId")%>;

	$(document).ready(function() {
		
		initRowCheck($("#table"));
		
		$("#select").click(function() {
			selectCount();
		});
		
		selectCount();
		getDictionary();
		
		$("#remove").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				$.each($("input[name=id]:checked"), function(i, n) {
					restDelete(url + "/" + $(n).val() + "/" + studentId, function() {
						selectCount();
					});
				});
			}
		});
		
		$("#picture_delete").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				restDelete(url + "/" + $("#id").val() + "/image", function() {
					$("#picture_show").attr("src", "");
				});
			}
		});
		
		$.getJSON("rest/student/"+studentId, function(data) {
					$("#parent-title").text(data.name+"-家长管理");
				});
		
		$("#disk").click(function() {
			var button = $(this);
			if (confirm("<fmt:message key="p.disk_conform" />")) {
				button.button('loading');
				restUpdate(url + "/" + $("#id").val() + "/"+ classesId +"/1/" + studentId, null, null, function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
					selectCount();
				});
			}
		});
				
		$("#c_disk").click(function() {
			var button = $(this);
			if (confirm("<fmt:message key="p.c_disk_conform" />")) {
				button.button('loading');
				restUpdate(url + "/" + $("#id").val() + "/" + classesId + "/0/" + studentId, null, null, function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
					selectCount();
				});
			}
		});
		
		//提交按钮
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');
				if ($("#id").val() == "") {
					restInsert(url, {studentId : studentId}, $("#form"), function(data) {	
						button.button('reset');
						hideDialog($("#dialog"));
						selectCount();
						
					});
				} else {
					restUpdate(url + "/" + $("#id").val(), {studentId : studentId}, $("#form"), function(data) {
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
		restSelect(url + "/count", {studentId : studentId, "keyword" : $("#keyword").val()}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {studentId : studentId, "keyword" : $("#keyword").val(), "page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, [{name: '<fmt:message key="update" />', func: "update"},{name: '收藏列表', func: "favorite"}]);
		});
	}

	function create() {
		showDialog($("#dialog"));
		$("#picture_show").hide();
		$("#picture_delete").hide();
		$("#disk").hide();
		$("#c_disk").hide();
		$("#form")[0].reset();
		$("#id").val("");
		
		$.getJSON("rest/global/parents.password", function(data){
			$("#password").val(data.value);
		});
		
		$("#resetPwd").hide();
		//$("#password").val("1234");
		//alert('1:'+$("#password").attr("readonly");
	}
	
	function update(id) {
		showDialog($("#dialog"));
		$("#resetPwd").show();

		restGet(url + "/" + id, null, $('#form'), function(data) {
			$("#picture_show").attr("src", "file/" + <%=teacher.getSchoolId()%> + "/parents/" + data.id + ".png");
			$("#picture_show").show();
			$("#picture_delete").show();
			if (data.idDisk != null) {
				$("#disk").hide();
				$("#c_disk").show();
			} else {
				$("#disk").show();
				$("#c_disk").hide();
			}
			if (data.pta) {
				$("#pta").val('1');
			} else {
				$("#pta").val('2');
			}
		});
		
	}
	
	function getDictionary() {
		$.getJSON("rest/value/list?keyword=parents,type", function(data){
			for(var i = 0; i < data.length; i ++){
				$('#type').append("<option value='"+ data[i].code + "'>" + data[i].name + "</option>");
			}
		});
	}
	
	function favorite(id) {
    window.open("module/favorite/favorite.jsp?parentsId=" + id ,"_self");
}

</script>

<%@ include file="footer.jsp" %>
