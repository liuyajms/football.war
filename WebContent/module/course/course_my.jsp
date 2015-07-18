<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title"><fmt:message key="course.my" /></h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
				</div>
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin: 0px;"></ul>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead >
			<tr>
				<th style="width:10px;"><input type="checkbox" class="rows"/></th>
				<th style="width:70px;"><fmt:message key="course_my.th1" bundle="${course}" /></th>
				<th style="width:70px;"><fmt:message key="course_my.th2" bundle="${course}" /></th>
				<th style="width:80px;"><fmt:message key="course_my.th3" bundle="${course}" /></th>
				<th style="width:70px;"><fmt:message key="course_my.th4" bundle="${course}" /></th>
				<th style="width:50px;"><fmt:message key="course_my.th5" bundle="${course}" /></th>
				<th name="classesName"><fmt:message key="course_my.classesName" bundle="${course}" /></th>
				<th name="courseName"><fmt:message key="course_my.courseName" bundle="${course}" /></th>
				<th name="teacherName"><fmt:message key="course_my.teacherName" bundle="${course}" /></th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<fmt:message key="course_my.title" bundle="${course}" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<div class="form-group">
						<label for="courseId">
							<fmt:message key="course_my.label1" bundle="${course}" />
						</label> <select class="combobox form-control" id="courseId" name="courseId"
							required="true">
							<option></option>
						</select>
					</div>
					<div class="form-group">
						<label for="teacherId">
							<fmt:message key="course_my.label2" bundle="${course}" />
						</label> <select class="combobox form-control" id="teacherId" name="teacherId"
							required="true">
							<option></option>
						</select>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="cancel" />
				</button>
				<button type="button" class="btn btn-primary" id="submit" data-loading-text="<fmt:message key="submiting" />">
					<fmt:message key="submit" />
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var url = "../../rest/classesTeacher";
	var rows = 10;
	var classesId = $("#classesId").val();
	
	$(document).ready(function() {

		initRowCheck($("#table"));
		selectCount();

		//查询按钮
		$("#select").click(function() {
			selectCount();
		});

		//提交按钮
		$("#submit").click(function() {
			var button = $(this);
			validate($("#form"), function() {
				button.button('loading');

				restInsert(url, {
					classesId : classesId
				}, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					selectCount();

				});
			});

		});
	});

	function create() {
		showDialog($("#dialog"));
		$("#form")[0].reset();
		$('#courseId').html("");
		getSelectData();
	}

	function selectCount() {
		restSelect(url + "/teacher/count", {
			"classesId" : $("#classesId").val()
		}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url + "/teacher", {
			"classesId" : $("#classesId").val(),
			"page" : page,
			"rows" : rows
		}, function(json) {
			refreshTable($("#table"), json, [ {
				name : '<fmt:message key="course_my.th1" bundle="${course}" />',
				func : "tree"
			}, {
				name : '<fmt:message key="course_my.th2" bundle="${course}" />',
				func : "classroom"
			}, {
				name : '<fmt:message key="course_my.th3" bundle="${course}" />',
				func : "evaluation"
			}, {
				name : '<fmt:message key="course_my.th4" bundle="${course}" />',
				func : "score"
			}, {
				name : '<fmt:message key="course_my.th5" bundle="${course}" />',
				func : "homework"
			} ], ["courseId"]);
		});
	}
	
	//跳转到知识树
	function tree(courseId) {
		window.open("knowledge/course_knowledge.jsp?courseId=" + courseId + "&classesId=" + $("#classesId").val(),"_self");
	}
	
	//跳转到微课堂
	function classroom(courseId) {
		window.open("classroom/course_classroom.jsp?courseId=" + courseId + "&classesId=" + $("#classesId").val(),"_self");
	}
	
	//跳转到课堂评价
	function evaluation(courseId) {
		window.open("evaluation/course_evaluation.jsp?courseId=" + courseId + "&classesId=" + $("#classesId").val(),"_self");
	}
	
	//跳转到成绩册
	function score(courseId) {
		window.open("score/course_score.jsp?courseId=" + courseId + "&classesId=" + $("#classesId").val(),"_self");
	}
	
	//跳转到作业
	function homework(courseId) {
		window.open("../homework/homework.jsp?courseId=" + courseId + "&classesId=" + $("#classesId").val(),"_self");
	}

</script>
<%@ include file="../../footer.jsp"%>