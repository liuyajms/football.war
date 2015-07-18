<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header_dialog.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">用户菜单</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="create" onclick="create()">添加</button>
					<button type="button" class="btn btn-default" id="remove">删除</button>
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
				<th style="width: 50px;">操作</th>
				<th name="schoolId" >学校Id</th>
				<th name="menuId">菜单Id</th>
				<th name="userId">用户ID</th>
				<th name="time" type="timestamp">创建时间</th>
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
				<h4 class="modal-title">用户菜单</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" >
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="menuId">菜单Id</label>
						<input type="text" id="menuId" class="form-control" name="menuId" />
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<button type="button" class="btn btn-primary" id="submit" data-loading-text="保存中...">保存</button>
			</div>
		</div>
	</div>
</div>



<script type="text/javascript">
	var url = "../../rest/userMenu";
	var rows = 10;
	var richedit;
	$(document).ready(function() {
	
		initRowCheck($("#table"));
	//	richedit = initRichedit("description");
	
		$("#select").click(function() {
			selectCount();
		});
		
		$("#export").click(function() {
		//	window.location = url + "/export?classesId=" + classesId + "&courseId=" + courseId;
		});
		
	
		$("#remove").click(function() {
			if (confirm("您确定吗？")) {
				$.each($("input[name=id]:checked"), function(i, n) {
					restDelete(url + "/" + 40, function() {
					});
				});
				selectCount();
			}
		});
		
		
		
	
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');
				if ($("#id").val() == "") {
					restInsert(url, {
				
					}, $("#form"), function(data) {
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
	
		selectCount();
	
	});
	
	function create() {
		showDialog($("#dialog"));
	//	richedit.html("");
		$("#form")[0].reset();
		$("#id").val("");
		$("#submit").show();
	}
	
	function update(id) {
		showDialog($("#dialog"));
	
		restGet(url + "/" + id, null, $('#form'), function(data) {
			//richedit.html(data.description);
			//$("#submit").hide();
		});
	}
	

	function selectCount() {
	/* 	restSelect(url + "/count", {
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		}); */
		select();
	}
	function select(page) {
		restSelect(url, {
			"page" : page,
			"rows" : rows
		}, function(json) {
			refreshTable($("#table"), json, [ {
				name : '修改',
				func : "update"
			} ]);
		});
	}

</script>
<%@ include file="../../footer.jsp" %>