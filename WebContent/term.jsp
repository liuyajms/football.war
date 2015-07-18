<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>	

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="term.manage"  />
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
				<th name="code" style="width:100px;"><fmt:message key="term.code" /></th>
				<th name="year"><fmt:message key="term.year" /></th>
				<th name="term" style="width:500px;"><fmt:message key="term" /></th>
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
					<fmt:message key="term.manage" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" />
					<div class="form-group">
						<label for="year">
							<fmt:message key="term.year" />
						</label>
						<select class="combobox form-control" id="year" name="year" required="true" onchange="fnChange()">
							<option></option>
						</select>
					</div>
					
					<div class="form-group">
						<label for="term">
							<fmt:message key="term" />
						</label>
						
						<div class="controls">  
		                    <label class="radio inline">
								<input type="radio" name="term" id="term1" value="1" onchange="fnChange()" />  
									<fmt:message key="term1" />
							</label>  
							<label class="radio inline">  
								<input type="radio" name="term" id="term2" value="2" onchange="fnChange()" />
									<fmt:message key="term2" />
							</label>
		                </div>  
						
						
					</div>
					
					<div class="form-group">
						<label for="code"><fmt:message key="term.code" /></label>
						<input type="text" class="form-control" id="code" name="code" required="true"/>
						<input type="hidden" class="form-control" id="codeOld" name="codeOld" required="true"/>
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
	
	var url = "rest/term";
	var action;
	var rows = 10;
	
	$(document).ready(function(){
		
		initRowCheck($("#table"));
		
		//加载班级届数
		setYear();
		selectCount();
		
		//删除数据
		$("#remove").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				$.each($("input[name=code]:checked"), function(i, n) {
					restDelete(url + "?code=" + $(n).val(), function() {
					});
				});
				selectCount();
			}
		});
		
		//查询按钮
		$("#select").click(function() {
			selectCount();
		});
		
		//提交按钮
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');

				if ($("#code").val() == "") {
					restInsert(url, null, $("#form"), function(data) {	
						button.button('reset');
						hideDialog($("#dialog"));
						selectCount();
					});
				} else {
					alert('codeOld:'+$("#codeOld").val());
					restUpdate(url + "/" + $("#code").val()+'?codeOld=' + $("#codeOld").val(), null, $("#form"), function(data) {
						button.button('reset');
						hideDialog($('#dialog'));
						selectCount();
					});
				}
			});

		});
	});
	
	function selectCount() {
		restSelect(url + "/count", {"keyword" : $("#keyword").val()}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {"classesName" : $("#keyword").val(), "page" : page, "rows" : rows}, function(json) {
			refreshTable($("#table"), json, [{name: '<fmt:message key="update" />', func: "update"}], ["code"]);
		});
	}
	
	function update(code) {
		showDialog($("#dialog"));

		restGet(url + "/" + code, null, $('#form'), function(data) {alert(data.term);
			$("#codeOld").val($("#code").val());
			if(data.term == 1) {
				$("#term1").attr("checked", "checked");
				$("#term1").val("1");
				$("#term2").val("0");
			} else {
				$("#term2").attr("checked", "checked");
				$("#term2").val("2");
				$("#term1").val("0");
			}
		});
		
	}
	
	function fnChange(){
		alert($('#term1').attr('checked'));
		if ($('#term1').attr('checked') == 'checked') {
			alert(1);
			$('#code').val($('#year').val() + "" + $('#term1').val() + "");
		} else {alert(2);
			$('#code').val($('#year').val() + "" + $('#term2').val() + "");
		}
	}
	
	function create() {
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$("#code").val("");
	}
	
	function setYear() {
		var dateY = new Date().getFullYear();
		
		for(var i = 20; i > 0; i -- ){
			
			$('#year').append("<option value=" + (dateY - i) + ">" + (dateY - i) + "</option>");
		}
		for(var i = 0; i < 21; i ++ ){
			if(i == 6){
				$('#year').append("<option value=" + (dateY + i) + " selected='true'" + ">" + (dateY + i) + "</option>");
			}else{
				$('#year').append("<option value=" + (dateY + i) + ">" + (dateY + i) + "</option>");
			}
		}
	}
	
	//教师、课程管理
	function addTeacher(id){
		menu("courseTeacher.jsp?classesId=" + id);
	}
	
	//学生管理	
	function studentM(id) {
		menu("student.jsp?classesId=" + id);
	}
</script>
<%@ include file="footer.jsp" %>