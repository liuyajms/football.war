<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../header_dialog.jsp"%>
<%
		String courseId = (String)request.getParameter("courseId");
		String classesId = (String)request.getParameter("classesId");
	%> 
<link rel="stylesheet" type="text/css" media="screen" href="../../../asset/datepicker/datepicker.css">
<link rel="stylesheet" type="text/css" media="screen" href="../../../asset/validate/validate.css">	
	
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="course_my.th4" bundle="${course}" />
		</h3>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-4">
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="create" onclick="create()">
						<fmt:message key="create" />
					</button>
					<button type="button" class="btn btn-default" id="remove">
						<fmt:message key="remove" />
					</button>
				</div>
				<div class="btn-group">
					<button type="button" class="btn btn-default" id="import">
						<fmt:message key="import" />
					</button>
					<button type="button" class="btn btn-default" id="export">
						<fmt:message key="export" />
					</button>
				</div>
			</div>
			<div class="col-md-4">
				<ul class="pagination" id="pagination" style="margin: 0px;"></ul>
			</div>
			<div class="col-md-4">
				<div class="input-group">
					<input type="text" class="form-control" id="keyword" /> <span class="input-group-btn">
						<button class="btn btn-default" type="button" id="select">
							<fmt:message key="select" />
						</button>
					</span>
				</div>
			</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th style="width: 30px;"><input type="checkbox" class="rows" /></th>
				<th style="width: 50px;"><fmt:message key="operation" /></th>
                <th name="term"><fmt:message key="course_my.term" bundle="${course}" /></th>
                <th name="classesName"><fmt:message key="course_my.classesName" bundle="${course}" /></th>
                <th name="courseName"><fmt:message key="course_my.courseName" bundle="${course}" /></th>
				<th name="studentName"><fmt:message key="course_my.studentName" bundle="${course}" /></th>
				<th name="typeName"><fmt:message key="course_my.typeName" bundle="${course}" /></th>
				<th name="dateStr"><fmt:message key="course_my.dateStr" bundle="${course}" /></th>
				<th name="score"><fmt:message key="course_my.score" bundle="${course}" /></th>
				<th name="updateTime" style="width: 150px;" type="timestamp">
					<fmt:message key="course_my.updateTime" bundle="${course}" />
				</th>
			</tr>
		</thead>
		<tbody />
	</table>
</div>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-mg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="title"><fmt:message key="course_my.th4" bundle="${course}" /></h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post">
					<input type="hidden" name="id" id="id" /> 
					<input type="hidden" name="classesId"  value='<%=classesId %>' />
					<input type="hidden" name="courseId" id="courseId"  value='<%=courseId %>' />

 					<div class="form-group" >
						<label for="studentId">
							<fmt:message key="course_my.studentName" bundle="${course}" />
						</label>
						<select class="combobox form-control" id="studentId" name="studentId" required="true">
						</select>
					</div>
					
					<div class="form-group">
						<label for="type">
							<fmt:message key="course_my.typeName" bundle="${course}" />
						</label>
						<select class="combobox form-control" id="type" name="type" required="true" >
						</select>
					</div>
					
					<div class="form-group" >
						<label for="date">
							<fmt:message key="course_my.dateStr" bundle="${course}" />
						</label>
						<input id="date" name="date" type="text" class="form-control required"
									data-date-format="yyyy-mm-dd"
									placeholder="时间格式:yyyy-mm-dd" />
					</div>
					
					<div class="form-group">
						<label for="score">
							<fmt:message key="course_my.score" bundle="${course}" />
						</label>
						<input type="text" class="form-control number" id="score" name="score" required="true" />
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

<div class="modal fade" id="dialog_import" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">
                	<fmt:message key="import" />
                </h4>
            </div>
            <div class="modal-body">
                <form role="form" id="form_import" method="post" class="form-horizontal">
                    <div class="form-group row">
                        <label for="" class="col-sm-2 control-label" style="text-align: left">
                        	<fmt:message key="import.step1" />
                        </label>

                         <label class="col-sm-1  control-label" style="text-align: left;padding-right: 0;padding-left: 0;">
                         	<fmt:message key="course_my.typeName" bundle="${course}" />
                         </label>
                            <div class="col-sm-2">
                                <select class="form-control" id="type_custom" name="type_custom" required="true">
                                    <option></option>
                                </select>
                            </div>
                        <label class="col-sm-2  control-label" >
                        	<fmt:message key="course_my.dateStr" bundle="${course}" />
                        </label>
                        <div class="col-sm-2">
                                <input id="date_custom" name="date_custom" type="text" class="form-control"
                                       data-date-format="yyyy-mm-dd"
                                       placeholder="yyyy-mm-dd" />
                         </div>
                        <div class="col-sm-2">
                            <input type="button" class="btn btn-default" value="下载模板" onclick="downTemplate();" id="downTemplateBtn"  data-toggle="tooltip"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="import" class="col-sm-2">
                        	<fmt:message key="import.step2" />
                        </label>
                        <div class="col-sm-8" style="padding-left: 0px">
                        <input type="file"  name="import" />
                        <p class="help-block"><fmt:message key="import.step2.description" /></p>
                        </div>
                    </div>

                </form>

                <br/>
                <table id="import_table" class="table table-striped table-bordered" cellspacing="0" width="100%">
                    <thead >
						<tr>
							<th name="name" width="100px"><fmt:message
									key="import.field.name" /></th>
							<th name="type" width="90px"><fmt:message
									key="import.field.type" /></th>
							<th name="value"><fmt:message key="import.field.description" /></th>
						</tr>
					</thead>
                    <tbody id="tBody" />
                </table>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="submitGrow();" data-loading-text="导入中...">导入</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript"
	src="../../../asset/datepicker/bootstrap-datepicker.js"></script>
<script type="text/javascript"
	src="../../../asset/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="../../../asset/validate/messages_zh.js"></script>	
<script type="text/javascript">
var title = "";
var url = "../../../rest/course/score";
var rows = 10;
var richedit;
var lastErrors;
var classesId;

$(document).ready(function() {
	title = $("#title").html();
	initRowCheck($("#table"));
	
	if($("input[name='classesId']").val() == "null"){
		classesId = $('#classesId').val();
		$("input[name='classesId']").val(classesId);
	} else {
		classesId = $("input[name='classesId']").val();
	}
	
	initDatepicker(); //初始化时间选择框
	
	initDropdownBox(); //初始化下拉选择框
	
	validateForm(); //初始化表单校验
	
	$("#export").click(function() {
		window.location = url + "/export?courseId=" + $("#courseId").val() + "&classesId=" + classesId + "&keyword=" + $("#keyword").val();
	});
	$("#select").click(function() {
		selectCount();
	});

	$("#submit").click(function() {
		var button = $(this);
		if( $('#courseId').val()  == "null" ){
			alert('<fmt:message key="course_my.message" bundle="${course}" />');
			return;
		}
		
		if( !$("#form").valid()){
			return;
		}
		
		validate($("#form"), function() {
			button.button('loading');
			if ($("#id").val() == "") {
				restInsert(url, null, $("#form"), function(data) {
					button.button('reset');
					hideDialog($("#dialog"));
					selectCount();
				});
			} else {
				restUpdate(url + "/" + $("#id").val(), null, $("#form"), function(data) {
					button.button('reset');
					hideDialog($('#dialog'));
					selectCount();
				});
			}
		});

	});

	selectCount();

    $('.panel-footer').on('click',clickHandler);
    $.getJSON(url+"/rule", function(data) {
        importTable($("#import_table"), data, null, false, 3);
    });
});


function clickHandler(e){
    $("#form_import")[0].reset();
    var tid = e.target.id;
    if( tid == 'import'){
        $.getJSON(url+"/currentInfo?classesId="+classesId +"&courseId="+$('#courseId').val(), function(data) {
            var tip = data.term + ',' +data.classesName + "," + data.courseName
            +'<fmt:message key="course_my.tip" bundle="${course}" />';
            $('#downTemplateBtn').tooltip({title:tip});
        });
        showDialog($("#dialog_import"));
    } else if( tid == 'remove' ){
    	if (confirm("<fmt:message key="remove.confirm" />")) {
            $.each($("input[name=id]:checked"), function(i, n) {
                restDelete(url + "/" + $(n).val(), function() {
                });
            });
            selectCount();
        }
    }
}

function downTemplate(){
//    window.location = "../../../template/courseScoreTemplate.xlsx";
//    window.location.href = url + "/exportTemplate?fileName=courseScoreTemplate.xlsx";
    var params = "";
    if($('#type_custom').val() !="" ){
        params = "&type="+$("#type_custom").find("option:selected").text();
    }
    if($('#date_custom').val() !=""){
        params += "&date="+$('#date_custom').val();
    }
   window.location.href = url + "/exportTemplate?classesId="+ classesId + params;
}

/**
 * 导入成绩册数据
 */
function submitGrow(){
    var button = $(this);
    button.button('loading');
    restInsert(url + "/imported", {courseId : $('#courseId').val()}, $("#form_import"), function(data) {
        button.button('reset');
        if (data.success) {
			alert("<fmt:message key="import.success.0" />" + data.message
					+ "<fmt:message key="import.success.1" />");
			hideDialog($("#dialog_import"));
			selectCount();
		} else {
			alert("<fmt:message key="import.failure" />\n\n" + data.message);
		}
    });

}


function create() {
	showDialog($("#dialog"));
	$("#form")[0].reset();
	$("#id").val("");
}

function update(id) {
	showDialog($("#dialog"));

	restGet(url + "/" + id, null, $('#form'), function(data) {
//		console && console.log(data);
		$('input[name^="date"]').val(
				data.date && new Date(data.date).format('yyyy-MM-dd'));
	});
}

function selectCount() {
	restSelect(url + "/count", {
		"classesId" : classesId,
		"courseId" : $("#courseId").val(),
		"keyword" : $("#keyword").val()
	}, function(count) {
		refreshPagination($("#pagination"), count, rows, select);
	});
}

function select(page) {
	restSelect(url, {
		"classesId" : classesId,
		"courseId" : $("#courseId").val(),
		"keyword" : $("#keyword").val(),
		"page" : page,
		"rows" : rows
	}, function(json) {
		refreshTable($("#table"), json, [ {
			name :  '<fmt:message key="update" />',
			func : "update"
		} ]);
	
	});
}

//加载下拉框数据
function initDropdownBox(){
	var params = {
			"classesId" : classesId,
			"keyword" : $("#keyword").val(),
			"page" : 0,
			"rows" : 999};
	$.getJSON("../../../rest/student", params, function(data){
		for(var i = 0; i < data.length; i ++){
			$('#studentId').append("<option value='"+ data[i].id + "'>" + data[i].name + "</option>");
		}
	});

    $.getJSON("../../../rest/value/list?keyword=course_score,type", function(data) {
        for (var i = 0; i < data.length; i++) {
            $('#type').add('#type_custom').append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");//('loadData', dataJ);
        }
    });
}

/**
 * 初始化时间选择器
 */
function initDatepicker() {
	$('#date').add('#date_custom').datepicker({
		inputMask : true
	}).on('changeDate', function(ev) {
		$('.datepicker').hide();
	});
}

/**
 * 初始化表单验证
 */
function validateForm() {
	$("#form").validate(
			{
				rules : { // 日期验证
					date : {
						dateISO : true,
						required : true
					}
				},
				messages : {
					date : "<fmt:message key="course_my.validate.time" bundle="${course}" />"

				},
				success : function(label) {
					label.parent().removeClass('has-error').parent()
							.removeClass('has-error').end().end().remove();
					// element
					// .text('OK!').addClass('valid')
					// .closest('.control-group').removeClass('error').addClass('success');
				},
				errorPlacement : function(error, label) {
					if (label.parent().hasClass('input-group')) { // 如果有此样式，设置错误显示位置
						label.parent().parent().append(error).removeClass(
								'has-success').addClass('has-error')
					} else {
						// error.appendTo(element.parent());
						label.parent().append(error).removeClass('has-success')
								.addClass('has-error')
					}
				}
			});
}
</script>

<%@ include file="../../../footer.jsp"%>