<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="student" />
		</h3>
	</div>
	<table id="studentSummaryTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th name="name" style="width: 300px;"><fmt:message key="multi.name" /></th>
				<th name="count"><fmt:message key="multi.count" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="parent" />
		</h3>
	</div>
	<table id="parentsSummaryTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 50px;"><fmt:message key="operation" /></th>
				<th name="name" style="width: 300px;"><fmt:message key="multi.name" /></th>
				<th name="count"><fmt:message key="multi.count" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<div class="modal fade" id="studentDialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="studentTitle">
					<fmt:message key="student" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="studentForm" method="post">
					<table id="studentTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
								<th name="name"><fmt:message key="student.name" /></th>
								<th name="code"><fmt:message key="student.code" /></th>
								<th name="card"><fmt:message key="student.card" /></th>
								<th name="gender_name"><fmt:message key="student.gender" /></th>
								<th name="birthday"><fmt:message key="student.birthday" /></th>
								<th name="classes_name"><fmt:message key="student.class" /></th>
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
				<button type="button" class="btn btn-primary" id="studentSubmit" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="parentsDialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="parentsTitle">
					<fmt:message key="parent" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="parentsForm" method="post">
					<table id="parentsTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
								<th name="name"><fmt:message key="parent.name" /></th>
								<th name="username"><fmt:message key="parent.username" /></th>
								<th name="student_name" style="color: #999;"><fmt:message key="parent.student" /></th>
								<th name="code" style="color: #999;"><fmt:message key="student.code" /></th>
								<th name="card" style="color: #999;"><fmt:message key="student.card" /></th>
								<th name="gender_name" style="color: #999;"><fmt:message key="student.gender" /></th>
								<th name="birthday" style="color: #999;"><fmt:message key="student.birthday" /></th>
								<th name="classes_name" style="color: #999;"><fmt:message key="student.class" /></th>
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
				<button type="button" class="btn btn-primary" id="parentsSubmit" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		initRowCheck($("#studentTable"));
		initRowCheck($("#parentsTable"));

		$("#studentSubmit").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				var button = $(this);
				button.button('loading');
				restUpdate("rest/multi/student", null, $("#studentForm"), function(data) {
					button.button('reset');
					hideDialog($('#studentDialog'));
					selectStudent();
				});
			}
		});
		$("#parentsSubmit").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				var button = $(this);
				button.button('loading');
				restUpdate("rest/multi/parents", null, $("#parentsForm"), function(data) {
					button.button('reset');
					hideDialog($('#parentsDialog'));
					selectParents();
				});
			}
		});
		
		selectStudent();
		selectParents();
	});

	function selectStudent() {
		restSelect("rest/multi/student/summary", null, function(json) {
			refreshTable($("#studentSummaryTable"), json, [ {
				name : '<fmt:message key="view" />',
				func : "student"
			} ], [ "name" ], false);
		});
	}
	function selectParents() {
		restSelect("rest/multi/parents/summary", null, function(json) {
			refreshTable($("#parentsSummaryTable"), json, [ {
				name : '<fmt:message key="view" />',
				func : "parents"
			} ], [ "name" ], false);
		});
	}
	function student(name) {
		showDialog($("#studentDialog"));
		restSelect("rest/multi/student", {
			"name" : name
		}, function(json) {
			refreshTable($("#studentTable"), json);
		});
	}
	function parents(name) {
		showDialog($("#parentsDialog"));
		restSelect("rest/multi/parents", {
			"name" : name
		}, function(json) {
			refreshTable($("#parentsTable"), json);
		});
	}
</script>

<%@ include file="footer.jsp"%>