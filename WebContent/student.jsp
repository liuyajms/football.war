<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="header_dialog.jsp" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">
        	<fmt:message key="student.manage" />
        </h3>
    </div>
    <div class="panel-footer">
        <div class="row">
            <div class="col-md-5">
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
                <%-- <div class="btn-group">
                    <button type="button" class="btn btn-default" id="import_grow_btn">
                    	<fmt:message key="student.import.grow" />
                    </button>
                </div> --%>
            </div>
            <div class="col-md-3">
                <ul class="pagination" id="pagination" style="margin:0px;"></ul>
            </div>
            <div class="col-md-4">
                <div class="input-group">
                    <input type="text" class="form-control" id="keyword"/>
				<span class="input-group-btn">
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
            <th style="width:30px;"><input type="checkbox" class="rows"/></th>
            <th style="width:50px;"><fmt:message key="operation" /></th>
            <th style="width:80px;"><fmt:message key="parent.manage" /></th>
            <th style="width:80px;"><fmt:message key="security" /></th>
            <th style="width:80px;">健康档案</th>
            <th style="width:65px;">错题集</th>
            <th name="name"  style="width:80px;"><fmt:message key="student.name" /></th>
            <%-- <th name="code"><fmt:message key="student.code" /></th> --%>
         <%--   <th name="card"><fmt:message key="student.card" /></th>--%>
            <%-- <th name="genderName"  style="width:50px;"><fmt:message key="student.gender" /></th>
            <th name="birthday"><fmt:message key="student.birthday" /></th> --%>
            <th name="classesName"  style="width:90px;"><fmt:message key="student.class" /></th>
        </tr>
        </thead>
        <tbody/>
    </table>
</div>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">
                	<fmt:message key="student.manage" />
                </h4>
            </div>
            <div class="modal-body">
                <form role="form" id="form" method="post">
                    <input type="hidden" name="id" id="id"/>

                    <div class="form-group">
                        <label for="name"><fmt:message key="student.name" /></label>
                        <input type="text" class="form-control" id="name" name="name" required="true"/>
                    </div>

                    <div class="form-group">
                        <label for="gender"><fmt:message key="student.gender" /></label>
                        <select class="combobox" id="gender" name="gender" style="height:35px;width:100px;">
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="birthday"><fmt:message key="student.birthday" /></label>
                        <input type="text" class="form-control" id="birthday"
                        	 placeholder="<fmt:message key="student.birthday.tip" />"
                               name="birthday"/>
                    </div>

                    <div class="form-group">
                        <label for="code">
                        	<fmt:message key="student.code" />
                        </label>
                        <input type="text" class="form-control" id="code" name="code"/>
                    </div>

                    <div class="form-group">
                        <label for="card"><fmt:message key="student.card" /></label>
                        <input type="text" class="form-control" id="card" name="card"/>
                    </div>

                    <div class="form-group">
                        <label for="address"><fmt:message key="student.address" /></label>
                        <input type="text" class="form-control" id="address" name="address"/>
                    </div>

                    <div class="form-group">
                        <label for="description">
                        	<fmt:message key="student.description" />
                        </label>
                        <textarea class="form-control" id="description" name="description"
                                  style="width:100%"></textarea>
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
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<fmt:message key="import" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form_import" id="form_import" method="post">
					<div class="form-group">
						<label><fmt:message key="import.step1" /></label>
						<button type="button" class="btn btn-default" id="export_import">
							<fmt:message key="export" />
						</button>
						<p class="help-block">
							<fmt:message key="import.step1.description" />
						</p>
					</div>

					<div class="form-group">
						<label for="import"><fmt:message key="import.step2" /></label> <input type="file" id="import" name="import" />
						<p class="help-block">
							<fmt:message key="import.step2.description" />
						</p>
					</div>

					<div class="form-group">
						<label style="color: red;"><fmt:message key="import.clear" /></label> <input type="checkbox" id="del" value="1"
							onclick="fnClick('del')" />
					</div>
				</form>
				<table id="import_table" class="table table-striped table-bordered" cellspacing="0" width="100%">
					<thead>
						<tr>
							<th name="name" width="100px"><fmt:message key="import.field.name" /></th>
							<th name="type" width="90px"><fmt:message key="import.field.type" /></th>
							<th name="value"><fmt:message key="import.field.description" /></th>
						</tr>
					</thead>
					<tbody id="tBody" />
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="submit_import" data-loading-text="<fmt:message key="importing" />">
					<fmt:message key="import" />
				</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="dialog_import_grow" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">
                	<fmt:message key="import" />
                </h4>
            </div>
            <div class="modal-body">
                <form role="form_import" id="form_import_grow" method="post" class="form-horizontal">

                    <div class="form-group row">
                        <label for="" class="col-sm-2 control-label" style="text-align: left"><fmt:message key="import.step1" /></label>

                        <label class="col-sm-1  control-label" style="text-align: left;padding-right: 0;padding-left: 0;">
                        	<fmt:message key="studentGrow.name" />
                        </label>
                        <div class="col-sm-2">
                            <input id="name_custom" name="name_custom" type="text" class="form-control"/>
                        </div>
                        <label class="col-sm-2  control-label" >
                        	<fmt:message key="studentGrow.type" />
                        </label>
                        <div class="col-sm-2">
                                <select class="form-control" id="type_custom" name="type_custom" required="true">
                                    <option></option>
                                </select>
                            </div>

                        <div class="col-sm-2">
                            <input type="button" class="btn btn-default" value="<fmt:message key="download.template" />"
                              onclick="downGrowTemplate();" id="downTemplateBtn"  data-toggle="tooltip"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="import_grow" class="col-sm-2"><fmt:message key="import.step2" /></label>
                        <div class="col-sm-8" style="padding-left: 0px">
                            <input type="file" name="import_grow" id="import_grow"/>
                            <p class="help-block"><fmt:message key="import.step2.description" /></p>
                        </div>
                    </div>

                    <%--<div class="form-group">
                        <label style="color:red; padding-right: 0px;" class="col-sm-2" >是否删除原有数据 </label>
                        <div class="col-sm-2">
                        <input type="checkbox" id="del_grow" value="1" onclick="fnClick('del_grow')" />
                            </div>
                    </div>--%>
                </form>
                <br>
                <table id="import_grow_table" class="table table-striped table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th name="name" width="100px"><fmt:message key="import.field.name" /></th>
						<th name="type" width="90px"><fmt:message key="import.field.type" /></th>
						<th name="value"><fmt:message key="import.field.description" /></th>
                    </tr>
                    </thead>
                    <tbody id="import_grow_tbody"/>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="submitGrow();" data-loading-text="<fmt:message key="importing" />">
             	   <fmt:message key="import" />
                </button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
var url = "rest/student";
var rows = 10;
var richedit;

var classesId = <%=request.getParameter("classesId")%>;

$(document).ready(function () {
    initRowCheck($("#table"));
    richedit = initRichedit("description");

    $("#remove").click(function () {
    	if (confirm("<fmt:message key="remove.confirm" />")) {
            $.each($("input[name=id]:checked"), function (i, n) {
                restDelete(url + "/" + $(n).val(), function () {
                });
            });
            selectCount();
        }
    });

    $("#export").click(function () {
        window.location = url + "/export?classesId=" + classesId + "&keyword=" + $("#keyword").val();
    });
    
    $("#export_import").click(function() {
		window.location = url + "/export?classesId=" + classesId;
	});

    $("#import").click(function () {
        showDialog($("#dialog_import"));
        $("#del").prop("checked", true);
    });

    $.getJSON("rest/student/rule", function (data) {
        importTable($("#import_table"), data, null, false, null);
    });

    $("#submit_import").click(function () {
        var button = $(this);
        button.button('loading');
        
        if ($('#del').is(":checked") == true) {
        	if (confirm("<fmt:message key="import.clear.confirm" />")) {
                restInsert(url + "/imported", {classesId: classesId, del: 1}, $("#form_import"), function (data) {
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
            } else {
                button.button('reset');
            }
        } else {
            restInsert(url + "/imported", {classesId: classesId, del: 0}, $("#form_import"), function (data) {
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
    });

    //$('#dp1').datepicker({format:"yyyy-dd-mm"});
    //$('.datepicker').datepicker();

    $("#select").click(function () {
        selectCount();
    });

    selectCount();

    $("#submit").click(function () {
        var button = $(this);
        richedit.sync();
        validate($("#form"), function () {
            button.button('loading');
            if ($("#id").val() == "") {
                restInsert(url, {classesId: classesId}, $("#form"), function (data) {
                    button.button('reset');
                    hideDialog($("#dialog"));
                    selectCount();
                });
            } else {
                restUpdate(url + "/" + $("#id").val(), null, $("#form"), function (data) {
                    button.button('reset');
                    hideDialog($('#dialog'));
                    selectCount();
                });
            }
        });

    });

    $('#import_grow_btn').on('click', clickHandler);

    $.getJSON("rest/student/grow/rule", function (data) {
        importTable($("#import_grow_table"), data, null, false, null);
    });
    
    $.getJSON("rest/value/list?keyword=student,gender", function(data) {
					for (var i = 0; i < data.length; i++) {
						$('#gender').append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");//('loadData', dataJ);
					}
				});

    $.getJSON("rest/value/list?keyword=student_grow,type", function (data) {
        for (var i = 0; i < data.length; i++) {
            $('#type_custom').append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");//('loadData', dataJ);
        }
    });
});

function create() {
    showDialog($("#dialog"));
    richedit.html("");
    $("#form")[0].reset();
    $("#id").val("");
}

function selectCount() {
    restSelect(url + "/count", {classesId: classesId, "keyword": $("#keyword").val()}, function (count) {
        refreshPagination($("#pagination"), count, rows, select);
    });
}
function select(page) {
    restSelect(url, {classesId: classesId, "keyword": $("#keyword").val(), "page": page, "rows": rows}, function (json) {
        refreshTable($("#table"), json, [
            {name: '<fmt:message key="update" />', func: "update"},
            {name: '<fmt:message key="parent.manage" />', func: "addParents"},
            {name: '<fmt:message key="security" />', func: "security"},
            {name: '健康档案', func: "health"},
            {name: '错题集', func: "wrong"}
        ]);
    });
}

function update(id) {
    showDialog($("#dialog"));

    restGet(url + "/" + id, null, $('#form'), function (data) {
        richedit.html(data.description);
    });
}

//加载班级数据
function fnGetCls() {
    $.getJSON("rest/classes/classesList", function (data) {
        var dataJ = [];
        dataJ.push({"text": "--<fmt:message key="option" />--", "id": 0});
        for (var i = 0; i < data.length; i++) {
            dataJ.push({"text": data[i].name, "id": data[i].id});
        }
        $('#classesId').combobox('loadData', dataJ);
    });

    $.getJSON("rest/value/list?tableCode=t001&fieldCode=gender", function (data) {
        var dataJ = [];
        dataJ.push({"text": "--<fmt:message key="option" />--", "id": 0});
        for (var i = 0; i < data.length; i++) {
            dataJ.push({"text": data[i].name, "id": data[i].code});
        }
        $('#gender').combobox('loadData', dataJ);
    });
}

function myformatter(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
            + (d < 10 ? ('0' + d) : d);
}
function myparser(s) {
    if (!s)
        return new Date();
    var ss = (s.split('-'));
    var y = parseInt(ss[0], 10);
    var m = parseInt(ss[1], 10);
    var d = parseInt(ss[2], 10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m - 1, d);
    } else {
        return new Date();
    }
}

function addParents(id) {
    window.open("parents.jsp?studenId=" + id + "&classesId=" + classesId,"_self");
}

function back() {
    window.open("classes.jsp","_self");
}

function security(id) {
    window.open("student_security.jsp?studentId=" + id + "&classesId=" + classesId,"_self");
}

function health(id) {
    window.open("module/health/health.jsp?studentId=" + id + "&classesId=" + classesId,"_self");
}

function wrong(id) {
    window.open("module/course/wrong/course_wrong.jsp?studentId=" + id + "&classesId=" + classesId,"_self");
}

//	$(".form_datetime").datepicker({format: 'yyyy-mm-dd'});

//	$('#starttime').datepicker({
//		  format: 'yyyy.mm.dd',
//		        weekStart: 1,
//		        autoclose: true,
//		        todayBtn: 'linked',
//		        language: 'zh-CN'
//		 }).on('changeDate',function(ev){
//		  var startTime = ev.date.valueOf();
//		  if(start<teach){
//		   alert("“评估开始时间 ”不能早于“授课时间 ” ！");
//		   $("#starttime").focus();
//		  }
//	});

function fnClick(str) {
    if ($('#' + str).is(":checked") == true) {
        $('#' + str).val("1");
    } else {
        $('#' + str).val("2");
    }
}

function clickHandler(e) {
    $("#form_import_grow")[0].reset();
    $.getJSON("rest/course/score/currentInfo?classesId="+classesId , function(data) {
        var tip = data.term + ',' +data.classesName ;
        $('#downTemplateBtn').tooltip({title:tip});
    });
    showDialog($("#dialog_import_grow"));
}

function downGrowTemplate() {
//        window.location.href = "./template/studentGrowTemplate.xlsx";
    var params = "";
    if ($('#type_custom').val() != "") {
        params = "&type=" + $("#type_custom").find("option:selected").text();
    }
    if ($('#name_custom').val() != "") {
        params += "&name=" + $('#name_custom').val();
    }
    window.location.href = url + "/grow/exportTemplate?classesId=" + classesId + params;
}

/**
 * 导入成长记录数据
 */
function submitGrow() {
    var button = $(this);
    button.button('loading');
    restInsert(url + "/grow/imported", {classesId: classesId, del_grow:$('#del_grow').val()}, $("#form_import_grow"), function (data) {
        button.button('reset');
        if (data.success) {
            alert("成功导入" + data.message + "条记录");
            hideDialog($("#dialog_import_grow"));
            selectCount();
        } else {
            alert("导入失败\n\n" + data.message);
        }
    });
}
</script>
<%@ include file="footer.jsp" %>