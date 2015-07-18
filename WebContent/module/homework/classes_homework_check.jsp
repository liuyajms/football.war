<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="homework" bundle="${homework}" />
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
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
					<input id="dateBegin" type="text" data-date-format="yyyy-mm-dd" class="form-control"
									placeholder="<fmt:message key="homework.date0" bundle="${homework}" />" />
					<input id="dateEnd" type="text" data-date-format="yyyy-mm-dd" class="form-control"
									placeholder="<fmt:message key="homework.date1" bundle="${homework}" />" />
					<span class="input-group-btn">
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
				<th name="classesName" style="width: 80px;"><fmt:message key="classes"  /></th>
				<th name="studentName" style="width: 80px;"><fmt:message key="homework.studentName" bundle="${homework}" /></th>
				<th name="createTime" type="timestamp" style="width: 120px;"><fmt:message key="homework.createTime" bundle="${homework}" /></th>
				<th name="checked==true? '<fmt:message key="homework.check0" bundle="${homework}" />':''" style="width: 50px;">
					<fmt:message key="homework.check1" bundle="${homework}" />
				</th>
				<th name="description"  style="width: 120px;"><fmt:message key="homework.description" bundle="${homework}" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<script type="text/javascript"
	src="../../asset/datepicker/bootstrap-datepicker.js"></script>

<script type="text/javascript">
	var title = "<fmt:message key="homework" bundle="${homework}" />";
	var url = "../../rest/homeworkCheck";
	var rows = 10;
	var richedit;
	var lastErrors;

$(document).ready(function() {
	title = $("#title").html();
	initRowCheck($("#table"));
	
	initDatepicker(); //初始化时间选择框

	$("#export").click(function() {
		window.location = url + "/classes/export?classesId=" + $("#classesId").val()  + "&dateBegin=" + $("#dateBegin").val() 
			+ "&dateEnd=" + $("#dateEnd").val();
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
	
	selectCount();

});

/**
 * 初始化时间选择器
 */
function initDatepicker() {
	$('#dateBegin').add('#date_custom').datepicker({
		inputMask : true
	}).on('changeDate', function(ev) {
		$('.datepicker').hide();
	});
	$('#dateEnd').add('#date_custom').datepicker({
		inputMask : true
	}).on('changeDate', function(ev) {
		$('.datepicker').hide();
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
		
	});
}

function selectCount() {
	restSelect(url + "/classes/count", {
		"classesId" : $("#classesId").val(),
		"dateBegin" : $("#dateBegin").val(),
		"dateEnd" : $("#dateEnd").val()
	}, function(count) {
		refreshPagination($("#pagination"), count, rows, select);
	});
}
function select(page) {
	restSelect(url + "/classes", {
		"classesId" : $("#classesId").val(),
		"dateBegin" : $("#dateBegin").val(),
		"dateEnd" : $("#dateEnd").val(),
		"page" : page,
		"rows" : rows
	}, function(json) {
		refreshTable($("#table"), json, [ ]);
	});
}

</script>

<%@ include file="../../footer.jsp"%>