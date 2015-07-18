<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">跑马灯</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="create" onclick="create()">添加</button>
					<button type="button" class="btn btn-default" id="remove">删除</button>
				</div>
			</div>
			<div class="col-md-4"></div>
			<div class="col-md-4"></div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
				<th name="path" type="image">图片</th>
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
				<h4 class="modal-title">跑马灯</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<div class="form-group">
						<label for="file">选择文件</label> <input type="file" id="file" name="file" required="true" />
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


<script>
	var url = "../../rest/marquee";

	$(document).ready(function() {

		initRowCheck($("#table"));

		$("#remove").click(function() {
			if (confirm("您确定吗？")) {
				$.each($("input[name=name]:checked"), function(i, n) {
					restDelete(url + "?name=" + $(n).val(), function() {
					});
				});
				select();
			}
		});

		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');
				restInsert(url, null, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					select();
				});
			});

		});

		select();

	});

	function create() {
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#submit").show();
	}

	function select() {
		restSelect(url, null, function(json) {
			refreshTable($("#table"), json, null, [ "name" ]);
		});
	}
</script>

<%@ include file="../../footer.jsp"%>