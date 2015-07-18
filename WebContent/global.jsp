<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<div class="modal-body">
	<form role="form" id="form" method="post">
		<div class="form-group">
			<label for="name">
				<fmt:message key="term0" />
			</label>
			<div class="row">
				<div class="col-sm-9">
					<select class="combobox form-control" name="year" required="true">
						<%
							for (int i = 2010; i < 2050; i++) {
						%>
						<option value="<%=i%>"><%=i%> - <%=i+1%> <fmt:message key="school.year" /></option>
						<%
							}
						%>
					</select>
				</div>
				<div class="col-sm-3">
					<label class="radio-inline"> 
						<input type="radio" name="term" value="0" /> 
						<fmt:message key="term1" />
					</label>
					<label class="radio-inline"> 
						<input type="radio" name="term" value="1" /> 
						<fmt:message key="term2" />
					</label>
				</div>
			</div>
		</div>

		<div class="form-group">
			<label for="name">
				<fmt:message key="term.begin" />
			</label> <input type="text" class="form-control" id="term_begin" name="term_begin"
				placeholder="<fmt:message key="date.tip2" />" required="true" />
		</div>

		<div class="form-group">
			<label for="name">
				<fmt:message key="elective.begin" />
			</label> <input type="text" class="form-control" id="elective_begin" name="elective_begin"
				placeholder="<fmt:message key="date.tip1" />" required="true" />
		</div>

		<div class="form-group">
			<label for="name">
				<fmt:message key="elective.end" />
			</label> <input type="text" class="form-control" id="elective_end" name="elective_end"
				placeholder="<fmt:message key="date.tip2" />" required="true" />
		</div>

		<div class="form-group">
			<label for="name">
				<fmt:message key="elective.grade" />
			</label> <input type="text" class="form-control" id="elective_grade" name="elective_grade"
				placeholder="<fmt:message key="grade.tip" />" required="true" />
		</div>

		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="name"><fmt:message key="school.time" /></label> <input type="text" class="form-control" id="reach_time" name="reach_time"
						placeholder="<fmt:message key="school.time.tip" />" required="true" />
				</div>
				<div class="form-group">
					<label for="name"><fmt:message key="valid.time" /></label>
					<div class="row">
						<div class="col-sm-6">
							<input type="text" class="form-control" id="reach_begin" name="reach_begin" 
								placeholder="<fmt:message key="valid.time.tip1" />" required="true" />
						</div>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="reach_end" name="reach_end" 
								placeholder="<fmt:message key="valid.time.tip2" />" required="true" />
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label for="name">
						<fmt:message key="school.over" />
					</label> <input type="text" class="form-control" id="leave_time" name="leave_time"
						placeholder="<fmt:message key="school.over.tip" />" required="true" />
				</div>
				<div class="form-group">
					<label for="name"><fmt:message key="valid.over" /></label>
					<div class="row">
						<div class="col-sm-6">
							<input type="text" class="form-control" id="leave_begin" name="leave_begin" 
								placeholder="<fmt:message key="valid.over.tip1" />" required="true" />
						</div>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="leave_end" name="leave_end" 
								placeholder="<fmt:message key="valid.over.tip2" />" required="true" />
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="form-group">
			<label for="name">
				<fmt:message key="security.password" />
			</label> <input type="text" class="form-control" id="security_password"
				name="security_password" required="true" />
		</div>

		<div class="form-group">
			<label for="name">
				<fmt:message key="user.password" />
			</label> <input type="text" class="form-control" id="parents_password"
				name="parents_password" required="true" />
		</div>
	</form>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-primary" id="submit" data-loading-text="<fmt:message key="submiting" />">
		<fmt:message key="submit" />
	</button>
</div>
<script type="text/javascript">
	var url = "rest/global";
	$(document).ready(function() {
		select();
		//$(".form_datetime").datetimepicker({format: 'yyyy-mm-dd'});

		$("#submit").click(function() {

			restUpdate(url, null, $('#form'), function() {
				alert("<fmt:message key="save.success" />");
				select();
			});

		});

	});

	function select() {

		restSelect(url, null, function(json) {
			for (var i = 0; i < json.length; i++) {
				if (json[i].codeParent == 'term' && json[i].code == 'default') {
					$("select[name='year']").val(json[i].value.substring(0, 4));
					if (json[i].value.substring(4, 5) == "0") {
						$("input[name='term']").eq(0).attr('checked', 'true');
					} else {
						$("input[name='term']").eq(1).attr('checked', 'true');
					}
				} else {
					$("#" + json[i].codeParent + "_" + json[i].code).val(json[i].value);
				}
			}
		});

	}
</script>
<%@ include file="footer.jsp"%>
