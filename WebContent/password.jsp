<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<div class="modal-body">
	<form role="form" id="form" method="post">
		
		<div class="form-group">
			<label for="name"><fmt:message key="password.old" /></label>
			<input type="password" class="form-control" id="codeOrgin" name="password_old" required="true" />
		</div>
		
		<div class="form-group">
			<label for="phone"><fmt:message key="password.new" /></label>
			<input type="password" class="form-control" id="codeNew" name="password_new" required="true" />
		</div>
		
		<div class="form-group">
			<label for="email"><fmt:message key="password.again" /></label>
			<input type="password" class="form-control" id="codeRepeat" name="codeRepeat" required="true" />
		</div>
		
	</form>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-primary" id="submit" data-loading-text="<fmt:message key="submiting" />">
		<fmt:message key="submit" />
	</button>
</div>

<script type="text/javascript">
	var url = "rest/teacher";
	
	$(document).ready(function() {
		
		$("#submit").click(function(){
			var codeOrgin = $("#codeOrgin").val();
			var codeNew = $("#codeNew").val();
			var codeRepeat = $("#codeRepeat").val();
			if(codeOrgin == ""){
				alert("<fmt:message key="password.tip1" />");
			}else if(codeNew != codeRepeat){
				alert("<fmt:message key="password.tip2" />");
			}else if(codeNew == ""){
				alert("<fmt:message key="password.tip3" />");
			}else{
				var id = "<%=teacher.getId()%>";
				restUpdate(url + "/" + id + "/password", null, $('#form'), function(data){
					if(data == true){
						alert("<fmt:message key="password.tip4" />");
						restDelete("rest/auth", function() {
							location.href = "login.jsp";
						});
					}else{
						alert("<fmt:message key="password.tip5" />");
					}
				});
			}
		});
		
	});

</script>
<%@ include file="footer.jsp" %>