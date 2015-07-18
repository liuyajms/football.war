<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />  
<div class="easyui-layout">
	<div title="<fmt:message key="phrase.title" />" style="padding: 2px; overflow: hidden;">
		<table id="user" title="<fmt:message key="phrase.title" />" class="easyui-datagrid" 
			style="width: auto; height: auto" toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="true"
			singleSelect="true">
			<thead>
				<tr>
					<th field="id" width="20"><fmt:message key="phrase.id" /></th>
					<th field="description" width="50"><fmt:message key="phrase.description" /></th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" onclick="newUser()"><fmt:message key="create" /></a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" onclick="editUser()"><fmt:message key="update" /> </a> 
		</div>
		<div id="user_info" class="easyui-dialog"
			style="width: 500px; 600px: auto; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post" novalidate>
				<input type="hidden" name="id" id="id" />
				<input type="hidden" name="schoolId" id="schoolId" />
				<div class="fitem">
					<label><fmt:message key="phrase.description" /></label> 
					<textarea name="description" id="description" style="height:60px;width:300px;"></textarea>
				</div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-save" onclick="saveUser()"><fmt:message key="submit" /></a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-no" onclick="deleteUser()" id="del">
				<fmt:message key="remove" />	
			</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-undo" onclick="javascript:$('#user_info').dialog('close')">
				<fmt:message key="cancel" />	
			</a>
		</div>
	</div>
</div>
<script type="text/javascript">

var url = "/child/rest/phrase";
var title = "<fmt:message key="phrase.title0" />";

$(document).ready(function() {
	restSelect($("#user"), url);
});

function newUser() {
	$("#del").hide();
	$("#user_info").dialog("open").dialog("setTitle", title + " - <fmt:message key="create" />");
	$("#fm").form("clear");
}
function editUser() {
	$("#del").show();
	var row = $("#user").datagrid("getSelected");
    if (row) {
    	$("#user_info").dialog("open").dialog("setTitle", title + " - <fmt:message key="update" />	");
		$("#fm").form("load", row);
    }
}
function saveUser() {
	if ($("#id").val() == "") {
		restInsert(url, null, $("#fm"), function(data) {
			;
		});
	} else {
		restUpdate(url + "/" + $("#id").val(), null, $("#fm"), function() {
		});
	}
	$("#user_info").dialog("close");
	restSelect($("#user"));
}
function deleteUser(){
	$.messager.confirm("<fmt:message key="remove" />","<fmt:message key="remove.confirm" />",function(r) {
		if (r) {
			restDelete(url + "/" + $("#id").val(), function() {
			});
			$("#user_info").dialog("close");
			restSelect($("#user"));
		}
	});
}

</script>

<jsp:include page="footer.jsp" />
