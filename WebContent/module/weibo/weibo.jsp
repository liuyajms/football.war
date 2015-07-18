<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<jsp:include page="../../header.jsp"></jsp:include>
<style type="text/css">
.font {
	font-size: 12px;
}

#table td {
	width: 200px;
}

#addShow a {
	color: blue;
}

* {
	font-size: 14px;
}

#cc {
	border: 1px solid #ccc;
}

.contents {
	width: 637px;
}

li a {
	color: gray;
	font-size: 14px;
}

#ulList li {
	height: 13px;
}

.one {
	font-size: 20px;
	color: yellowgreen;
	font-weight: bold;
}

.expandCom {
	border: 1px solid #ccc;
	height: auto;
	padding: 10px;
	display: none;
}

.selectLi {
	background: #ccc;
}
</style>
<script charset="utf-8" src="editor/kindeditor-min.js"></script>
<script charset="utf-8" src="editor/zh_CN.js"></script>
<script>
	var classesId = null;
	var schoolId;
	$(document)
			.ready(
					function() {
						var requestUrl = "../../rest/classes";
						//获取班级信息添加到下拉选中
						var firstOpt = $("<option/>")
								.text(
										"--<fmt:message key="weibo.option" bundle="${weibo}" />--")
								.attr("value", "0").selected("true");
						$("#selectShow").append(firstOpt);
						$.getJSON(requestUrl + "/classesList", function(json) {

							$(json).each(
									function() {
										var opt = $("<option/>")
												.text(this.name).attr("value",
														this.id);
										$("#selectShow").append(opt);
									});
						});
						//生成左侧导航列表
						$("#ulList")
								.append(
										"<li><a href='javascript:void(0);'><fmt:message key="weibo.all" bundle="${weibo}" /></a><li>");
						$("#ulList")
								.append(
										"<li value='-1'><a href='javascript:void(0);'><fmt:message key="weibo.school" bundle="${weibo}" /></a><li>");
						$
								.getJSON(
										requestUrl + "/classesList",
										function(json) {
											$(json)
													.each(
															function() {
																var li = "<li value="+this.id+"><a href='javascript:void(0);'>"
																		+ this.name
																		+ "</a><li>";
																$("#ulList")
																		.append(
																				li);
															});
										});
						//创建导航列表点击事件
						$('#ulList li')
								.live(
										'click',
										function() {
											classesId = null;
											$('#ulList li').removeClass(
													"selectLi");
											$(this).addClass("selectLi");
											if ($(this).text() == "<fmt:message key="weibo.all" bundle="${weibo}" />") {
											} else {
												classesId = $(this).val();
											}
											getAllWeibos(classesId);
										});
						getAllWeibos(classesId);
					});
	//发布微博
	function publish() {
		var url = "rest/weibo";
		onloading($('#fm'));
		restInsert(url, null, $("#fm"), function(data) {
			alert("<fmt:message key="weibo.success" bundle="${weibo}" />");
			$('#description').val('');
			getAllWeibos(classesId);
			removeload();
		});

	}

	function onRadioPublishAll() {
		$("#selectTr").hide();
		$("#selectTr").clear();
	}
	function onRadioPublishGrade() {
		$("#selectTr").show();
		$("#selectShow").hide();
		$("#checkboxShow").show();
	}
	function onRadioPublishClass() {
		$("#selectTr").show();
		$("#checkboxShow").hide();
		$("#selectShow").show();
	}

	//获取评论
	function getAllWeiboComments(weiId) {
		var divStr = "";
		var requestUrl = "rest/weiboComment";
		$
				.ajax({
					url : requestUrl,
					data : {
						weiboId : weiId
					},
					dataType : "json",
					contentType : "application/x-www-form-urlencoded; charset=utf-8",
					success : function(data) {
						$(data.rows)
								.each(
										function() {
											//格式化时间
											var t = this.createTime;
											var name = "";
											var d = new Date();
											d.setTime(t);
											var s = d
													.format('yyyy-MM-dd HH:mm:ss');
											//获取评论者
											var parthId;
											var realPath = "";
											name = this.createUserName;
											if (this.createUserName
													.indexOf("<fmt:message key="weibo.seperator" bundle="${weibo}" />") != -1) {
												realPath = "parents";
											} else {
												realPath = "teacher";
											}
											pathId = this.createUserId;
											var divS = "<table  cellspacing='10' style='border-top: 1px dashed #ccc'>"
													+ "<tr>"
													+ "<td rowspan='4' style='width:50px' valign='top'><img style='width:50px;height:50px;' src='../../files/"
													+ schoolId
													+ "/"
													+ realPath
													+ "/"
													+ pathId
													+ ".png'</td>"
													+ "<td style='color:blue;'>"
													+ name
													+ "</td>"
													+ "</tr>"
													+ "<tr>"
													+ "<td colspan='2'><div style='width:500px'>"
													+ this.description
													+ "</div>"
													+ "</td>"
													+ "</tr>"
													+ "<tr>"
													+ "<td style='color:blue;'>"
													+ s
													+ "</td>"
													+ "<td align='right'><input type='hidden' id='weiboCommentsId' value="+this.id+"><a href='javascript:deleteWeiboComments("
													+ this.weiboId
													+ ","
													+ this.id
													+ ");'><fmt:message key="remove" /></a></td>"
													+ "</tr>" + "</table>";
											divStr += divS;
										});
						$("#" + weiId).html(divStr);
					}
				});
	}
	//展开评论
	function expandComments(msgId) {
		$("#" + msgId).toggle();
		onloading($("body"));
		getAllWeiboComments(msgId);
		removeload();
	}

	Date.prototype.format = function(fmt) {
		var o = {
			"M+" : this.getMonth() + 1, //月份        
			"d+" : this.getDate(), //日        
			"h+" : this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时        
			"H+" : this.getHours(), //小时        
			"m+" : this.getMinutes(), //分        
			"s+" : this.getSeconds(), //秒        
			"q+" : Math.floor((this.getMonth() + 3) / 3), //季度        
			"S" : this.getMilliseconds()
		//毫秒        
		};
		if (/(y+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		}
		if (/(E+)/.test(fmt)) {
			fmt = fmt
					.replace(
							RegExp.$1,
							((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f"
									: "\u5468")
									: "")
									+ week[this.getDay() + ""]);
		}
		for ( var k in o) {
			if (new RegExp("(" + k + ")").test(fmt)) {
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
						: (("00" + o[k]).substr(("" + o[k]).length)));
			}
		}
		return fmt;
	}
	//删除微博
	function deleteWeibo(id) {
		var url = "rest/weibo";
		$.messager
				.confirm(
						'<fmt:message key="weibo.remove" bundle="${weibo}" />',
						'<fmt:message key="weibo.remove.confirm" bundle="${weibo}" />',
						function(r) {
							onloading($("body"));
							if (r) {
								$
										.ajax({
											type : "DELETE",
											url : url + "/" + id,
											dataType : "json",
											success : function() {
												removeload();
												alert("<fmt:message key="weibo.remove.confirm" bundle="${weibo}" />");
												getAllWeibos(classesId);
											}
										});
							}
						});
	}
	//删除评论
	function deleteWeiboComments(weiboId, id) {
		var url = "rest/weiboComment";
		$.messager
				.confirm(
						'<fmt:message key="weibo.remove" bundle="${weibo}" />',
						'<fmt:message key="weibo.remove.confirm" bundle="${weibo}" />',
						function(r) {
							onloading($("body"));
							if (r) {
								$
										.ajax({
											type : "DELETE",
											url : url + "/" + id,
											dataType : "json",
											success : function() {
												removeload();
												alert("<fmt:message key="weibo.remove.confirm" bundle="${weibo}" />");
												getAllWeiboComments(weiboId);
											}
										});
							}
						});
	}
	//发表评论
	function newComments(id) {
		$('#commentsDialog')
				.dialog('open')
				.dialog('setTitle',
						'alert("<fmt:message key="weibo.comment" bundle="${weibo}" />");');
		$('#uDescription').val('');
		$('#newComments').val(id);
	}
	//保存评论
	function saveComments() {
		var url = "rest/weiboComment";
		var id = $("#newComments").val();
		onloading($('#fm'));
		restInsert(url, null, $("#fm2"), function(data) {
			removeload();
			alert("<fmt:message key="weibo.add.success" bundle="${weibo}" />");
			getAllWeiboComments(id);

		});
		$('#commentsDialog').dialog('close');
	}

	//审核
	function check(id) {
		var url = "rest/weibo";
		$.messager
				.confirm(
						'<fmt:message key="weibo.confirm"  bundle="${weibo}" />',
						'<fmt:message key="weibo.confirm.message"  bundle="${weibo}" />',
						function(r) {
							if (r) {
								restUpdate(
										url + "/" + id,
										null,
										null,
										function() {
											alert("<fmt:message key="weibo.confirm.success"  bundle="${weibo}" />");
											getAllWeibos(classesId);
										});
							}
						});
	}
	//获取微博
	function getAllWeibos(classesId) {
		onloading($('#fm'));
		var requestUrl;
		if (classesId != null) {
			requestUrl = "rest/weibo?classesId=" + classesId;
		} else {
			requestUrl = "rest/weibo?";
		}
		var p = $('#user').datagrid('getPager');
		$(p)
				.pagination(
						{
							beforePageText : '<fmt:message key="weibo.page0"  bundle="${weibo}" />',//页数文本框前显示的汉字  
							afterPageText : '<fmt:message key="weibo.page1"  bundle="${weibo}" /> {pages} <fmt:message key="weibo.page2"  bundle="${weibo}" />',
							displayMsg : '<fmt:message key="weibo.page3"  bundle="${weibo}" /> {from} - {to} <fmt:message key="weibo.page4"  bundle="${weibo}" /> {total} <fmt:message key="weibo.page5"  bundle="${weibo}" />',
							onSelectPage : function(pageNumber, pageSize) {
								var page = pageNumber - 1;
								var pagination = $(this);
								pagination.pagination('loading');
								$
										.getJSON(
												requestUrl + "&page=" + page
														+ "&rows=" + pageSize,
												function(data) {
													var divStr = "";
													$("#addShow").html("");
													$('#user').datagrid(
															"loadData", data);
													pagination
															.pagination('loaded');
													$(data.rows)
															.each(
																	function() {
																		totalComments = this.totalComments;
																		schoolId = this.schoolId;
																		//格式化时间
																		var t = this.createTime;
																		var name = "";
																		var d = new Date();
																		d
																				.setTime(t);
																		var s = d
																				.format('yyyy-MM-dd HH:mm:ss');
																		//获取广播范围
																		var type = "";
																		var realPath = "";
																		if (this.classesId != null
																				&& this.classesId != 0) {
																			type = this.classesName;
																		} else {
																			type = "<fmt:message key="weibo.school"  bundle="${weibo}" />";
																		}
																		var pathId;
																		name = this.createUserName;
																		if (this.createUserName
																				.indexOf("<fmt:message key="weibo.seperator"  bundle="${weibo}" />") != -1) {
																			realPath = "parents";
																		} else {
																			realPath = "teacher";
																		}
																		pathId = this.createUserId;
																		var divS = "<table  cellspacing='10' style='border-top: 1px dashed #ccc'>"
																				+ "<tr>"
																				+ "<td rowspan='4' style='width:50px' valign='top'><img style='width:50px;height:50px;' src='../../files/" + this.schoolId + "/"+ realPath + "/" +pathId + ".png'/></td>"
																				+ "<td style='color:blue;'>"
																				+ name
																				+ "</td>"
																				+ "<td align='right' style='width:auto'><label style='color: white; background: yellowgreen'>"
																				+ type
																				+ "</label></td>"
																				+ "</tr>"
																				+ "<tr>"
																				+ "<td colspan='2'><div class='contents'" +">"
																				+ this.description
																				+ "</div>"
																				+ "</td>"
																				+ "</tr>"
																				+ "<tr>"
																				+ "<td style='color:blue;'>"
																				+ s
																				+ "</td>"
																				+ "<td align='right'><a href='javascript:deleteWeibo("
																				+ this.id
																				+ ");'><fmt:message key="remove"  /></a>&nbsp&nbsp<a href='javascript:newComments("
																				+ this.id
																				+ ");'> <fmt:message key="weibo.publish"  bundle="${weibo}" />";
																		if (this.auditTeacherId == null) {
																			divS += "</a>&nbsp;&nbsp;<a href='javascript:check("
																					+ this.id
																					+ ");'><fmt:message key="weibo.verify"  bundle="${weibo}" />";
																		}
																		divS += "</a>&nbsp;&nbsp;<a href='javascript:void(0)' onclick='expandComments("
																				+ this.id
																				+ ")'><fmt:message key="weibo.viewComment"  bundle="${weibo}" />["
																				+ this.commentCount
																				+ "]</a></td>"
																				+ "</tr><tr><td colspan='2'><div id="+this.id+" class='expandCom'></div></td></tr>"
																				+ "</table>";
																		divStr += divS;
																	});
													$("#addShow").html(divStr);
												});
							}
						});
		$(p).pagination("select");
		removeload();
	}
</script>
<div id="cc" class="easyui-layout" style="height: auto;">
	<div title=""
		style="background: #FFF; width: 240px; height: 800px; padding: 10px; float: left; overflow: auto;">
		<ul>
			<li class="one"><fmt:message key="weibo.classes"
					bundle="${weibo}" />
				<ul>
					<li style="border-top: 1px solid #000; height: 12px;"></li>
				</ul>
				<ul id="ulList">
				</ul></li>
		</ul>
	</div>
	<div title=""
		style="padding: 10px; background: #FFF; overflow: auto; border-left: 1px solid #ccc;">
		<form id="fm" method="post" enctype="multipart/form-data">
			<table style="border: 1px solid #ccc" class="font">
				<tr>
					<td width="70px"><fmt:message key="weibo.description"
							bundle="${weibo}" /></td>
					<td colspan="2"><textarea id="description" name="description"
							rows="8" style="width: 630px"></textarea> <span id="spBJQCount"></span>
						<input type="file" id="pic" name="pic"
						value="<fmt:message key="weibo.pic"  bundle="${weibo}" />" /></td>
				</tr>
				<tr id="trNewsType">
					<td><fmt:message key="weibo.publish.area" bundle="${weibo}" /></td>
					<td colspan="2"><input type="radio" id="radioPublishAll"
						value="1" name="radioPublish" onclick="onRadioPublishAll()" /><label
						for="radioPublishAll"><fmt:message key="weibo.all"
								bundle="${weibo}" /></label> <input type="radio" id="radioPublishClass"
						value="3" name="radioPublish" checked="checked"
						onclick="onRadioPublishClass()" /><label for="radioPublishClass"><fmt:message
								key="classes" /></label></td>
				</tr>
				<tr id="selectTr">
					<td><fmt:message key="weibo.select.area" bundle="${weibo}" /></td>
					<td colspan="2" id="selectTd"><select id="selectShow"
						name="classesId">
					</select></td>
				</tr>
				<tr>
					<td></td>
					<td><a href="weibo.jsp" id="linkMicroblog"
						style='color: white; background: yellowgreen'> <fmt:message
								key="weibo" bundle="${weibo}" />
					</a> <a href="broadcast.jsp" style='color: black'><fmt:message
								key="broadcast" /></a></td>
					<td><input type="checkbox" id="checkPush" checked="checked" />
						<label for="checkPush"> <fmt:message
								key="weibo.publish.button" bundle="${weibo}" />
					</label>&nbsp;&nbsp; <input type="button" onclick="publish()"
						value="<fmt:message key="weibo.send"  bundle="${weibo}" />" /></td>
				</tr>
			</table>
		</form>
		<div id="addShow" style="background: #FFF; overflow: visible;"></div>
		<div id="listShow"
			style="padding-top: 10px; border-top: 1px dashed #ccc;">
			<table id="user" class="easyui-datagrid" pagination="true"
				border="false" style="height: 30px;"></table>
		</div>
		<br />
	</div>
	<div id="commentsDialog" class="easyui-dialog" modal="true"
		style="width: 600px; height: 250px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<form id="fm2" method="post">
			<input type="hidden" name="weiboId" id="newComments" />
			<table style="padding: 7px;">
				<tr>
					<td><fmt:message key="weibo.comment" bundle="${weibo}" /></td>
					<td><textarea name="description" id="uDescription" rows="8"
							style="width: 450px"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-save" onclick="saveComments()"> <fmt:message
				key="submit" /></a> <a href="javascript:void(0)"
			class="easyui-linkbutton" iconCls="icon-undo"
			onclick="javascript:$('#commentsDialog').dialog('close')"> <fmt:message
				key="cancel" />
		</a>
	</div>
</div>
</div>
<jsp:include page="../../footer.jsp"></jsp:include>