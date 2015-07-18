<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="school.title" />
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="create"
						onclick="create()">
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
				</div>
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin: 0px;"></ul>
			</div>
			<div class="col-md-4">
				<div class="input-group">
					<input type="text" class="form-control" id="keyword" /> <span
						class="input-group-btn">
						<button class="btn btn-default" type="button" id="select">
							<fmt:message key="select" />
						</button>
					</span>
				</div>
			</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered"
		cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th style="width: 95px;"><fmt:message key="school.th1" /></th>
				<th style="width: 80px;"><fmt:message key="school.th2" /></th>
				<th name="name" style="width: 150px;"><fmt:message key="schoolName" /></th>
				<th name="phone" style="width: 80px;"><fmt:message key="phone" /></th>
				<%-- <th name="email" style="width: 70px;"><fmt:message key="email" /></th> --%>
				<th name="address"><fmt:message key="address" /></th>
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
				<h4 class="modal-title">
					<fmt:message key="school.title" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="name"> <fmt:message key="schoolName" />
						</label> <input type="text" class="form-control" id="name" name="name"
							required="true" />
					</div>
					<div class="form-group">
						<label for="remark"> <fmt:message key="phone" />
						</label> <input type="text" class="form-control" id="phone" name="phone"
							required="true" />
					</div>
					<div class="form-group">
						<label for="remark"><fmt:message key="email" /></label> <input
							type="text" class="form-control" id="email" name="email"
							required="true" />
					</div>
					<div class="form-group">
						<label for="remark"> <fmt:message key="address" />
						</label> <input type="text" class="form-control" id="address"
							name="address" required="true" />
					</div>

					<div class="form-group">
						<label> <input type="checkbox" name="available"
							id="available" value="1" /> <fmt:message key="available" />
						</label>
					</div>
					<div class="form-group">
						<label for="remark"> <fmt:message key="x" />
						</label> <input type="text" class="form-control" id="vx" name="vx" />
					</div>
					<div class="form-group">
						<label for="remark"> <fmt:message key="y" />
						</label> <input type="text" class="form-control" id="vy" name="vy" />
					</div>
					<div class="form-group">
						<label for="remark"> <fmt:message key="region" />
						</label> <input type="text" class="form-control" id="region" name="region" />
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="submit"
					data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var url = "rest/school";
	var title = "<fmt:message key="school" />";
	var rows = 10;
	var schoolId;

	$(document).ready(
			function() {
				initRowCheck($("#table"));
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

				$("#submit").click(
						function() {
							var button = $(this);
							validate($("#form"), function() {
								button.button('loading');

								if ($("#id").val() == "") {
									restInsert(url, null, $("#form"), function(
											data) {
										button.button('reset');
										hideDialog($("#dialog"));
										selectCount();

									});
								} else {
									restUpdate(url + "/" + $("#id").val(),
											null, $("#form"), function(data) {
												button.button('reset');
												hideDialog($('#dialog'));
												selectCount();
											});
								}
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
			}, {
				name : '<fmt:message key="school.th1" />',
				func : "manage"
			}, {
				name : '<fmt:message key="school.th2" />',
				func : "point"
			} ]);
		});
	}

	function create() {
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#id").val("");
	}

	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			if (data.available) {
				$("#available").attr("checked", "checked");
				$("#available").val("1");
			}
		});
	}

	function manage(id) {
		window.open("schoolManager.jsp?schoolId=" + id,"_self");
	}

	function point(id) {
		window.open("schoolPoint.jsp?schoolId=" + id,"_self");
	}
</script>


<%@ include file="footer.jsp" %>
