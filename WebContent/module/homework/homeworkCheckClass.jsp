<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp" %>	
<%
	cn.com.weixunyun.child.model.pojo.Teacher teacher = null;
	Cookie[] cookies = request.getCookies();
	String rsessionid = null;
	for (Cookie cookie : cookies) {
		if ("rsessionid".equals(cookie.getName())) {
			rsessionid = cookie.getValue();
		}
	}
	teacher = cn.com.weixunyun.child.Session.getInstance(rsessionid).get("teacher");
%>
<jsp:include page="../../header.jsp" />
<div class="easyui-layout">
	<div title="作业查看" style="padding: 2px; overflow: hidden;">
		<table id="user" title="作业查看" class="easyui-datagrid"
			style="width: auto; height: auto;"  toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="true"
			singleSelect="true">
			<thead>
				<tr>
					<th field="name" width="50">姓名</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<div title="作业查看" modal="true" id="divStudent" class="easyui-dialog" style="width: 600px; 600px: auto;top:240px;" closed="true">
		<table id="userStudent" class="easyui-datagrid"
			pagination="false" rownumbers="true" fitColumns="true"
			singleSelect="true">
			<thead>
				<tr>
					<th field="course">科目</th>
					<th field="desHomework">内容</th>
					<th field="complate" formatter="ttrans">是否完成</th>
					<th field="beginTime">开始时间</th>
					<th field="endTime">完成时间</th>
				</tr>
			</thead>
		</table>
		
		
	</div>
		
	<div id="toolbar">
		<label>条件：</label>
		<input class="easyui-combobox" name="condition" id="condition" style="width:100px;"
			data-options="valueField:'id',textField:'text'" />
		<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
			onclick="queryCon()">搜索</a>
	</div>
		
		<script type="text/javascript">
			$(document).ready(function(){
				queryCon();
				$.getJSON("/child/rest/student/0?teacherId=" + <%=teacher.getId()%>, function(data){
					var dataJ = [];
					dataJ.push({"text":"--全班--", "id":0});
					for(var i = 0; i < data.total; i ++){
						dataJ.push({"text":data.rows[i].name, "id":data.rows[i].id});
					}
					$('#condition').combobox('loadData', dataJ);
					$('#condition').combobox({
						onSelect: function(){
							queryCon();
						}
					});
				});
				
			});
		
			function ttrans(value, row, index){
				console.log(row.complete);
				if(row.complete == true){
					return '是';
				}else{
					return '否';
				}
			}

			var url = "rest/homeworkcomplete";
			var action;

			function queryCon(){
				var value = $('#condition').combobox("getValue");
				if(value == ""){
					value = 0;
				}
				var p = $('#user').datagrid('getPager');
				$(p).pagination({
					onSelectPage : function(pageNumber, pageSize) {
						var pagination = $(this);
						pagination.pagination('loading');
						$.getJSON(url + "?page=" + pageNumber + "&rows=" + pageSize + "&sId=" + value, function(data) {
							$('#user').datagrid("loadData", data);
							pagination.pagination('loaded');
						});
					}
				});
				$(p).pagination("select");
			}
			
			//行点击事件,点击某个学生查看具体的家庭作业完成情况
			$("#user").datagrid({
				onClickRow: function(index, data){
					var stuId = data.id;
					$.getJSON(url + "?sId=" + stuId, function(data) {
						$('#userStudent').datagrid("loadData", data);
						
					});
					$('#divStudent').dialog('open').dialog('setTitle', '作业查看');
				}
					
			});

		</script>
		</div>

<jsp:include page="../../footer.jsp" />