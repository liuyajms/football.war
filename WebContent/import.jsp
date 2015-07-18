<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header_dialog.jsp" %>	

<div class="panel panel-default">
<div class="modal fade" id="dialog_import" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<fmt:message key="student.import" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form_import" id="form_import" method="post">
					<div class="form-group">
						<label for="picture">
							<fmt:message key="import.file" />
						</label>
						<input type="file" id="import" name="import" />
					</div>
					
					<div class="form-group">
						<label style="color:red;">
							<fmt:message key="import.clear" />
						</label>
						<input type="checkbox" id="del" value="1" onclick="fnClick('del')" />
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="submit_import" data-loading-text="<fmt:message key="importing" />">
					<fmt:message key="import" />
				</button>
			</div>
		</div>
	</div>
</div>

</div>
<script type="text/javascript">
	var url = "rest/student";
	//"rest/" + "<%=request.getParameter("url")%>";
	var classesId = <%=request.getParameter("classesId")%>;
	
	$(document).ready(function() {
	
		showDialog($("#dialog_import"));
		$("#del").prop("checked", true);
		
		$("#import").click(function() {
			showDialog($("#dialog_import"));
			$("#del").prop("checked", true);
		});
		
		$("#submit_import").click(function() {
			var button = $(this);
			button.button('loading');
			if ($('#del').is(":checked") == true) {
				//if(confirm("确定删除原有数据?")){
					$('#pro').show();
					restInsert(url + "/imported", {classesId : classesId, del : 1}, $("#form_import"), function(data) {	
						button.button('reset');
						hideDialog($("#dialog_import"));
						$('#pro').hide();
						selectCount();
					});
				//}
			} else {
				restInsert(url + "/imported", {classesId : classesId, del : 0}, $("#form_import"), function(data) {	
					button.button('reset');
					hideDialog($("#dialog_import"));
					selectCount();
				});
			}
		});
		
	});
	
	function fnClick(str){
		if($('#' + str).is(":checked") == true){
			$('#' + str).val("1");
		}else{
			$('#' + str).val("2");
		}
	}
</script>
<%@ include file="footer.jsp" %>