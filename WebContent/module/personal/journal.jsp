<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header_dialog.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">个人日志</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="create" onclick="create()">添加</button>
					<button type="button" class="btn btn-default" id="remove">删除</button>
					<button type="button" class="btn btn-default" id="getPics">获取图片列表</button>
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
				<th name="description" style="width: 250px;">描述</th>
				<th name="pic">图片个数</th>
				<th name="voiceLength">声音长度</th>
				<th name="createUser">创建人</th>
				<th name="createTime" type="timestamp">创建时间</th>
<!-- 				<th name="updateUser">更新人</th>
				<th name="updateTime" type="timestamp">更新时间</th> -->
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
				<h4 class="modal-title">个人日志</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="description">描述</label>
						<textarea class="form-control" id="description" name="description" style="width: 100%;"></textarea>
					</div>
					<div class="form-group">
						<label for="pic">上传图片</label> <input type="file" id="pic" name="pic" multiple />
					</div>
					<div class="form-group">
						<label for="voice">上传声音</label> <input type="file" id="voice" name="voice"  />
						<input type="hidden"
							id="voiceLength" name=voiceLength value="0" />
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
	var url = "../../rest/journal";
	var rows = 10;
	var richedit;
	$(document).ready(function() {
	
		initRowCheck($("#table"));
		richedit = initRichedit("description");
	
		$("#select").click(function() {
			selectCount();
		});
		
		$("#export").click(function() {
		//	window.location = url + "/export?classesId=" + classesId + "&courseId=" + courseId;
		});
		
	
		$("#remove").click(function() {
			if (confirm("您确定吗？")) {
				$.each($("input[name=id]:checked"), function(i, n) {
					restDelete(url + "/" + $(n).val(), function() {
					});
				});
				selectCount();
			}
		});
		
		
		$("#getPics").click(function() {
				$.each($("input[name=id]:checked"), function(i, n) {
					restGet(url + "/getPics/" + $(n).val(),null, null,function(data) {
						console.log(data)
					});
				});
		});
		
	
		$("#submit").click(function() {
			var button = $(this);
			richedit.sync();
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
		richedit.html("");
		$("#form")[0].reset();
		$("#id").val("");
		$("#submit").show();
	}
	
	function update(id) {
		showDialog($("#dialog"));
	
		restGet(url + "/" + id, null, $('#form'), function(data) {
			richedit.html(data.description);
			//$("#submit").hide();
		});
	}
	

	function selectCount() {
		restSelect(url + "/count", {
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
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