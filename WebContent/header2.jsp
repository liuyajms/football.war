<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="header_head.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
    $(function(){
//清空导航条，并将当前页面title放入
        $.cookie("path",$('.panel-title').text(),{path:'<%=path%>'});
		$('#keyword').keydown(function(e){
			if(e.keyCode==13){
   				$("#select").click();
			}
		});
    })
     
	var path = "../education_spring/"		
</script>
