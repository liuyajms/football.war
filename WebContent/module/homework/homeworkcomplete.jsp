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
	<div title="家庭作业" style="padding: 2px; overflow: hidden;">
		<table id="user" title="家庭作业" class="easyui-datagrid"
			style="width: auto; height: auto" toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="true"
			singleSelect="false">
			<thead>
				<tr>
					<th field="course" width="50">科目</th>
					<th field="description" id="description" width="150">内容</th>
					<th field="complete" id="complete" width="15" align="center" editor="{type:'checkbox', options:{on:'是', off:'否'}}">是否完成</th>
					<th field="begin_time" width="15" editor="text">开始时间</th>
					<th field="end_time" width="15" editor="text">完成时间</th>
					<th field="actionMenu" width="15" formatter="formatAction">操作</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
			 <a href="javascript:void(0)":
				class="easyui-linkbutton" iconCls="icon-add" plain="true"
				onclick="submitData()">提交</a> 
		</div>
		
		
	</div>
	
		<div style="padding: 2px; overflow: hidden;">
			<label class="des">评价</label>
       		<textarea name="evaluate" id="evaluate"  style="height:80px;"></textarea>
       		
       		<label class="des">睡觉时间</label>
       		<input type="text" name="sleep" id="sleepTime"></input>

		</div>
	
	

		<script type="text/javascript">
			 $(document).ready(function(){
				$('#evaluate').css("width", "50%");
			}); 
			
			function formatAction(value, row, index){
				if (row.editing){
					var s = '<a href="#" onclick="saveData(this)">保存</a> ';
					return s;
				} else {
					var e = '<a href="#" onclick="inputData(this)">录入</a> ';
					return e;
				}
			}
			
			//在表格中加入可编辑的单元格
			$.extend($.fn.datagrid.defaults.editors, {
				numberspinner: {
					init: function(container, options){
						var input = $('<input type="text">').appendTo(container);
						return input.numberspinner(options);
					},
					destroy: function(target){
						$(target).numberspinner('destroy');
					},
					getValue: function(target){
						return $(target).numberspinner('getValue');
					},
					setValue: function(target, value){
						$(target).numberspinner('setValue',value);
					},
					resize: function(target, width){
						$(target).numberspinner('resize',width);
					}
				}
			});
		$(function(){
			$('#user').datagrid({
				onBeforeEdit:function(index,row){
					row.editing = true;
					updateActions(index);
				},
				onAfterEdit:function(index,row){
					row.editing = false;
					updateActions(index);
				},
				onCancelEdit:function(index,row){
					row.editing = false;
					updateActions(index);
				}
			});
		});
		
		function updateActions(index){
			$('#user').datagrid('updateRow',{
				index:index,
				row:{}
			});
		}
		function getRowIndex(target){
			var tr = $(target).closest('tr.datagrid-row');
			return parseInt(tr.attr('datagrid-row-index'));
		}
		function inputData(target){
			$('#user').datagrid('beginEdit', getRowIndex(target));
		}
		
		function saveData(target){
			$('#user').datagrid('endEdit', getRowIndex(target));
			$('#user').datagrid('selectAll');//全选表格数据
		}

			var url = "/child/rest/homeworkcomplete";
			var action;

			$(function() {
				$(function() {
					var p = $('#user').datagrid('getPager');
					$(p).pagination({
						onSelectPage : function(pageNumber, pageSize) {
							var pagination = $(this);
							pagination.pagination('loading');
							$.getJSON(url + "?page=" + pageNumber + "&rows=" + pageSize, function(data) {
								$('#user').datagrid("loadData", data);
								pagination.pagination('loaded');
							});
						}
					});
					$(p).pagination("select");
				});
			});

			//保存方法，如果id为空的话，则为新增，如果不为空的话，则是修改
			function saveUser() {
				if ($("#id").val() == "") {
					$.post(url, {
						action : action,
						course : $("#course").val(),
						description : $("#description").val()
					}, function(data) {
						$('#user_info').dialog('close'); // close the dialog
						$($('#user').datagrid('getPager')).pagination('select');
					});
				} else {
					$.ajax({
						type : "PUT",
						url : url + "/" + $("#id").val(),
						contentType : "application/x-www-form-urlencoded; charset=utf-8",
						dataType : "json",
						data : {
							course : $("#course").val(),
							description : $("#description").val()
						},
						success : function() {
							$('#user_info').dialog('close'); // close the dialog
							$($('#user').datagrid('getPager'))
							.pagination('select');
						}
					});
				}
			}
			
			function submitData(){
				$('#user').datagrid('selectAll');
				var gridData = $("#user").datagrid("getSelections");
				for(var i = 0; i < gridData.length; i ++){
					console.log(gridData[i]);
					$.ajax({
						type : "PUT",
						url : url,
						contentType : "application/x-www-form-urlencoded; charset=utf-8",
						dataType : "json",
						data : {
							homeworkId: gridData[i].homeworkId,
							studentId: gridData[i].studentId,
							beginTime: gridData[i].begin_time,
							endTime: gridData[i].end_time,
							complete: gridData[i].complete,		//转
							sleepTime: $('#sleepTime').val(),
							evaluate: $('#evaluate').val()    	//转
						},
						success : function() {
							alert(1);
						}
					});
				}
				 
			}

		</script>
		</div>

<jsp:include page="../../footer.jsp" />