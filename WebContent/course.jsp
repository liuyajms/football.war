<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">
			<fmt:message key="course.manage" />
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
			<ul class="pagination" id="pagination" style="margin:0px;"></ul>
		</div>
		</div>
	</div>
	<table id="table" class="table table-striped table-bordered" cellspacing="0" width="100%">
		<thead >
			<tr>
				<th style="width:10px;"><input type="checkbox" class="rows"/></th>
				<th style="width:50px;"><fmt:message key="operation" /></th>
				<th name="name" style="width:100px;"><fmt:message key="courseName" /></th>
				<th name="description">介绍</th>
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
					<fmt:message key="course.manage" />
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" id="form" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" id="id" />
					
					<div class="form-group">
						<label for="name">
							<fmt:message key="courseName" />
						</label>
						<input type="text" class="form-control" id="name" name="name" required="true" />
					</div>
					
					<div class="form-group">
						<label for="description">
							介绍
						</label>
						<textarea type="text" class="form-control" id="description" name="description" style="width:100%"></textarea>
					</div>
					
					<div class="form-group">
						<label for="pic"><fmt:message key="notice.image" bundle="${notice}" /></label> <input type="file" id="image"
							name="image" multiple="true" />
					</div>
					
					<div class="form-group row">
						<div class="col-sm-1"></div>
						<div class="col-sm-11" id="picDiv">
						</div>
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

<div class="modal fade" id="picDialog" tabindex="-1" role="dialog"
	aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div style="text-align:center;">
				<img class="center" id="picImage" scr="">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	
	var url = "rest/course";
	var action;
	var rows = 10;
	var richedit;
	$(document).ready(function(){
		initRowCheck($("#table"));
		richedit = initRichedit("description");
		
		selectCount();
		
		$("#select").click(function() {
			selectCount();
		});
		
		$("#export").click(function() {
			window.location = url + "/export";
		});
		
		$("#export_import").click(function() {
			window.location = url + "/export";
		});
		
		$("#import").click(function () {
	        showDialog($("#dialog_import"));
	        $("#del").prop("checked", true);
	    });
	
	    $.getJSON(url + "/rule", function (data) {
	        importTable($("#import_table"), data, null, false, null);
	    });
		
		$("#remove").click(function() {
			if (confirm("<fmt:message key="remove.confirm" />")) {
				$.each($("input[name=id]:checked"), function(i, n) {
					restDelete(url + "/" + $(n).val(), function() {
					});
				});
				selectCount();
			}
		});
		
		$("#submit_import").click(function () {
        var button = $(this);
        button.button('loading');
        
        if ($('#del').is(":checked") == true) {
        	if (confirm("<fmt:message key="import.clear.confirm" />")) {
                restInsert(url + "/imported", {del: 1}, $("#form_import"), function (data) {
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
            restInsert(url + "/imported", {del: 0}, $("#form_import"), function (data) {
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
		
		//提交按钮
		$("#submit").click(function() {
			var button = $(this);
			richedit.sync();
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
	});
	
	function selectCount() {
		restSelect(url + "/count", {"keyword" : $("#keyword").val()}, function(count) {
			refreshPagination($("#pagination"), count, rows, select);
		});
	}
	function select(page) {
		restSelect(url, {"keyword" : $("#keyword").val(), "page" : page, "rows" : rows}, function(json) {
			console.log(json);
			refreshTable($("#table"), json, [{name: '<fmt:message key="update" />', func: "update"}]);
		});
	}
	
	function create() {
		showDialog($("#dialog"));
		$('div[id^="picShow"]').remove();
		richedit.html("");
		$("#form")[0].reset();
		$("#id").val("");
	}
	function update(id) {
		showDialog($("#dialog"));
		$('div[id^="picShow"]').remove();
		restGet(url + "/" + id, null, $('#form'), function(data) {
			console.log(data);
			richedit.html(data.description);
			var addpicString="";
			var lage = "file/"+data.schoolId+"/course/"+data.id+"@l.png?"+Math.random();
			var p = "file/"+data.schoolId+"/course/"+data.id+".png?"+Math.random();
      		addpicString+=("<div class=\"col-sm-6 control-label\" id=\"picShow\">"+
      		"<a id=\"lg\" href=\"javascript:picImage('"+lage+"');\" >"+
			"<img onerror=\"def()\" id=\"picImage0\" src=\""+p+"\" / >"+
			"</a>"+
			/* "<a id=\"del\" width=\"100px\" href=\"javascript:pichide('"+i+"','"+data.id+"');\" >删除</a>"+ */
			"</div> "
			);
      picMark="";
      console.log(addpicString)
      $('#picDiv').append(addpicString); 
		});
		
	}
	
		function picImage(pathImage) {
$("#picImage").attr("src",pathImage);
showDialog($("#picDialog"));
}

function def(){
       $("#picImage0").hide();
}

</script>
<%@ include file="footer.jsp" %>