<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<table width="100%" border="1" cellpadding="0" cellspacing="0"
	id="tabAdd">
	<tr align="center">
		<td width="40px;"  bgcolor="#EFEFEF" Name="number" id="serId"></td>
		<td width="110px;" bgcolor="#EFEFEF" Name="selections" EditType="TextBox">
			<fmt:message key="curriculum.selections" bundle="${curriculum}" />
		</td>
		<td width="110px;" bgcolor="#EFEFEF" Name="mon" EditType="TextBox">
			<fmt:message key="curriculum.week1" bundle="${curriculum}" />
		</td>
		<td width="110px;" bgcolor="#EFEFEF" Name="tue" EditType="TextBox">
			<fmt:message key="curriculum.week2" bundle="${curriculum}" />
		</td>
		<td width="110px;" bgcolor="#EFEFEF" Name="wed" EditType="TextBox">
			<fmt:message key="curriculum.week3" bundle="${curriculum}" />
		</td>
		<td width="110px;" bgcolor="#EFEFEF" Name="thu" EditType="TextBox">
			<fmt:message key="curriculum.week4" bundle="${curriculum}" />
		</td>
		<td width="110px;" bgcolor="#EFEFEF" Name="fri" EditType="TextBox">
			<fmt:message key="curriculum.week5" bundle="${curriculum}" />
		</td>
	</tr>
	<tr>
		<td bgcolor="#FFFFFF" align="center" height="30px;">1</td>
		<td bgcolor="#FFFFFF" align="center"></td>
		<td bgcolor="#FFFFFF" align="center"></td>
		<td bgcolor="#FFFFFF" align="center"></td>
		<td bgcolor="#FFFFFF" align="center"></td>
		<td bgcolor="#FFFFFF" align="center"></td>
		<td bgcolor="#FFFFFF" align="center"></td>
	</tr>
</table>

<script type="text/javascript">
	$(document).ready(function(){
		var tabAdd = $("#tabAdd").get(0);
		EditTables(tabAdd);
		for(var i = 1; i < 10; i ++){
			AddRow($('#tabAdd').get(0),i);
		}
		DeleteRow($('#tabAdd').get(0), 10);
	});
	
	$('#tabAdd tr td').bind('mousemove', function(){
		var trSeq = $(this).parent().parent().find("tr").index($(this).parent()[0]);
		$('#trId').val(trSeq);
	});
	
	//绑定右键菜单
	$('#tabAdd tr').contextMenu('myMenu1', {
		bindings: {
	    	'addOneUp': function(t) {
	    		AddRow($('#tabAdd').get(0), parseInt($('#trId').val()), "before");
	        },
	        'addOneDown': function(t) {
	        	AddRow($('#tabAdd').get(0), parseInt($('#trId').val()), "after");
	        },
	        'delete': function(t) {
	        	DeleteRow($('#tabAdd').get(0),parseInt($('#trId').val()));
	        }
		}
				

	});

</script>
