<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />
<div class="easyui-layout">
	<div title="<fmt:message key="performance" />" style="padding: 2px; overflow: hidden;">
		<table id="user" title="<fmt:message key="performance" />" class="easyui-datagrid"
			style="width: auto; height: auto" toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="true"
			singleSelect="false">
			<thead>
				<tr>
					<th field="ck" checkbox="true"></th>
					<th field="teacherName" width="20"><fmt:message key="teacher" /></th>
					<th field="studentName" width="20"><fmt:message key="student" /></th>
					<th field="description" width="50"><fmt:message key="description" /></th>
					<th field="createTime" formatter="formatDate" width="50"><fmt:message key="time" /></th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
			<input name="condition" id="condition" class="easyui-validatebox" />
			<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
				onclick="queryCon()"> <fmt:message key="search" /> </a> <a href="javascript:void(0)"
				class="easyui-linkbutton" iconCls="icon-add" plain="true"
				onclick="newUser()"><fmt:message key="create" /></a> <a href="javascript:void(0)"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true"
				onclick="editUser()"><fmt:message key="update" /></a> <a href="javascript:void(0)"
				class="easyui-linkbutton" iconCls="icon-remove" plain="true"
				onclick="deleteSome()"><fmt:message key="remove" /> </a>
		</div>
		<div id="user_info" class="easyui-dialog" modal="true"
			style="width: 820px; height: auto; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post" novalidate>
				<input type="hidden" name="id" id="id" /> 
				<input type="hidden" name="schoolId" id="schoolId" />
				<table>
					<tr>
						<td align="right"><label>
							<fmt:message key="student0" />
						&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="student1" /></label></td>
						<td><input class="easyui-combobox" id="studentId"
							name="studentId" required="true"
							data-options="valueField:'id',textField:'text'"
							style="width: 250px; height: 25px;" /></td>
					</tr>
					<tr>
						<td align="right"><label>
						<fmt:message key="description0" />
						&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="description1" /></label></td>
						<td><textarea name="description" id="description"
								style="height: 260px; width: 600px;"></textarea></td>
					</tr>

					<tr>
						<td align="right"><label ><fmt:message key="info" /></label><br />
						<label><fmt:message key="info.tip" /></label>
						</td>
						<td id="idDesc">
							<ul class="descLiClass">
							</ul>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-save" onclick="saveUser()">
				<fmt:message key="submit" />	
			</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-undo"
				onclick="javascript:$('#user_info').dialog('close')">
				<fmt:message key="cancel" />	
			</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	var url = "rest/performance";
	var title = "<fmt:message key="performance" />";
	var length = 0;

	$(document).ready(function() {
		queryCon();
		
	});
	
	function getPerfor(){
		$(".descLiClass").html("");
		$.getJSON("rest/value/list?tableCode=t001&fieldCode=performance", function(data){
			length = data.length;
			for(var i = 0; i < length; i ++){
				var aLabel = "<li><a href='javascript:void(0)' id='aId" + i + "'style='color:black;' onclick='fnGetDes(" + i + ")'>" + data[i].name + "</a></li>";
				$(".descLiClass").append(aLabel);
			}
			
			$(".descLiClass li").bind("mousemove", function(){
				$(this).attr("style", "text-decoration:underline");
			});
			$(".descLiClass li").bind("mouseout", function(){
				$(this).attr("style", "text-decoration:none");
			});
			
		});
	}
	
	
	function fnGetDes(i) {
		var record = $("#description").val();
		if (record == "") {
			$("#description").val($("#aId" + i).html());
		} else {
			$("#description").val(record + "," + $("#aId" + i).html());
		}

	}
	
	//加载下拉列表框学生数据
	function getSD(){
		$.getJSON("rest/student?getList=0", function(data) {
			var datas = [];
			datas.push({
				"text" : "--<fmt:message key="option" />--",
				"id" : 0
			});
			for (var i = 0; i < data.rows.length; i++) {
				datas.push({
					"text" : data.rows[i].name,
					"id" : data.rows[i].id
				});
			}
			$('#studentId').combobox('loadData', datas);
		});
	}

	function newUser() {
		$("#del").hide();
		$("#user_info").dialog("open").dialog("setTitle", title + " - <fmt:message key="create" />");
		$("#fm").form("clear");

		getPerfor();
		getSD();
	}
	function editUser() {
		var rows = $("#user").datagrid("getSelections");
		if (rows.length > 1) {
			$.messager.alert('<fmt:message key="tip" />', '<fmt:message key="performance.tip1" />');
		} else {
			var row = rows[0];
			if (row) {
				$("#user_info").dialog("open").dialog("setTitle", title + " - <fmt:message key="update" />");
				onloading($('#fm'));
				restGet(url + "/" + row.id, null, $('#fm'), function(data){	
					getSD();
				});
				
			}
		}

		getPerfor();
	}
	function saveUser() {
		if ($("#id").val() == "") {
			onloading($('#fm'));
			restInsert(url, null, $("#fm"), function(data) {
				removeload();
				alert("<fmt:message key="add.success" />");
				$("#user_info").dialog("close");
				queryCon();
			});
		} else {
			onloading($('#fm'));
			restUpdate(url + "/" + $("#id").val(), null, $("#fm"), function() {
				removeload();
				alert("<fmt:message key="operate.success" />");
				$("#user_info").dialog("close");
				queryCon();
			});
		}
	}

	function deleteSome() {
		var id = 0;
		var row = $("#user").datagrid("getSelections");
		if (row.length > 0) {
			$.messager.confirm("<fmt:message key="remove" />", '<fmt:message key="remove.confirm" />', function(r) {
				onloading($('body'));
				if (r) {
					for (var i = 0; i < row.length; i++) {
						restDelete(url + "/" + row[i].id, null);
					}
					removeload();
					queryCon();
				}
			});
		}
	}

	//搜索按钮方法
	function queryCon() {
		var con = $("#condition").val();
		onloading($('body'));
		restSelect($('#user'), url, {con : con});
		removeload();
	}
</script>

<jsp:include page="footer.jsp" />
