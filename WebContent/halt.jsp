<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="halt" />
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
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead >
			<tr>
				<th style="width:30px;"><input type="checkbox" class="rows"/></th>
				<th style="width:50px;"><fmt:message key="operation" /></th>
				<th name="time" type="date"><fmt:message key="halt.time" /></th>
				<th name="type==1?'<fmt:message key="halt.type1" />':type==2?'<fmt:message key="halt.type2" />':'<fmt:message key="halt.type3" />'" >
					<fmt:message key="halt.type" />
				</th>
				<th name="description"><fmt:message key="halt.description" /></th>
				<th name="available==true?'<fmt:message key="halt.available1" />':'<fmt:message key="halt.available2" />'">
					<fmt:message key="halt.available1" />
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
					<fmt:message key="halt.set" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />
					<input type="hidden" name="schoolId" id="schoolId" />
					
					<div class="form-group">
						<label for="time">
							<fmt:message key="halt.time" />
							<fmt:message key="required" />
						</label>
						<input size="16" type="text" name="time" id="time" readonly="true" class="form_datetime" />
					</div>
					
					<div class="form-group">
						<label for="type">
							<fmt:message key="halt.type" />
						</label>
						<select class="combobox" id="type" name="type" required="true">
							<option value="1"><fmt:message key="halt.type1" /></option>
							<option value="2"><fmt:message key="halt.type2" /></option>
							<option value="3"><fmt:message key="halt.type3" /></option>
						</select>
					</div>
					
					<div class="checkbox">
						<label for="available">
							<fmt:message key="halt.available" />
						</label>
						<input type="checkbox" name="available" id="available" value="2" />
					</div>
					
					<div class="form-group">
						<label for="description">
							<fmt:message key="halt.description" />
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
	var url = "rest/halt";
	var rows = 10;
	
	$(document).ready(function(){
		initRowCheck($("#table"));
		selectCount();
		$(".form_datetime").datetimepicker({format: 'yyyy-mm-dd'});
		
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
		restSelect(url + "/count", null, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	
	function select(page) {
		restSelect(url, {"page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, [{name: '<fmt:message key="update" />', func: "update"}]);
		});
	}
	
	function create(){
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#id").val("");
	}
	
	function update(id) {
		showDialog($("#dialog"));

		restGet(url + "/" + id, null, $('#form'), function(data) {//alert(data);alert(data.available);
			if(data.available) {
				$("#available").attr("checked", "checked");
				$("#available").val("1");
			}
			return ;
		});
	}
</script>

<%@ include file="footer.jsp" %>