<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="sensitive" />
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
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="refresh">
						<fmt:message key="sensitive.refresh" />
					</button>
				</div>
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin: 0px;"></ul>
			</div>
			<div class="col-md-4">
				<div class="input-group">
					<input type="text" class="form-control" id="keyword" /> <span class="input-group-btn">
						<button class="btn btn-default" type="button" id="select">
							<fmt:message key="select" />
						</button>
					</span>
				</div>
			</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th name="name" width="100px"><fmt:message key="sensitive.name" /></th>
				<th name="name0"><fmt:message key="sensitive.name0" /></th>
				<th name="name1"><fmt:message key="sensitive.name1" /></th>
				<th name="name2"><fmt:message key="sensitive.name2" /></th>
				<th name="name3"><fmt:message key="sensitive.name3" /></th>
				<th name="name4"><fmt:message key="sensitive.name4" /></th>
				<th name="name5"><fmt:message key="sensitive.name5" /></th>
				<th name="name6"><fmt:message key="sensitive.name6" /></th>
				<th name="name7"><fmt:message key="sensitive.name7" /></th>
				<th name="available" type="boolean" value0="是" value1="否"><fmt:message key="sensitive.available" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
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
						<label for="picture"><fmt:message key="import.step2" /></label> <input type="file" id="import" name="import" />
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
					<thead>
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

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="title">
					
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data" class="form-horizontal">
					<input type="hidden" name="id" id="id" />

					<div class="form-group" style="margin-bottom: 5px;">
						<label for="name" class="col-sm-2"><fmt:message key="sensitive.name" /></label>
							
					</div>
					
					<div class="form-group">
						<div class="col-sm-12">
						 <input type="text" class="form-control" id="name"
							name="name" required="true" />
						</div>
					</div>
					
					
					<div class="form-group" style="margin-top: 30px;margin-bottom: 5px;">
						<label for="name" class="col-sm-12"><fmt:message key="sensitive.namea" /></label>
					</div>
					
					<div class="form-group">
						<div class="col-sm-3">
						 <input type="text" class="form-control" id="name0"
							name="name0" placeholder="<fmt:message key="sensitive.name0" />" />
						</div>	
						
						<div class="col-sm-3">
						 <input type="text" class="form-control" id="name1"
							name="name1" placeholder="<fmt:message key="sensitive.name1" />" />
						</div>
						
						<div class="col-sm-3">
						 <input type="text" class="form-control" id="name2"
							name="name2" placeholder="<fmt:message key="sensitive.name2" />" />
						</div>
						
						<div class="col-sm-3">
						 <input type="text" class="form-control" id="name3"
							name="name3" placeholder="<fmt:message key="sensitive.name3" />" />
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-sm-3">
						 <input type="text" class="form-control" id="name4"
							name="name4" placeholder="<fmt:message key="sensitive.name4" />" />
						</div>	
						
						<div class="col-sm-3">
						 <input type="text" class="form-control" id="name5"
							name="name5" placeholder="<fmt:message key="sensitive.name5" />" />
						</div>
						
						<div class="col-sm-3">
						 <input type="text" class="form-control" id="name6"
							name="name6" placeholder="<fmt:message key="sensitive.name6" />" />
						</div>
						
						<div class="col-sm-3">
						 <input type="text" class="form-control" id="name7"
							name="name7" placeholder="<fmt:message key="sensitive.name7" />" />
						</div>
					</div>
								
					
					<div class="checkbox">
						<label> <input type="checkbox" checked name="available" id="available" /><fmt:message key="sensitive.available" />
						</label>
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
<script type="text/javascript">
var url = "rest/sensitive";
var rows = 10;

$(document).ready(function() {
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
	
	$("#refresh").click(function() {
		if (confirm("<fmt:message key="remove.confirm" />")) {
			restGet(url+"/refresh",null,null,function(data){
				
				if(data.success){
					alert("<fmt:message key="sensitive.refresh.success" />"+data.message);
				}else{
					alert("<fmt:message key="import.refresh.failure" />\n\n" + data.message);
				}
			});
		}
	});

	$("#select").click(function() {
		selectCount();
	});
	
	$("#export").click(function() {
		window.location = url + "/export?keyword=" + $("#keyword").val();
	});



	$("#import").click(function() {
		showDialog($("#dialog_import"));
		$("#del").prop("checked", true);
	});
	
	
	$("#submit_import").click(
						function() {
							var button = $(this);
							button.button('loading');
							if ($('#del').is(":checked") == true) {
								if (confirm("<fmt:message key="import.clear.confirm" />")) {								
        							restInsertS(url + "/imported", {
										del : 1
									}, $("#form_import"), function(data)
        							 {
        							
										
											button.button('reset');
											if (data.success) {
												alert("<fmt:message key="import.success.0" />" + data.message
													+ "<fmt:message key="import.success.1" />");
												hideDialog($("#dialog_import"));
												selectCount();
											} else {
												alert("<fmt:message key="import.failure" />\n\n" + data.message);
											}

									} );
								} else {
									button.button('reset');
								}
							} else {
								restInsert(url + "/imported", {
									del : 0
								}, $("#form_import"), function(data) {
									console.log(data);
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
	
	
	$.getJSON(url + "/rule", function(data) {
		importTable($("#import_table"), data, null, false, 2);
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
		} ]);
	});
}

function create() {
	
	showDialog($("#dialog"));
	$("#title").text("<fmt:message key="sensitive.create" />");
	$("#form")[0].reset();
	$("#id").val("");
}

function update(id) {

	$("#title").text("<fmt:message key="sensitive.update" />");	
	showDialog($("#dialog"));

	restGet(url + "/" + id, null, $('#form'), function(data) {
		if (true == data.available) {
				$("#available").prop("checked", true);
			}
	});
}

var restInsertS = function(url, params, form, f) {
	url = randomUrl(url);
	if (form == null) {
		$.post(url, params, f);
	} else {
		form.ajaxSubmit({
			url: url,
			type: "POST",
			dataType: "json", 
			data: params,
			async: false, 
			success: f,
						error: function(xhr, status, error) {
				f();
			}
		});
	}
};

</script>
<%@ include file="footer.jsp"%>
