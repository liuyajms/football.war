<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>
<div class="modal-body">
	<form role="form" id="form" method="post" enctype="multipart/form-data">
		<div class="form-group">
			<label for="name">
				<fmt:message key="schoolName"  />
			</label> <input type="text" class="form-control" id="name" name="name" required="true" />
		</div>

		<div class="form-group">
			<label for="phone">
				<fmt:message key="phone"  />
			</label> <input type="text" class="form-control" id="phone" name="phone" />
		</div>

		<div class="form-group">
			<label for="email">
				<fmt:message key="email"  />
			</label> <input type="text" class="form-control" id="email" name="email" />
		</div>

		<div class="form-group">
			<label for="address">
				<fmt:message key="address"  />
			</label> <input type="text" class="form-control" id="address" name="address" />
		</div>
		<div class="form-group">
			<label for="picture">
				<fmt:message key="pic"  />
			</label> <input type="file" id="picture" name="picture" /> <img style="max-height: 100px;"
				class="img-rounded" id="picture_show" />
		</div>
		
		<div class="form-group">
			<label for="picture_school">
				<fmt:message key="pic_school"  />
			</label>
			<input type="file" id="picture_school" name="picture_school" />
			<img style="max-height: 100px;" class="img-rounded" id="picture_school_show" />
		</div>
		
		<div class="form-group">
			<label for="description">
				<fmt:message key="school.description"  />
			</label>
			<textarea class="form-control" id="description" name="description" style="width: 100%; height: 160px;"></textarea>
		</div>
	</form>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-primary" id="submit" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
</div>
<script type="text/javascript">
var url = "../../rest/contact";
$(document).ready(function() {

	select();

	$("#submit").click(function() {
		var button = $(this);
		button.button('loading');
		restUpdate(url, null, $("#form"), function(data) {
			button.button('reset');
			alert("<fmt:message key="save.success" />");
			select();
		});
	});
});

function select() {
	restSelect(url, null, function(data) {
		$("#name").val(data.name);
		$("#phone").val(data.phone);
		$("#email").val(data.email);
		$("#address").val(data.address);
		$("#picture").val("");
		$("#picture_show").attr("src", "../../files/" + data.id + "/logo.png?" + data.updateTime);
		$("#description").val(data.description);
		$("#picture_school").val("");
		$("#picture_school_show").attr("src", "../../files/" + data.id + "/school.png?" + data.updateTime);
	});
}
</script>

<%@ include file="../../footer.jsp"%>