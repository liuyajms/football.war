<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header_dialog.jsp" %>	
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="schoolPoint.title" />
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
			<div class="btn-group">
			</div>
		</div>
		<div class="col-md-4">
			<ul class="pagination" id="pagination" style="margin:0px;"></ul>
		</div>
		<div class="col-md-4">
			<div class="input-group">
				<input type="text" class="form-control" id="keyword" />
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
		<thead >
			<tr>
				<th style="width:30px;"><input type="checkbox" class="rows"/></th>
				<th style="width:50px;"><fmt:message key="operation" /></th>
				<th name="name"><fmt:message key="teacher.name" /></th>
				<th name="mobile"><fmt:message key="phone" /></th>
				<th name="point"><fmt:message key="point" /></th>
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
					<fmt:message key="schoolPoint.title1" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />
					
					<div class="form-group">
						<label for="name">
							<fmt:message key="teacher.name" />
						</label>
						<input type="text" class="form-control" id="name" name="name" readonly />
					</div>
					
					<div class="form-group">
						<label for="point">
							<fmt:message key="point" />
						</label>
						<input type="number" class="form-control" id="point" name="point" readonly />
					</div>
					
					<div class="form-group">
						<label for="point">
							<fmt:message key="schoolPoint.title1" />
						</label>
						<input type="number" class="form-control" id="score" name="score"  />
					</div>
					
					<div class="form-group">
						<label for="remark">
							<fmt:message key="schoolPoint.label1" />
						</label>
						<input type="text" class="form-control" id="desc" name="desc" style="height:70px;" />
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

	var url = "rest/teacher";
	var rows = 10;
	var schoolId = <%=request.getParameter("schoolId")%>;

	$(document).ready(function() {
		initRowCheck($("#table"));
		selectCount();
		
		$("#export").click(function() {
			window.location = url + "/export/point?schoolId=" + schoolId + "&keyword=" + $("#keyword").val();
		});
		
		$("#select").click(function() {
			selectCount();
		});
		
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');
				
				if ($("#score").val() == "" || $("#score").val() == 0) {
					alert("<fmt:message key="schoolPoint.tip" />");
					button.button('reset');
					return ;
				}
				
				restUpdate("rest/point/" + $("#id").val(), null, $("#form"), function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
					selectCount();
				});
			});

		});
	});
	
	function selectCount() {
		restSelect("rest/teacher/all/count", {"keyword" : $("#keyword").val(), "schoolId" : schoolId}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect("rest/teacher/all", {"keyword" : $("#keyword").val(), "schoolId" : schoolId, "page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, [{name: '<fmt:message key="schoolPoint.exchange" />', func: "update"}]);
		});
	}
	
	function create(){
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#id").val("");
	}
	
	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			if(data.available) {
				$("#available").attr("checked", "checked");
				$("#available").val("1");
			}
		});
	}
	
</script>
</div>

<%@ include file="footer.jsp" %>
