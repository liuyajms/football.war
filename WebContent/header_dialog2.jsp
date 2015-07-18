<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="header_head.jsp"%>

<script type="text/javascript">

$(function(){

    var _count = -1;
    var _title = $('.panel-title').html();
    var _pathArray = $.cookie('path').split(",");
    //判断路径导航条中是否有此记录
    for(var i=0; i< _pathArray.length; i++){
        if( _pathArray[i] == _title){
            _count = i;
            //重新设置path
            _pathArray = _pathArray.slice(0,i);
        }
    }

	console.log(_pathArray);

    var _str = '',_len=_pathArray.length;
    for(var i=0; i< _len; i++){
       _str +=  '<a href="javascript:history.go(-'+ (_len-i) +')">' + _pathArray[i] +'</a>' + ' > ';
    }
    _str += _title;
    $('.panel-title').html(_str);

    //将当前title加入path
    $.cookie('path',_pathArray+','+_title,{path:'<%=path%>'});

	
})
</script>