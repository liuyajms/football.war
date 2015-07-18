<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header_dialog.jsp" %>	
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title"><span id="security-title">
			<fmt:message key="studentSecurity" />
			</span>
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
		<div class="col-md-4">
			<div class="btn-group">
				<button type="button" class="btn btn-default" id="export">
					<fmt:message key="export" />
				</button>
			</div>
		</div>
		<div class="col-md-4">
			<ul class="pagination" id="pagination" style="margin:0px;"></ul>
		</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead >
			<tr>
				<th style="width:30px;"><input type="checkbox" class="rows"/></th>
				<th name="time" type="timestamp"><fmt:message key="time" /></th>
				<th name="deviceName"><fmt:message key="studentSecurity.door" /></th>
				<th name="reach==true?'<fmt:message key="reach" />':'<fmt:message key="leave" />'">
					<fmt:message key="reachOrLeave" />
				</th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<script type="text/javascript">
	var url = "rest/security/record";
	var rows = 10;
	
	var studentId = <%=request.getParameter("studentId")%>;
	var classesId = <%=request.getParameter("classesId")%>;
	
	$(document).ready(function() {
		initRowCheck($("#table"));
		
		$("#select").click(function() {
			selectCount();
		});
		
		$("#export").click(function() {
			window.location = url + "/export?userId=" + studentId;
		});
		
		$.getJSON("rest/student/"+studentId, function(data) {
					$("#security-title").text(data.name+"-安全监管");
				});
		
		selectCount();
		
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');
				richedit.sync();
				if ($("#id").val() == "") {
					restInsert(url, {classesId : classesId}, $("#form"), function(data) {	
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
	});
	
	function selectCount() {
		restSelect(url + "/count", {userId : studentId, "keyword" : $("#keyword").val()}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {userId : studentId, "keyword" : $("#keyword").val(), "page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, []);
		});
	}
	
	function back() {
		menu("student.jsp?classesId=" + classesId);
	}
	
</script>
<%@ include file="footer.jsp" %>