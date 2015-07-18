<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<div class="panel panel-default" >
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="dictionary" />
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
		</div>
		<div class="col-md-4">
			<ul class="pagination" id="pagination" style="margin:0px;"></ul>
		</div>
		<div class="col-md-4">
			<div class="input-group">
				<select class="combobox form-control" id="keyword">
					<option></option>
				</select>
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
				<th name="code"><fmt:message key="dictionary.code" /></th>
				<th name="name"><fmt:message key="dictionary.name" /></th>
				<th name="ord" ><fmt:message key="dictionary.ord" /></th>
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
					<fmt:message key="dictionary" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" >
					<input type="hidden" name="schoolId" id="schoolId"/>
					<input type="hidden" name="dictionaryTableCode" id="dictionaryTableCode"/>
					<input type="hidden" name="dictionaryFieldCode" id="dictionaryFieldCode"/>
					
					<div class="form-group">
						<label for="title">
							<fmt:message key="dictionary.code" />
						</label>
						<input type="text" class="form-control" id="code" name="code" required="true" />
					</div>
					
					<div class="form-group">
						<label for="description">
							<fmt:message key="dictionary.name" />
						</label>
						<input type="text" class="form-control" id="name" name="name" required="true" />
					</div>
					
					<div class="form-group">
						<label for="description">
							<fmt:message key="dictionary.ord" />
						</label>
						<input type="text" class="form-control" id="ord" name="ord" required="true" />
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
	var url = "rest/value";
	var rows = 10;
	var key, oldCode;
	key = '<%=request.getParameter("keyword")%>';
	
	$(document).ready(function(){
		getJson();
		selectCount();
		
		$("#select").click(function() {
			key = $("#keyword").val();
			selectCount();
		});
		
		$("#remove").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				$.each($("input[name=code]:checked"), function(i, n) {
					restDelete(url + "/" + $(n).val() + "/"+key.split(",")[0]+"."+key.split(",")[1], function() {
					});
				});
				alert('<fmt:message key="operate.success" />');
				selectCount();
			}
		});
		
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');
				if ($("#schoolId").val() == "") {
					restInsert(url, {"keyword" : key}, $("#form"), function(data) {	
						button.button('reset');
						hideDialog($("#dialog"));
						selectCount();
						
					});
				} else {
					restUpdate(url + "/" + $("#code").val(), {"keyword" : key, "oldCode":oldCode}, $("#form"), function(data) {
						button.button('reset');
						hideDialog($('#dialog'));
						selectCount();
					});
				}
			});

		});
	
	});

	function selectCount() {
		restSelect(url + "/list/count", {"keyword" : key}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}

	function select(page) {
		restSelect(url + "/list", {"keyword" : key, "page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, [{name: '<fmt:message key="update" />', func: "update"}], ["code"]);
		});
	}
	
	//加载数据字典-表列表
	function getJson(){
		$.getJSON("rest/field", function(data){
			for(var i = 0; i < data.length; i ++){
				$('#keyword').append("<option value='" + data[i].dictionaryTableCode + "," + data[i].code + "'>" +
						 data[i].dictionaryTableName+ "_" + data[i].name + "</option>");
			}
			$("#keyword").val(key);
		});
	}
	
	function create(){
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#schoolId").val('');
	}
	
	function update(code) {
		showDialog($("#dialog"));
		restGet(url, {"tableCode" : key.split(",")[0], "fieldCode":key.split(",")[1], "code":code}, $('#form'), function(data) {
			return ;
		});
		oldCode = code;
	}
	
	function show(fieldCode, tableCode){
		restSelect(url + "/list", {"tableCode" : tableCode, "fieldCode": fieldCode, "page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, [{name: '<fmt:message key="update" />', func: "update"}], ["code"]);
		});
	}
	</script>
	</div>

<%@ include file="footer.jsp" %>