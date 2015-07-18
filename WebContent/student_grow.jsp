<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="header_dialog.jsp"%>

<%
		String studentId = (String)request.getParameter("studentId");
	%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="studentGrow" />
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
				<th name="name" style="width: 200px;">
					<fmt:message key="studentGrow.name" />
				</th>
				<th name="typeName" style="width: 150px;">
					<fmt:message key="studentGrow.type" />
				</th>
				<th name="description">
					<fmt:message key="studentGrow.description" />
				</th>
				<th name="updateTime" style="width: 150px;" type="timestamp">
					<fmt:message key="updateTime" />
				</th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="title">
					<fmt:message key="studentGrow" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" /> <input type="hidden" name="classesId"  value="" />
					<input type="hidden" name="studentId" id="studentId"  value='<%=studentId %>' />
	
					<div class="form-group">
						<label for="name">
							<fmt:message key="studentGrow.name" />
						</label>
						<input type="text" class="form-control" id="name" name="name" required="true" />
					</div>
					
 					<div class="form-group">
						<label for="type">
							<fmt:message key="studentGrow.type" />
						</label>
						<select class="combobox form-control" id="type" name="type" required="true" >
						</select>
					</div>
					
					<div class="form-group">
						<label for="description">
							<fmt:message key="studentGrow.description" />
						</label>
						<textarea class="form-control" id="description" name="description" style="width: 100%;"></textarea>
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
var title = "";
var url = "rest/student/grow";
var rows = 10;
var richedit;
var lastErrors;

$(document).ready(function() {
	title = $("#title").html();
	initRowCheck($("#table"));
	richedit = initRichedit("description");

	//$('input[name="classesId"]').val($('#classesId').val())
	
	$("#export").click(function() {
		window.location = url + "/export?studentId=" + $("#studentId").val() + "&keyword=" + $("#keyword").val();
	});
	$("#select").click(function() {
		selectCount();
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
		richedit.sync();
		
		$('#studentId').val();
		
		if( $('#studentId').val() == ""){
			alert("<fmt:message key="studentGrow.tip" />");
			return;
		}
		
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

	selectCount();

	$("#picture_delete").click(function() {
		if (confirm("<fmt:message key="remove.confirm" />")) {
			restDelete(url + "/" + $("#id").val() + "/image", function() {
				$("#picture_show").attr("src", "");
			});
		}
	});


    $.getJSON("rest/value/list?keyword=student_grow,type", function(data) {
        for (var i = 0; i < data.length; i++) {
            $('#type').append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");//('loadData', dataJ);
        }
    });
});




function create() {
	showDialog($("#dialog"));
	richedit.html("");
	$("#form")[0].reset();
	$("#id").val("");
}

function update(id) {
	showDialog($("#dialog"));

	restGet(url + "/" + id, null, $('#form'), function(data) {
		richedit.html(data.description);
	});
}

function selectCount() {
	restSelect(url + "/count", {
		"studentId" : $("#studentId").val(),
		"keyword" : $("#keyword").val()
	}, function(count) {
		refreshPagination($("#pagination"), count, rows, select);
	});
}
function select(page) {
	restSelect(url, {
		"studentId" : $("#studentId").val(),
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

function fnClick(str) {
	if ($('#' + str).is(":checked") == true) {
		$('#' + str).val("1");
	} else {
		$('#' + str).val("2");
	}
}


</script>

<%@ include file="footer.jsp"%>