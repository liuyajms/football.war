<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="broadcast.school" />
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
				<th style="width: 80px;"><fmt:message key="broadcast.remark" bundle="${broadcast}" /></th>
				<th name="title" style="width:200px;">
					<fmt:message key="broadcast.title" bundle="${broadcast}" />
				</th>
				<th name="description">
					<fmt:message key="broadcast.description" bundle="${broadcast}" />
				</th>
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
					<th style="width: 80px;"><fmt:message key="broadcast.school" bundle="${broadcast}" /></th>
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="title">
							<fmt:message key="broadcast.title" bundle="${broadcast}" />
						</label>
						<input type="text" class="form-control" id="title" name="title" required="true" />
					</div>
					<div class="form-group">
						<label for="description">
							<fmt:message key="broadcast.description" bundle="${broadcast}" />
						</label>
						<textarea class="form-control" id="description" name="description" style="width:100%;height:160px;"></textarea>
					</div>
					
					<div class="form-group">
						<label for="pic">
							<fmt:message key="broadcast.pic" bundle="${broadcast}" />
						</label>
						<input type="file" id="pic" name="pic" multiple="true"/>
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
	var url = "../../rest/broadcast";
	var rows = 10;
	
	$(document).ready(function(){
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
	});
	
	function selectCount() {
		restSelect(url + "/school/count", {"keyword" : $("#keyword").val()}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url + "/school", {"keyword" : $("#keyword").val(), "page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, [ {
				name : '<fmt:message key="update" />',
				func : "update"
			}, {
				name : '<fmt:message key="broadcast.remark" bundle="${broadcast}" />',
				func : "viewComment"
			} ]);
		});
	}
	
	function viewComment(id){
		menu("broadcastSchoolComment.jsp?t=1&id=" + id);
	}
	
	function create(){
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#id").val("");
	}
	
	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {
			return ;
		});
	}
</script>
<%@ include file="../../footer.jsp" %>