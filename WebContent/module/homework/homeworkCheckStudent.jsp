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
	<div title="作业查看" id="divStudent" style="padding: 2px; overflow: hidden;">
		<table id="userStudent" title="【今天家庭作业完成情况】" class="easyui-datagrid"
			style="width: auto; height: auto; " toolbar="#toolbar"
			rownumbers="true" fitColumns="true"
			singleSelect="false">
			<thead>
				<tr>
					<th field="course" width="10">科目</th>
					<th field="description" id="description" width="150">内容</th>
					<th field="complete" id="complete" width="20" align="center" formatter="ttrans">是否完成</th>
					<th field="beginTime" width="20" editor="text" >开始时间</th>
					<th field="endTime" width="20" editor="text">完成时间</th>
				</tr>
			</thead>
		</table>
		<div style="padding: 2px; overflow: hidden;">
			<label class="des">评价</label>
       		<textarea name="evaluate" id="evaluate"  style="height:80px;"></textarea>
       		
       		<label class="des">睡觉时间</label>
       		<input type="text" name="sleep" id="sleepTime"></input>

		</div>
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
				$('#evaluate').css("width", "50%");
				$.getJSON("/child/rest/student/0?teacherId=" + <%=teacher.getId()%>, function(data){
					console.log(data);
					var dataJ = [];
					for(var i = 0; i < data.total; i ++){
						dataJ.push({"text":data.rows[i].name, "id":data.rows[i].id});
					}
					$('#condition').combobox('loadData', dataJ);
					$('#condition').combobox({
						onSelect: function(){
							alert($('#condition').combobox('getValue');
						}
					});
				});
			});
			
			function ttrans(value, row, index){
				console.log(row.complete);
				if(row.complete == true){
					return '<span style="color:green;font-size:20px;font-weight:bold;">√</span>';
				}else{
					return '<span style="color:red;font-size:20px;font-weight:bold;">×</label>';
				}
			}

			var url = "rest/homeworkcomplete";
			var action;

			$(function() {
				var value = $('#condition').combobox('getValue');
				alert(value);
				$(function() {
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
				});
			});
		</script>
		</div>

<jsp:include page="../../footer.jsp" />