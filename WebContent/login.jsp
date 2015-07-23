<%@ page import="java.util.UUID" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />


<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="renderer" content="webkit" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<script src="asset/jquery/jquery-1.11.1.min.js"></script>
<script src="asset/jquery/jquery.cookie.js"></script>
<script src="asset/jquery/jquery.form.js"></script>

<link rel="stylesheet" href="asset/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" href="asset/bootstrap/bootstrap-theme.min.css" />


<script src="asset/bootstrap/bootstrap.min.js"></script>

<style>
.icon {
	width: 16px;
	height: 16px;
	margin-right: 8px;
}

.bs-docs-nav .navbar-toggle .icon-bar {
	background-color: #4E8120;
}
</style>

<script src="asset/application/rest.js"></script>
<script src="asset/application/application.js"></script>

<script>
	function quit() {
		if (confirm("你确定吗？")) {
			restDelete("rest/auth", function() {
				location.href = "login.jsp";
			});
		}
	}
</script>
<script type="text/javascript">
    var EDUCATION_HOST='http://localhost:8080/education_spring';

	$(document).ready(function() {

		$.cookie("rsessionid","<%=UUID.randomUUID().toString()%>");
        $.cookie("code", "eb45324a84d32182e74ac80c71d6f1dc");

		var login = function(username, password) {
			restInsert("rest/auth", {
//				schoolId : schoolId,
				username : username,
				password : password
			}, null, function(data) {
				if (data == null || data == "") {
					alert("登录失败！");
				} else {
					var json = "[";
					$.each(data.classes, function(i, n) {
						if (i > 0) {
							json += ",";
						}
						json += "{";
						json += "\"id\":\"" + n.id + "\",";
						json += "\"name\":\"" + n.name + "\"";
						json += "}";
					});
					json += "]";
					$.cookie("classes", json);
					$.cookie("rsessionid", data.rsessionid);
					location.href = "index.jsp";
				}
			});
		};
		

		$("#button").click(function() {
            login($("#username").val(), $("#password").val());
			/*restSelect("rest/auth", {
				username : $("#username").val(),
				password : $("#password").val(),
				imgCode:$("#imgCode").val()
			}, function(data) {
				var json=[];
				for(var i=0,j=0;i<data.length;i++){
					if(data[i].type==0||data[i].type==0){
						json[j]=data[i];
						j++;			
					}
				}
				if (json != null && json.length > 0) {
					// TODO 如果一个教师在多个学校，则首先需要选择学校，然后通过学校登陆进去，暂时不考虑一个教师在多个学校的情况
					if(json.length > 1){
                        initSelectBox(json);
                    }else{
                        login(json[0].schoolId, $("#username").val(), $("#password").val());
                    }
				} else {
					alert("登录失败！");
				}
			});*/
		});
		
		$('#img').on('click',function(){
            $(this).attr('src','rest/auth/imgCode?'+Math.random());
        })

        $('#img').click();

        $('#submit').on('click',function(){
            	login($('#school_name').val(), $("#username").val(), $("#password").val());
        });
        
        $('#imgCode').add('#password').add('#username').keydown(function(e){
			if(e.keyCode==13){
   			$("#button").click();
			}
		});
		
	});

    function initSelectBox(data){
        $('select').html('');
        for(var i=0; i<data.length; i++){
            if(data[i].type != 10){
                $('select').append('<option value='+data[i].schoolId+' type='+data[i].type+'>'+ data[i].schoolName+'('+data[i].name+')' + '</option>');
            }else{
            	$('select').append('<option value='+data[i].educationId+' type='+data[i].type+'>'+ data[i].educationName+'('+data[i].name+')' + '</option>');
            }
        }
        if($('select option').length > 1){
            showDialog($("#dialog"));
        }else{
            $('#submit').click();
        }
    }
</script>
</head>
<body style="background: url(asset/application/images/background.png) repeat;">

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-mg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="title">请选择登陆的学校</h4>
            </div>
            <div class="modal-body">
                <form role="form" id="form" method="post" class="form-horizontal">
                    <div class="form-group"  id="projOld">
                        <label for="school_name" class="col-sm-2 control-label">学校名</label>
                        <div class="col-sm-9">
                            <select class="form-control" required="true" id="school_name">
                                <!--  <option value=""></option> -->
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="submit" data-loading-text="保存中...">保存</button>
            </div>
        </div>
    </div>
</div>

	<div class="container_flud" style="background-color: rgba(78, 129, 32, 0.7); margin-top: 100px; padding: 20px;">
		<div class="container">
			<div clsss="row">
				<div class="col-md-10">
					<h4 style="color: #fff;">微讯云通 登录</h4>
				</div>
				<div class="col-md-2"></div>
			</div>
			<div clsss="row">
				<div class="col-md-3">
					<input class="form-control" name="username" id="username" type="text" placeholder="用户名" value="13882115937"/>
				</div>
				<div class="col-md-3">
					<input class="form-control" name="password" id="password" type="password" placeholder="密码" value="1234"/>
				</div>
				 <div class="col-md-1">
                    <img src="" id="img"/>
                </div>
                <div class="col-md-2">
                    <input class="form-control" name="imgCode" id="imgCode"
                           type="text" placeholder="验证码" value="" />
                </div>
				<div class="col-md-2">
					<input class="btn btn-default btn-block" id="button" type="button" value="登&nbsp;&nbsp;&nbsp;&nbsp;录" />
				</div>
			</div>
			<div clsss="row" style="margin-top:100px;">
				<div class="col-md-0"></div>
				<div class="col-md-12">
					<h5 style="color: #fff;">请使用最新的浏览器，如<a href="http://www.google.cn/chrome/" target="_blank" style="color:#fff;">Google Chrome</a>, Apple Safari, <a href="http://ie.microsoft.com" target="_blank" style="color:#fff;">IE</a>最新版本；如您正在使用 搜狗、360、百度、腾讯等支持高速模式的浏览器，则请点击顶部地址栏右边的切换按钮，切换到 <span style="font-weight:bold;color:#ccc;">高速模式</span></h5>
					<h5 style="color: #fff; display: none;">找回密码</h5>
				</div>
			</div>
		</div>
	</div>
</body>
</html>