<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header_dialog.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="curriculum.typeName" bundle="${curriculum}" />
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
					<button type="button" class="btn btn-default" id="import">
						<fmt:message key="import" />
					</button>
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
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />

					<div class="form-group">
						<label for="picture">
							<fmt:message key="curriculum.pic" bundle="${curriculum}" />
						</label> <input type="file" id="picture" name="picture" /> <img
							style="width: 400px; height: 300px;" class="img-rounded" id="picture_show" /> <span id="picture_delete"
							style="cursor: pointer;">
								<fmt:message key="curriculum.delPic" bundle="${curriculum}" />
							</span>
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
						</label> <input type="checkbox" name="defaultc" id="defaultc" value="1" />
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

<div class="modal fade" id="dialog_import" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<fmt:message key="import" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form_import" id="form_import" method="post">
					<div class="form-group">
						<label><fmt:message key="import.step1" /></label>
						<button type="button" class="btn btn-default" id="export_import">
							<fmt:message key="export" />
						</button>
						<p class="help-block">
							<fmt:message key="import.step1.description" />
						</p>
					</div>

					<div class="form-group">
						<label for="import"><fmt:message key="import.step2" /></label> <input type="file" id="import" name="import" />
						<p class="help-block">
							<fmt:message key="import.step2.description" />
						</p>
					</div>

					<div class="form-group">
						<label style="color: red;"><fmt:message key="import.clear" /></label> <input type="checkbox" id="del" value="1"
							onclick="fnClick('del')" />
					</div>
				</form>
				<table id="import_table" class="table table-striped table-bordered" cellspacing="0" width="100%">
					<thead >
						<tr>
							<th name="name" width="100px"><fmt:message key="import.field.name" /></th>
							<th name="type" width="90px"><fmt:message key="import.field.type" /></th>
							<th name="value"><fmt:message key="import.field.description" /></th>
						</tr>
					</thead>
					<tbody id="tBody" />
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="submit_import" data-loading-text="<fmt:message key="importing" />">
					<fmt:message key="import" />
				</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var url = "../../rest/curriculum";
var rows = 10;

$(document).ready(function() {

	selectCount();
	
	$("#picture_delete").click(function() {
		if (confirm("<fmt:message key="remove.confirm" />")) {
			restDelete(url + "/" + $("#id").val() + "/image", function() {
				$("#picture_show").attr("src", "");
			});
		}
	});
	
	$("#select").click(function() {
		selectCount();
	});

	$("#export").click(function() {
		window.location = url + "/export?classesId=" + $("#classesId").val();
	});
	
	$("#export_import").click(function() {
		window.location = url + "/export?classesId=" + $("#classesId").val();
	});
		
	$("#import").click(function() {
		showDialog($("#dialog_import"));
		$("#del").prop("checked", true);
	});
		
	$.getJSON(url+"/rule", function(data) {
		importTable($("#import_table"), data, null, false, 2);
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
	
	//提交导入信息
		$("#submit_import").click(function() {
			var button = $(this);
			button.button('loading');
			
			if ($('#del').is(":checked") == true) {
				if (confirm("<fmt:message key="import.clear.confirm" />")) {
					restInsert(url + "/imported", {del : 1, classesId : $("#classesId").val()}, $("#form_import"), function(data) {	
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
				restInsert(url + "/imported", {del : 0, classesId : $("#classesId").val()}, $("#form_import"), function(data) {	
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

	$("#submit").click(function() {
		var button = $(this);
		validate($("#form"), function() {
			button.button('loading');
			if ($("#id").val() == "") {
				restInsert(url, {
					classesId : $("#classesId").val()
				}, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					selectCount();

				});
			} else {
				restUpdate(url + "/" + $("#id").val(), {
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
	$("#picture_show").hide();
	$("#picture_delete").hide();
	$("#form")[0].reset();
	$('#id').val("");
	$("#defaultc").prop("checked", true);
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

	restGet(url + "/" + id, null, $('#form'),
		function(data) {
			$("#picture_show").attr("src",
					"../../files/" + data.schoolId + "/curriculum/" + data.id + ".png?" + data.updateTime);
			$("#picture_show").show();
			$("#picture_delete").show();
			
			$("#defaultc").prop("checked", data.defaultc);
		});
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

</script>

<%@ include file="../../footer.jsp"%>