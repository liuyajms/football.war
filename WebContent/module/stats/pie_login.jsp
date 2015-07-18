<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp" %>
<script src="../../asset/amcharts/amcharts.js" type="text/javascript"></script>
<script src="../../asset/amcharts/serial.js" type="text/javascript"></script>
 <script src="../../asset/amcharts/pie.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" media="screen"
	href="../../asset/datepicker/datepicker.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="../../asset/validate/validate.css">
<script type="text/javascript"
	src="../../asset/datepicker/bootstrap-datepicker.js"></script>
<script type="text/javascript"
	src="../../asset/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="../../asset/validate/messages_zh.js"></script>
<script type="text/javascript" src="../../asset/spin/spin.js"></script>
<body>
<div style="border:1px solid #999;padding:3px;background:#F0E68C;">
<div class="panel-heading">
	<h3 class="panel-title">登陆比例统计</h3>
</div>
<div class="col-md-3"></div>
<div class="col-md-9">
	<div class="input-group">
		<div div class="col-md-4">
		<input id="beginDate" name="beginDate" type="text" class="form-control required"
				data-date-format="yyyy-mm-dd"  placeholder="起始时间" />
		</div>
		<div div class="col-md-4">
		<input id="endDate" name="endDate" type="text" class="form-control required"
				data-date-format="yyyy-mm-dd"  placeholder="终止时间" />
		</div>
		<div div class="col-md-4">
			<span class="input-group-btn">
			<button class="btn btn-default" type="button" id="select">
				查询
			</button> </span>
		</div>
	</div>
</div>

<div id="piediv01" style="width: 100%; height: 400px;"></div>
</div>
<div class="col-md-12"  style="border:1px solid #999;padding:3px;background:#FFFACD;">
<h5>教师登陆比例</h5>
<div id="piediv02" style="width: 100%; height: 400px;"></div>
</div >
<div class="col-md-12"  style="border:1px solid #999;padding:3px;background:#FFE4E1;">
<h5>家长登陆比例</h5>
<div id="piediv03" style="width: 100%; height: 400px;"></div>
</div>
<div  style="border:1px solid #999;padding:3px;background:#ADD8E6;">
<h5 >时间段内详细数据</h5>
<table id="table" class="table table-striped table-bordered" style="width: 100%;">
	<thead>
		<tr>
			<th name="name" width="100px">条目</th>
			<th name="visits" width="100px">人数</th>


		</tr>
	</thead>
	<tbody />
</table>
</div>
</body>
<script type="text/javascript">
            var chart;
            var chartData = [];
            var chartData2 = [ {
                    "country": "Australia",
                        "litres": 109.9
                }, {
                    "country": "Austria",
                        "litres": 108.3
                }, {
                    "country": "UK",
                        "litres": 65
                }, {
                    "country": "Belgium",
                        "litres": 50
                }]; 
            var date = new Date();
            var bDate = new Date();
            var chartCursor;
            var url ="../../rest/stats/login";
            var logData,pieData=[],pieData1=[],pieData2=[],pieData3=[];/*1.教师 2.家长 3.注册  */
            var schoolId = <%=school.getId()%>;
            var opts = {
				  lines: 12, // The number of lines to draw
				  length: 15, // The length of each line
				  width: 5, // The line thickness
				  radius: 10, // The radius of the inner circle
				  color: '#000', // #rbg or #rrggbb
				  speed: 1, // Rounds per second
				  trail: 100, // Afterglow percentage
				  shadow: true, // Whether to render a shadow
				  top:"500px",
				  left:"800px"
				};
			var spinner;
			$(document).ready(function () {
				spinner = new Spinner(opts).spin($('body')[0]);
				selectCount();
				
				$("#select").click(function() {
					spinner = new Spinner(opts).spin($('body')[0]);
					selectCount();
           			pie1.validateNow();
           			pie1.validateData(); 
           			pie2.validateNow();
           			pie2.validateData(); 
           			pie3.validateNow();
           			pie3.validateData(); 
           			
				});
				
				initDatepicker(); 
				/////////////////////////////////////////////////////////

                
				/*   // PIE CHART
                pie = new AmCharts.AmPieChart();
                pie.dataProvider = pieData;
                pie.titleField = "name";
                pie.valueField = "visits";
                pie.patternField = "pattern";
                pie.outlineColor = "#000000";
                pie.outlineAlpha = 0.6;
                pie.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
				pie.depth3D = 15;
				pie.angle  = 30;
                var legendpie = new AmCharts.AmLegend();
                legendpie.markerBorderColor = "#000000";
                legendpie.switchType = undefined;
                legendpie.align = "center";
                pie.addLegend(legendpie);

                // WRITE
                pie.write("piediv01"); */
            	
            	 		
											
				/////////////////////////////////////////////////////////
				
            });
			/////////////////////////////////////分割线////////////////////////////////////////////////////////////
            // generate some random data, quite different range
            function generateChartData() {
                var firstDate = new Date();
                firstDate.setDate(firstDate.getDate() - 500);

                for (var i = 0; i < 50; i++) {
                    // we create date objects here. In your data, you can have date strings
                    // and then set format of your dates using chart.dataDateFormat property,
                    // however when possible, use date objects, as this will speed up chart rendering.
                    var newDate2 = new Date(firstDate);
                    var newDate = new Date(firstDate);
                    newDate.setDate(newDate.getDate() + i);

                    var visits = Math.round(Math.random() * 40) - 20;

					var d = 9;
					
                    chartData.push({
                        date: newDate,
                        visits: visits,
                        visits2: 9,
                    });
                    
                    chartData2.push({
                        date: newDate,
                        visits: 10,
                    });
                }
            }

            // this method is called when chart is first inited as we listen for "dataUpdated" event
            function zoomChart() {
                // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
                chart.zoomToIndexes(logData.length - 5, logData.length - 1);
            }

            // changes cursor mode from pan to select
            function setPanSelect() {
                if (document.getElementById("rb1").checked) {
                    chartCursor.pan = false;
                    chartCursor.zoomable = true;
                } else {
                    chartCursor.pan = true;
                }
                chart.validateNow();
            }
     
     function selectCount() {
		select();
	}
            
     function select() {
     	bDate.setFullYear(date.getFullYear()-1, date.getMonth(), date.getDate());     	
     	beginDate = ($("#beginDate").val()!="")?$("#beginDate").val():bDate.format('yyyy-MM-dd');
     	endDate = ($("#endDate").val()!="")?$("#endDate").val():date.format('yyyy-MM-dd');
     	if(beginDate>endDate){
			alert("起始时间大于结束时间。。。");
			spinner.stop();//加载进度条关闭
			return;
		}
		restSelect(url, {
			"beginDate" :  beginDate,
			"endDate" :  endDate,
			"schoolId" : schoolId,
		}, function(json) {
			for(var i=0;i<4;i++){
				pieData[i]={};
				pieData[i].visits=0;
				if(i<2){
					pieData1[i]={};
					pieData2[i]={};
					pieData3[i]={};
					pieData1[i].visits=0;
					pieData2[i].visits=0;
					pieData3[i].visits=0;
				}
			}
			pieData[0].name=pieData3[0].name="家长注册人数";
			pieData[1].name=pieData2[0].name="家长登陆人数";
			pieData[2].name=pieData3[1].name="教师注册人数";
			pieData[3].name=pieData1[0].name="教师登陆人数";
			pieData1[1].name="教师未登录人数";
			pieData2[1].name="家长未登录人数";
			//for(var i=0;i<1;i++){
				pieData[0].visits=pieData3[0].visits=json.parents;
				pieData[1].visits=pieData2[0].visits=json.parentsLogin;
				pieData[2].visits=pieData3[1].visits=json.teachers;
				pieData[3].visits=pieData1[0].visits=json.teachersLogin;
				pieData1[1].visits=json.teachers-json.teachersLogin;
				pieData2[1].visits=json.parents-json.parentsLogin;
			//}
			spinner.stop();//加载进度条关闭
			draw();
			refreshTable($("#table"), pieData, null,null,false,null);
			$("#endDate").val(endDate);
			$("#beginDate").val(beginDate);
		});
	}
	
	var restSelectSync = function(url, params, f) {
		$.ajax({
        	url: url,
         	type: "GET",
         	dataType: "json",
         	data: params, 
         	async:false,
         	success:f,
         	error: function(xhr, status, error) {
				f();
			}
     	});
	};/*同步*/
	
			/**
 	* 初始化时间选择器
 	*/
	function initDatepicker() {
		$('#beginDate').add('#endDate').datepicker({
			inputMask : true
		}).on('changeDate', function(ev) {
			$('.datepicker').hide();
				
			});
	}
	
	function draw(){
		 // PIE CHART
                pie1 = new AmCharts.AmPieChart();
                pie1.dataProvider = pieData1;
                pie1.titleField = "name";
                pie1.valueField = "visits";
                pie1.patternField = "pattern";
                pie1.outlineColor = "#000000";
                pie1.outlineAlpha = 0.6;
                pie1.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
				pie1.depth3D = 15;
				pie1.angle  = 30;
                var legendpie1 = new AmCharts.AmLegend();
                legendpie1.markerBorderColor = "#000000";
                legendpie1.switchType = undefined;
                legendpie1.align = "right";
                pie1.addLegend(legendpie1);

                // WRITE
                pie1.write("piediv02");
                
                  // PIE CHART
                pie2 = new AmCharts.AmPieChart();
                pie2.dataProvider = pieData2;
                pie2.titleField = "name";
                pie2.valueField = "visits";
                pie2.patternField = "pattern";
                pie2.outlineColor = "#000000";
                pie2.outlineAlpha = 0.6;
                pie2.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
				pie2.depth3D = 15;
				pie2.angle  = 30;
                var legendpie2 = new AmCharts.AmLegend();
                legendpie2.markerBorderColor = "#000000";
                legendpie2.switchType = undefined;
                legendpie2.align = "right";
                pie2.addLegend(legendpie2);

                // WRITE
                pie2.write("piediv03");
                
                // PIE CHART
                pie3 = new AmCharts.AmPieChart();
                pie3.dataProvider = pieData3;
                pie3.titleField = "name";
                pie3.valueField = "visits";
                pie3.patternField = "pattern";
                pie3.outlineColor = "#000000";
                pie3.outlineAlpha = 0.6;
                pie3.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
				pie3.depth3D = 15;
				pie3.angle  = 30;
                var legendpie3 = new AmCharts.AmLegend();
                legendpie3.markerBorderColor = "#000000";
                legendpie3.switchType = undefined;
                legendpie3.align = "center";
                pie3.addLegend(legendpie3);

                // WRITE
                pie3.write("piediv01");					
	}

        </script>

<%@ include file="../../footer.jsp"%>