<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="template" />
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group"></div>
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
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th name="code"><fmt:message key="name1" /></th>
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
					<fmt:message key="template" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="code" id="code" />
					<textarea class="form-control" id="remark" name="remark" style="width: 100%;" required="true"></textarea>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="submit" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var url = "rest/template";
	var rows = 10;
	var richedit;

	$(document).ready(function() {
		initRowCheck($("#table"));
		richedit = initRichedit("remark", '500px');
		selectCount();

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

		$("#submit").click(function() {
			richedit.sync();
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');
				if ($("#code").val() == "") {
					restInsert(url, null, $("#form"), function(data) {
						button.button('reset');
						hideDialog($("#dialog"));
						selectCount();

					});
				} else {
					restUpdate(url + "/" + $("#code").val(), null, $("#form"), function(data) {
						button.button('reset');
						hideDialog($('#dialog'));
						selectCount();
					});
				}
			});

		});
	});

	function selectCount() {
		restSelect(url + "/count", null, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {
			"page" : page,
			"rows" : rows
		}, function(json) {
			refreshTable($("#table"), json, [ {
				name : '<fmt:message key="update" />',
				func : "update"
			} ], [ "code" ]);
		});
	}

	function create() {
		showDialog($("#dialog"));
		richedit.html("");
		$("#form")[0].reset();
		$("#id").val("");
	}

	function update(code) {
		showDialog($("#dialog"));

		restGet(url + "/" + code, null, $('#form'), function(data) {
			richedit.html(data.description);
		});
	}
</script>
<%@ include file="footer.jsp"%>