<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>


<div class="panel">
	<div class="title">
		<label>
			<img src="images/navigation.png" style="width:22px;height:22px;" />
			<fmt:message key="teacher.manage1" />
		</label>
		<span class="control">
			<button onclick="create()" class="button button_create">
				<fmt:message key="create" />
			</button>
		</span>
	</div>
	<div class="view">
		<div class="table asynctable">
			<div class="filter">
				<input id="keyword" />
				<a class="button" onclick="javascript:select();">
					<fmt:message key="select" />
				</a>
			</div>
			<table id="table" title="<fmt:message key="teacher.manage1" />" class="table">
				<thead >
					<tr>
						<th name="name" style="width:100px;"><fmt:message key="teacher.name" /></th>
						<th name="mobile" style="width:100px;" align="center"><fmt:message key="phone" /></th>
						<th name="remark"><fmt:message key="teacher.remark" /></th>
						<th name="description"><fmt:message key="teacher.description" /></th>
					</tr>
				</thead>
				<tbody />
			</table>
		</div>
	</div>
</div>

<div id="user_info" style="display:none;">
	<form id="fm" method="post" style="font-size:14px;" novalidate enctype="multipart/form-data">
		<input type="hidden" name="id" id="id" />
		<div class="panel">
			<div class="title">
				<span class="control">
					<a href="javascript:submit();" class="button"><fmt:message key="submit" /></a>
					<a href="javascript:remove();" class="button button_remove"><fmt:message key="remove" /></a>
				</span>
			</div>
			<div class="view">
				<ul class="layout layout_form" cols="1">
					<li>
					<label for="email" class="for">
						<fmt:message key="teacher.role" />
					</label>
					<span>
						<input class="easyui-combobox" id="roleId" name="roleId"
							required="true" data-options="valueField:'id',textField:'text'"
							style="width: 600px; height: 25px;"/>
					</span>
				</li>
				<li>
					<label for="email" class="for">
						<fmt:message key="teacher.name" />
					</label>
					<span>
						<input name="name" id="name" class="easyui-validatebox"
							required="true" style="width: 600px; height: 25px;"/>
					</span>
				</li>
				<li>
					<label for="email" class="for">
						<fmt:message key="teacher.account" />
					</label>
					<span>
						<input name="mobile" id="mobile"
							class="easyui-numberbox" missingMessage="<fmt:message key="teacher.mobile.validate" />" 
							minLength="11" maxlength="11" style="width: 600px; height: 25px;"
							required="true"/>
					</span>
				</li>
				<li>
					<label for="email" class="for">
						<fmt:message key="teacher.title" />
					</label>
					<span>
						<input class="easyui-combobox" id="title" name="title"
							required="true" data-options="valueField:'id',textField:'text'"
							style="width: 250px; height: 25px;"/>
					</span>
				</li>
				<li>
					<label for="email" class="for">
						<fmt:message key="teacher.image.create" />
					</label>
					<span>
						<input name="image" id="image" type="file"
							id="uploadImage" style="width: 600px; height: 25px;" />
							<input type="text" name="picPath" id="picPath" readonly="true"/>
					</span>
				</li>
				<li>
					<label for="email" class="for">
						<fmt:message key="teacher.remark" />
					</label>
					<span style="display:inline-block;width:600px;">
						<textarea id="remark" name="remark"></textarea>
					</span>
				</li>
				<li>
					<label for="email" class="for">
						<fmt:message key="teacher.description" />
					</label>
					<span>
						<textarea name="description" id="description"
								style="height: 60px; width: 600px;"></textarea>
					</span>
				</li>
				</ul>
			</div>
		</div>
	</form>
</div>
<script charset="utf-8" src="editor/kindeditor-min.js"></script>
<script charset="utf-8" src="editor/zh_CN.js"></script>
<script type="text/javascript">
	var url = "rest/teacher";
	var title = "<fmt:message key="teacher" />";
	var page = 0;
	var rows = 10;

	$(document).ready(function() {
		select();
		$("#picture").show();
		$("#picPath").hide();
		$("#picPath").attr("style", "width:400px;border-style:none;");
		
		$("#mobile").bind("change", function(){
			if(!(/^\d{11}$/g).test($("#mobile").val())){
				alert("<fmt:message key="teacher.mobile.validate" />");
				$("#mobile").focus();
			}
		})
	});

	var editor;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="remark"]', {
			//afterBlur: function(){this.sync()};
			width: "100%",
			height : "150px"
		});
	});

	//下拉列表框数据加载
	function fnGetData() {
		$.getJSON("rest/value/list?tableCode=teacher&fieldCode=title",
				function(data) {
					var dataJ = [];
					dataJ.push({
						"text" : "--<fmt:message key="option" />--",
						"id" : 0
					});
					for (var i = 0; i < data.length; i++) {
						dataJ.push({
							"text" : data[i].name,
							"id" : data[i].code
						});
					}
					$('#title').combobox('loadData', dataJ);
				});
		$.getJSON("rest/role", function(data) {
			var datas = [];
			datas.push({
				"text" : "--<fmt:message key="option" />--",
				"id" : 0
			});
			for (var i = 0; i < data.rows.length; i++) {
				datas.push({
					"text" : data.rows[i].name,
					"id" : data.rows[i].id
				});
			}
			$('#roleId').combobox('loadData', datas);
		});
	}

	function create() {
		showDialog({"model": true}, "<fmt:message key="teacher.create" />", null, $("#user_info"));
		//$("#del").hide();
		//$("#picture").show();
		//$("#picPath").hide();
		//$("#picPath").val("");
		//$("#user_info").dialog("open").dialog("setTitle", title + " - 添加");
		//$("#fm").form("clear");
		//$("#roleId").combobox('setValue', '0');
		//$("#title").combobox('setValue', '0');
		//editor.html("");
		//fnGetData();

	}
	function update(key) {

		$("#picPath").val("");
		showDialog({"model": true}, "<fmt:message key="teacher.update" />", null, $("#user_info"));
		restGet(url + "/" + key, null, $('#fm'), function(data){
			//onloading($('#fm'));
			//editor.html(data.remark);//KindEditor同步
		});
		//fnGetData();
		$("#image").val("");
	}

	function submit() {
		//editor.sync();//KindEditor同步
		if ($("#id").val() == "") {
			//onloading($("#fm"));
			restInsert(url, null, $("#fm"), function(data) {
				hideDialog();
				select();
			});
		} else {
			//onloading($("#fm"));
			restUpdate(url + "/" + $("#id").val(), null, $("#fm"), function() {
				hideDialog();
				select();
			});
			
		}
	}
	function remove() {
		if ($("#id").val() != "" && confirm("<fmt:message key="remove.confirm" />")) {
			restDelete(url + "/" + $("#id").val(), function() {
				hideDialog();
				select();
			});
		}
	}


	function select() {
		restSelect(url, {"keyword" : $("#keyword").val(), "page" : page, "rows" : rows}, function(json) {
			var heads = new Array();

			var thead = $("#table>thead");
			$.each($("th", thead), function(i, n) {
				heads[i] = $(n);
			});

			var tbody = $("#table>tbody");
			tbody.empty();

			$.each(json.rows, function(i, n) {
				var tr = $("<tr></tr>");
				tr.dblclick(function() {
					update(n.id);
				});
				tbody.append(tr);

				$.each(heads, function(j, o) {
					var td = $("<td></td>");
					td.attr("align", o.attr("align"));
					td.text(eval("n." + o.attr("name")));

					tr.append(td);
				});
			});
		});
	}
</script>


<%@ include file="footer.jsp" %>

