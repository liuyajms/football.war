<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header_dialog.jsp" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="weiboComment" bundle="${weiboComment}" />
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
		<div class="col-md-4">
			<div class="btn-group">
				<button type="button" class="btn btn-default" id="create" onclick="create()" style="display:none;">
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
				<th name="description">
					<fmt:message key="weiboComment.description" bundle="${weiboComment}" />
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
					<fmt:message key="weiboComment" bundle="${weiboComment}" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="description">
							<fmt:message key="weiboComment.description" bundle="${weiboComment}" />
						</label>
						<textarea class="form-control" id="description" name="description" style="width:100%;height:160px;"></textarea>
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
	var url = "../../rest/weiboComment";
	var rows = 10;
	var weiboid = <%=request.getParameter("id")%>;
	//var w = <%=request.getParameter("w")%>;alert(w);
	
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
					restInsert(url, {weiboId : weiboid}, $("#form"), function(data) {	
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
		restSelect(url + "/count?weiboId=" + weiboid, {"keyword" : $("#keyword").val()}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url + "?weiboId=" + weiboid, {"keyword" : $("#keyword").val(), "page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, []);
		});
	}
	
	function create(){
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#id").val("");
	}
	
	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {alert(data.description);
			console.log(data);
		});
	}
	
	function back() {
		//var w = <%=request.getParameter("w")%>;alert(0);
		//alert('w:'+w);
		//if (w == 1) {
			menu("weiboSchool.jsp");
		//} else (
		//	menu("weiboClasses.jsp");
		//)
	}
</script>
<%@ include file="../../footer.jsp" %>