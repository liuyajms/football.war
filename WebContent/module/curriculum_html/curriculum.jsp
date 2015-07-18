<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header_dialog.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="curriculum" bundle="${curriculum}" />
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
				<th name="className"><fmt:message key="curriculum.className" bundle="${curriculum}" /></th>
				<th name="typeName"><fmt:message key="curriculum.typeName" bundle="${curriculum}" /></th>
				<th name="createTime" style="width: 150px;" type="timestamp"><fmt:message key="curriculum.createTime" bundle="${curriculum}" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<input type="hidden" id="classesId" value="<%=request.getParameter("classesId")%>" />

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><fmt:message key="curriculum" bundle="${curriculum}" /></h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" /> <input type="hidden" name="trId" id="trId" />

					<div class="form-group">
						<label for="description">
							<fmt:message key="curriculum.content" bundle="${curriculum}" />
						</label>
						<table>
							<tr>
								<td style="width: 100%" id="tdId"></td>
							</tr>
						</table>
					</div>

					<div class="form-group">
						<label for="type">
							<fmt:message key="curriculum.typeName" bundle="${curriculum}" />
						</label> <select class="combobox" name="type" id="type">
							<option value="0"><fmt:message key="curriculum.type0" bundle="${curriculum}" /></option>
							<option value="1"><fmt:message key="curriculum.type1" bundle="${curriculum}" /></option>
							<option value="2"><fmt:message key="curriculum.type2" bundle="${curriculum}" /></option>
						</select>
					</div>
					<div class="checkbox">
						<label for="defaultc">
							<fmt:message key="curriculum.defaultType" bundle="${curriculum}" />
						</label> <input type="checkbox" name="defaultc" id="defaultc" value="2"
							onclick="fnClick('defaultc')" />
					</div>

					<div id="myMenu1" class="contextMenu">
						<ul>
							<li id="addOneUp"><fmt:message key="curriculum.row1" bundle="${curriculum}" /></li>
							<li id="addOneDown"><fmt:message key="curriculum.row2" bundle="${curriculum}" /></li>
							<li id="delete"><fmt:message key="curriculum.row3" bundle="${curriculum}" /></li>
						</ul>
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
<script src="<%=path%>/asset/jquery/jquery.contextmenu.r2.js"></script>
<script type="text/javascript">
var url = "../../rest/curriculum";
var rows = 10;

$(document).ready(function() {

	selectCount();

	$("#select").click(function() {
		selectCount();
	});

	$("#export").click(function() {
		window.location = url + "/export?classesId=" + $("#classesId").val();
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

	$("#submit").click(function() {
		var button = $(this);
		validate($("#form"), function() {
			button.button('loading');
			if ($("#id").val() == "") {
				restInsert(url, {
					remark : $("#tdId").html(),
					classesId : $("#classesId").val()
				}, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					selectCount();

				});
			} else {
				restUpdate(url + "/" + $("#id").val(), {
					remark : $("#tdId").html(),
					classesId : $("#classesId").val()
				}, $("#form"), function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
					selectCount();
				});
			}
		});

	});
});

function create() {
	showDialog($("#dialog"));
	$("#form")[0].reset();
	$('#id').val("");
	$('#tdId').html("");
	$('#tdId').load("tableAdd.jsp");
}

function selectCount() {
	restSelect(url + "/count", {
		"classesId" : $("#classesId").val()
	}, function(count) {
		refreshPagination($("#pagination"), count, rows, select);
	});
}
function select(page) {
	restSelect(url, {
		"classesId" : $("#classesId").val(),
		"page" : page,
		"rows" : rows
	}, function(json) {
		refreshTable($("#table"), json, [ {
			name : '<fmt:message key="update" />',
			func : "update"
		} ]);
	});
}

function update(id) {
	showDialog($("#dialog"));

	restGet(url + "/" + id, null, $('#form'), function(data) {
		$('#tdId').html("");
		$("#tdId").html(data.description);
		if (data.defaultc) {
			$("#defaultc").attr("checked", "checked");
			$("#defaultc").val("1");
		}

		var tabAdd = $("#tabAdd").get(0);

		$('#serId').attr("editType", "");

		// 设置表格可编辑
		EditTables(tabAdd);

		$('#tabAdd tr td').bind('mousemove', function() {
			var trSeq = $(this).parent().parent().find("tr").index($(this).parent()[0]);
			$('#trId').val(trSeq);
		});

		// 绑定右键菜单
		$('#tabAdd tr').contextMenu('myMenu1', {
			bindings : {
				'addOneUp' : function(t) {
					AddRow($('#tabAdd').get(0), parseInt($('#trId').val()), "before");
				},
				'addOneDown' : function(t) {
					AddRow($('#tabAdd').get(0), parseInt($('#trId').val()) + 1, "after");
				},
				'delete' : function(t) {
					DeleteRow($('#tabAdd').get(0), parseInt($('#trId').val()));
				}
			}

		});
	});
}

function divClick(id) {
	if (id == 1) {
		AddRow($('#tabAdd').get(0), 0);
	} else if (id == 3) {
		DeleteRow($('#tabAdd').get(0), 0)
	}

}

// 课表类型
function curType(value, row, index) {
	if (value == 0) {
		return "<fmt:message key="curriculum.type0" bundle="${curriculum}" />";
	} else if (value == 1) {
		return "<fmt:message key="curriculum.type1" bundle="${curriculum}" />";
	} else {
		return "<fmt:message key="curriculum.type2" bundle="${curriculum}" />";
	}
}

function fnClick() {
	if ($('#defaultc').is(":checked") == true) {
		$('#defaultc').val("1");
	} else {
		$('#defaultc').val("2");
	}
}
</script>

<%@ include file="../../footer.jsp"%>