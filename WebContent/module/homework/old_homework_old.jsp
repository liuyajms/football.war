<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp" %>
<%
/* 	cn.com.weixunyun.child.model.pojo.Teacher teacher = null;
	Cookie[] cookies = request.getCookies();
	String rsessionid = null;
	for (Cookie cookie : cookies) {
		if ("rsessionid".equals(cookie.getName())) {
			rsessionid = cookie.getValue();
		}
	}
	teacher = cn.com.weixunyun.child.Session.getInstance(rsessionid).get("teacher"); */
%>
<div class="easyui-layout">
	<div title="作业管理" style="padding: 2px; overflow: hidden;">
		<table id="user" title="作业管理" class="easyui-datagrid"
			style="width: auto; height: auto" toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="true"
			singleSelect="false">
			<thead>
				<tr>
					<th field="ck" checkbox="true"></th>
					<th field="courseName" width="50">科目</th>
					<th field="description" width="150">内容</th>
					<th field="updateTime" width="50" formatter="formatDate">更新时间</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
			 <a href="javascript:void(0)"
				class="easyui-linkbutton" iconCls="icon-add" plain="true"
				onclick="newUser()">添加</a> <a href="javascript:void(0)"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true"
				onclick="editUser()">修改 </a> <a href="javascript:void(0)"
				class="easyui-linkbutton" iconCls="icon-remove" plain="true"
				onclick="deleteUser()">删除 </a>

		</div>
		<div id="user_info" class="easyui-dialog" modal="true"
			style="width: 600px; 600px: auto; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post" novalidate>
				<input type="hidden" name="id" id="id" />
				<div class="fitem">
					<label>科目</label> 
					<input name="courseId" id="courseId" style="width:200px;" class="easyui-combobox" required="true"
						data-options="valueField:'id',textField:'text'" />
				</div>
				<div class="fitem">
					<label>班级</label> 
					<input class="easyui-combobox"  name="classesId" id="classesId" required="true"
											style="width:200px;"  data-options="valueField:'id',textField:'text'" />
				</div>
				<div class="fitem">
					<label class="des">内容</label>
       				<textarea name="description" id="description" style="width:350px;height:100px;"></textarea>

				</div>
			</form>
		</div>

		<div id="dlg" class="easyui-dialog" closed="true" title="提示信息"
			data-options="iconCls:'icon-save'"
			style="width:300px;height:100px;padding:10px">请选择一条数据进行修改！！</div>

		<div id="dlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-save" onclick="saveUser()">保存</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-no" onclick="deleteUser()" id="del">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-undo"
				onclick="javascript:$('#user_info').dialog('close')">取消</a>
		</div>
	</div>
		<script type="text/javascript">

			var url = "../../rest/homework";
			var action;
			
			$(document).ready(function(){
				queryCon();
				$.getJSON("rest/classes?classesName=testNameName", function(data){
					var dataJ = [];
					dataJ.push({"text":"--请选择--", "id":0});
					for(var i = 0; i < data.total; i ++){
						dataJ.push({"text":data.rows[i].name, "id":data.rows[i].id});
					}
					$('#classesId').combobox('loadData', dataJ);
					
				});
				
				$.getJSON("rest/course", function(data){
					var dataJ = [];
					dataJ.push({"text":"--请选择--", "id":0});
					for(var i = 0; i < data.total; i ++){
						dataJ.push({"text":data.rows[i].name, "id":data.rows[i].id});
					}
					$('#courseId').combobox('loadData', dataJ);
					
				});
			});

			function queryCon(){
				//restSelect($("#user"), url+"/session");
			}
					

			//新增
			function newUser() {
				$("#del").hide();
				$('#user_info').dialog('open').dialog('setTitle', '作业-添加');
				$('#fm').form('clear');
				action = "add";
			}

			//作业的修改方法,每次只允许修改一条作业
			function editUser() {
				$("#del").hide();
				var row = $('#user').datagrid('getSelections');
				var des = row[0].description;
				var de = [];
				de = des.split("<br/>");
				var descrip = "";
				for(var i = 0; i < de.length; i ++){
					descrip += de[i] + "\n";
				}
				if (row.length == 1) {
					$('#user_info').dialog('open').dialog('setTitle', '作业-修改');
					$('#fm').form('load', row[0]);
					$('#classesIdName').combobox('setValue', row[0].classId);
					$('#description').val(descrip);
				} else if (row.length != 0) {
					$("#dlg").dialog('open');
				}
				action = "update";
			}

			//保存方法，如果id为空的话，则为新增，如果不为空的话，则是修改
			function saveUser() {
				if ($("#id").val() == "") {
					restInsert(url, null, $("#fm"), function(data) {
						queryCon();
					});
				} else {
					restUpdate(url + "/" + $("#id").val(), null, $("#fm"), function(data) {
						queryCon();
					});
				}
				$('#user_info').dialog('close'); // close the dialog
			}

			//删除作业,可以批量删除
			function deleteUser() {
				var id = 0;
				var row = $('#user').datagrid('getSelections');
				if (row.length > 0) {
					$.messager.confirm('删除', '确定删除？', function(r) {
						if (r) {
							for ( var i = 0; i < row.length; i++) {
								id = row[i].id;
								$.ajax({
									type : "DELETE",
									url : url + "/" + id,
									dataType : "json",
									success : function() {
										$('#user_info').dialog('close'); // close the dialog
										$($('#user').datagrid('getPager')).pagination('select');
									}
								});
							}
						}

					});
				}

			}
		</script>
		</div>

<jsp:include page="../../footer.jsp" />