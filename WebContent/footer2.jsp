<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
</div>
</div>
<!--body end-->

<!--footer-->
<div class="footer">
	<div class="w">
		<div class="footer-logo">
			<p>
				备案/许可证编号：</br>蜀ICP备14026305 号
			</p>
		</div>
		<div class="footer-list">
			<div class="footer-list-item" style="width: 240px;">
				<h6>公司地址</h6>
				<ul>
					<li><span class="icon icon-add"></span>地址：成都市高新区天仁路387号大鼎世 </br>
						　　　　　纪广场2号楼1307</li>
					<li><span class="icon icon-phone"></span>Phone:028-85291445</li>
					<li><span class="icon icon-email"></span>Email:service@wishcloud.com.cn</li>
				</ul>
			</div>
			<div class="footer-list-item" style="width: 180px;">
				<h6>关于微讯云通</h6>
				<ul>
					<li><a href="#">核心价值观</a></li>
					<li><a href="#">价值主张</a></li>
					<li><a href="#">发展历程</a></li>
				</ul>
			</div>
			<div class="footer-list-item" style="width: 180px;">
				<h6>合作单位</h6>
				<ul>
					<li><a href="#">成都中讯创新信息技术有限公司</a></li>
					<li><a href="http://www.sugon.com/">曙光信息产业股份有限公司</a></li>
					<li><a href="http://www.cdcloud.org/">成都云计算中心</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
<!--footer end-->
<div class="gotop">
    <a class="gotop-btn" href="javascript:void(0);"></a>
</div>


<!-- <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script> -->
<script type="text/javascript" src="<%=path%>/asset/header/js/jquery.scrollTo.js"></script>
<script type="text/javascript">
    $(function () {


        //回到顶部
        $(".gotop-btn").click(function () {
            $.scrollTo(0, 1000);
        });
        
        //设置菜单项选中事件
        var _url = location.href;
        $('.school-nav li').each(function(){
        	var p = $("a",this).attr("href");
        	if(_url.indexOf(p) != -1){
        		$(this).addClass("on");
        	}
        });
        
        //设置Button按钮显示尺寸
        $(".panel-footer .btn-default").addClass("btn-sm");
        
        //设置表单标题字体
        $(".modal-title").attr({style:"font-size:large"});

    });
    
    var count=0;
    function imgError(obj){
    	if(count ==0){
    		$(".school-logo").attr("src",'<%=path%>/asset/header/img/school-logo.png');
    	}
    	
    	count++;
    }
</script>
</body>
</html>