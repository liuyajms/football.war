<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title"><fmt:message key="role" /></h3>
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
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin:0px;"></ul>
			</div>
			<div class="col-md-4">
			</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead >
			<tr>
				<th style="width:30px;"><input type="checkbox" class="rows"/></th>
				<th style="width:50px;"><fmt:message key="update" /></th>
				<th style="width:50px;"><fmt:message key="permission" /></th>
				<th name="name"><fmt:message key="name1" /></th>
				<th name="description"><fmt:message key="description2" /></th>
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
				<h4 class="modal-title"><fmt:message key="role" /></h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="title"><fmt:message key="name1" /></label>
						<input type="text" class="form-control" id="name" name="name" required="true" />
					</div>
					
					<div class="form-group">
						<label for="titleShort"><fmt:message key="description2" /></label>
						<input type="text" class="form-control" id="description" name="description" required="true" />
					</div>

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


<div class="modal fade" id="dialog_popedom" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog  modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><fmt:message key="role" /></h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form_popedom" method="post">
					<input type="hidden" id="role_id" />
					<table id="table_popedom" class="table table-bordered" cellspacing="0" width="100%">
						<thead >
							<tr>
								<th><input type="checkbox" class="rows"/><fmt:message key="resource" /></th>
								<th colspan="16"><fmt:message key="function" /></th>
							</tr>
						</thead>
						<tbody />
					</table>
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
	var title = "<fmt:message key="role" />";
	var url = "rest/role";
	var rows = 10;
	var lastErrors;
	
	$(document).ready(function() {
		initRowCheck($("#table"));
		
		$("#remove").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				$.each($("input[name=id]:checked"), function(i, n) {
					restDelete(url + "/" + $(n).val(), function() {
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
		
		$("#submit_popedom").click(function() {
			var button = $(this);
			button.button('loading');
			$.each($("input[name=popedom_id]"), function(i, n) {
				var popedomId = $(n).val();
				var action = 0;
				for (var i = 0; i < 16; i++) {
					if ($("#" + popedomId + "action" + i).is(":checked")) {
						action += (1 << i);
					}
				}
				$("#" + popedomId + "action").val(action);
			});
			restUpdate("rest/role/" + $("#role_id").val() + "/popedom", null, $("#form_popedom"), function(data) {
				button.button('reset');
				hideDialog($('#dialog_popedom'));
			});
		});

		selectCount();
		selectPopedom();

	});
	
	function create(){
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#id").val("");
	}

	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
		});
	}
	
	function selectCount() {
		restSelect(url + "/count", null, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {"page" : page, "rows" : rows}, function(json) {
			
		refreshTable($("#table"), json, [ {
					name : '<fmt:message key="update" />',
					func : "update"
				}, {
					name : '<fmt:message key="permission" />',
					func : "popedom"
				} ]);
			});
	}

	// 角色权限 
	function popedom(id) {
		$("#form_popedom")[0].reset();
		$("#role_id").val(id);
		restSelect("rest/role/" + id + "/popedom", null, function(json) {
			showDialog($("#dialog_popedom"));
			$.each(json, function(i, n) {
				for (var j = 0; j < 16; j++) {
					var index = 1 << j;
					if ((index & n.action) > 0) {
						$("#" + n.id + "action" + j).prop("checked", true);
					}
				}
			});
		});
	}

	function selectPopedom() {
		restSelect("rest/popedom", null, function(json) {
			refreshPopedomTable($("#table_popedom"), json);
		});
	}

	function refreshPopedomTable(table, json) {
		var tbody = $(">tbody", table);
		tbody.empty();

		$
				.each(
						json,
						function(i, n) {
							var tr = $("<tr></tr>");
							tbody.append(tr);

							var more = n.action8Name != null;

							var td = $("<td></td>");
							td.attr("rowspan", more ? 2 : 1);
							td
									.append("<input type=checkbox class=row value=" + n.id + " />");
							td
									.append("<input type=hidden name=popedom_id value=" + n.id + " />");
							td
									.append("<input type=hidden name=action value=0 id=" + n.id + "action />");
							td.append("<strong>" + n.name + "</strong>");
							tr.append(td);

							for (var i = 0; i < 8; i++) {
								var td = $("<td></td>");

								var text = eval("n.action" + i + "Name");
								if (text != null) {
									td
											.append("<input type=checkbox id=" + n.id + "action" + i + " />");
									td.append(text);
								} else {
									more = false;
								}

								tr.append(td);
							}

							if (more) {
								var tr = $("<tr></tr>");
								tbody.append(tr);
								for (; i < 16; i++) {
									var td = $("<td></td>");

									var text = eval("n.action" + i + "Name");
									if (text != null) {
										td
												.append("<input type=checkbox id=" + n.id + "action" + i + " />");
										td.append(text);
									}

									tr.append(td);
								}
							}
						});

		$(".row", $("#table_popedom")).click(function() {
			var id = $(this).val();
			for (var i = 0; i < 16; i++) {
				var checkbox = $("#" + id + "action" + i);
				if (checkbox != null) {
					checkbox.prop("checked", $(this).is(':checked'));
				}
			}
		});
		$(".rows", $("#table_popedom")).click(function() {
			var checkboxs = $(".row", $("#table_popedom"));
			checkboxs.prop("checked", $(this).is(':checked'));
			$.each(checkboxs, function(i, n) {
				var checkbox = $(n);
				var id = checkbox.val();
				for (var i = 0; i < 16; i++) {
					var checkbox = $("#" + id + "action" + i);
					if (checkbox != null) {
						checkbox.prop("checked", $(this).is(':checked'));
					}
				}
			});
		});
	}
</script>
<%@ include file="footer.jsp" %>
