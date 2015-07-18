<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<div id="gpic" style="overflow:hidden; width:auto; height:550px; margin-left: auto; margin-right: auto;">

<table border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td id="gpic1" valign="top" align="center"><table width="1000" border="0" align='center' cellpadding="0" cellspacing="0">
        <tr>
          <td><img src="asset/application/images/inner_1.png" style="width:350px;height:550px;margin-right:5px;"></td>
          <td><img src="asset/application/images/inner_2.png" style="width:350px;height:550px;margin-right:5px;"></td>
          <td><img src="asset/application/images/inner_3.png" style="width:350px;height:550px;margin-right:5px;"></td>
          <td><img src="asset/application/images/inner_4.png" style="width:350px;height:550px;margin-right:5px;"></td>
        </tr>
      </table></td>
    <td id="gpic2" valign="top"></td>
  </tr>
</table>
</div>

<script>
var speed=40;
gpic2.innerHTML=gpic1.innerHTML;
function Marquee(){
	if(gpic2.offsetWidth-gpic.scrollLeft<=0)
		gpic.scrollLeft-=gpic1.offsetWidth;
	else{
		gpic.scrollLeft++;
	}
}
var MyMar=setInterval(Marquee,speed);
gpic.onmouseover=function() {clearInterval(MyMar)}
gpic.onmouseout=function() {MyMar=setInterval(Marquee,speed)}
</script>

<%@ include file="footer.jsp" %>

