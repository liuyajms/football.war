<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../header.jsp"%>
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
<script type="text/javascript" src="../../asset/validate/messages_zh.js"></script>
 <script type="text/javascript" src="../../asset/spin/spin.js"></script>
<body>
	<div style="border:1px solid #999;padding:3px;background:#F0E68C;">
		<div class="panel-heading">
			<h3 class="panel-title">登陆情况统计</h3>
		</div>
		<div class="col-md-4">

				<h5 id="count" style="padding-top: 10px;">时间段内总条数</h5>
		</div>
		<div class="col-md-8">
			<div class="input-group">
				<div div class="col-md-4">
					<input id="beginDate" name="beginDate" type="text"
						class="form-control required" data-date-format="yyyy-mm-dd"
						placeholder="起始时间" />
				</div>
				<div div class="col-md-4">
					<input id="endDate" name="endDate" type="text"
						class="form-control required" data-date-format="yyyy-mm-dd"
						placeholder="终止时间" />
				</div>
				<div div class="col-md-4">
					<span class="input-group-btn">
						<button class="btn btn-default" type="button" id="select">
							<fmt:message key="select" />
						</button> </span>
				</div>
			</div>
		</div>

		<div id="chartdiv" style="width: 100%; height: 400px;"></div>
	</div>
	<div style="border:1px solid #999;padding:3px;background:#ADD8E6;">
		<h5>近十天登陆情况比例</h5>
		<div id="piediv" style="width: 100%; height: 400px;" ></div>


		<table id="table" class="table table-striped table-bordered"
			style="width: 100%;" >
			<thead>
				<tr>
					<th name="date" width="100px">日期</th>
					<th name="parents" width="100px">家长登陆</th>
					<th name="teachers" width="100px">教师登陆</th>

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
		"country" : "Australia",
		"litres" : 109.9
	}, {
		"country" : "Austria",
		"litres" : 108.3
	}, {
		"country" : "UK",
		"litres" : 65
	}, {
		"country" : "Belgium",
		"litres" : 50
	} ];
	var date = new Date();
	var bDate = new Date();
	var chartCursor;
	var url = "../../rest/stats/visit";
	var logData, pieData = [];
	var schoolId =
<%=school.getId()%>
	;
	var count = 0;
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
	$(document)
			.ready(
					function() {
						spinner = new Spinner(opts).spin($('body')[0]);
						selectCount();

						$("#select").click(function() {
							spinner = new Spinner(opts).spin($('body')[0]);
							selectCount();
							chart.dataProvider = logData;
							chart.validateNow();
							chart.validateData();
							pie.validateNow();
							pie.validateData();
						});

						initDatepicker();
						/////////////////////////////////////////////////////////

						
					});
	/////////////////////////////////////分割线////////////////////////////////////////////////////////////
	// generate some random data, quite different range
	function generateChartData() {
		var firstDate = new Date();
		firstDate.setDate(firstDate.getDate() - 500);

		for ( var i = 0; i < 50; i++) {
			// we create date objects here. In your data, you can have date strings
			// and then set format of your dates using chart.dataDateFormat property,
			// however when possible, use date objects, as this will speed up chart rendering.
			var newDate2 = new Date(firstDate);
			var newDate = new Date(firstDate);
			newDate.setDate(newDate.getDate() + i);

			var visits = Math.round(Math.random() * 40) - 20;

			var d = 9;

			chartData.push({
				date : newDate,
				visits : visits,
				visits2 : 9,
			});

			chartData2.push({
				date : newDate,
				visits : 10,
			});
		}
	}

	// this method is called when chart is first inited as we listen for "dataUpdated" event
	function zoomChart() {
		// different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
		chart.zoomToIndexes(logData.length - 40, logData.length - 1);
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
		bDate.setFullYear(date.getFullYear() - 1, date.getMonth(), date
				.getDate());

		beginDate = ($("#beginDate").val() != "") ? $("#beginDate").val()
				: bDate.format('yyyy-MM-dd');
		endDate = ($("#endDate").val() != "") ? $("#endDate").val() : date
				.format('yyyy-MM-dd');
		if(beginDate>endDate){
			alert("起始时间大于结束时间。。。");
			spinner.stop();//加载进度条关闭
			return;
		}
		restSelect(url, {
			"beginDate" : beginDate,
			"endDate" : endDate,
			"schoolId" : schoolId,
		}, function(json) {
			logData = json;
			var jData = [];
			var j = logData.length - 1;
			for ( var i = 0; i < 2; i++) {
				pieData[i] = {};
				pieData[i].visits = 0;
			}
			/* classWeibo: null
			date: "2014-12-22"
			notice: null
			parentWeibo: null
			stars: null
			uploads: null */
			pieData[0].name = "家长登陆";
			pieData[1].name = "教师登陆";
			var n;
			if (j > 10)
				n = 10;
			else
				n = j;
			for ( var i = 0; i < n; i++, j--) {
				jData[i] = logData[j];
				if (jData[i].parents == null)
					jData[i].parents = 0;
				if (jData[i].teachers == null)
					jData[i].teachers = 0;
				pieData[0].visits += jData[i].parents;
				pieData[1].visits += jData[i].teachers;
			}
			for ( var i = 0; i < logData.length; i++) {
				count += logData[i].parents;
				count += logData[i].teachers;
			}
			spinner.stop();//加载进度条关闭
			draw();			
			refreshTable($("#table"), jData, null, null, false, null);
			$("#endDate").val(endDate);
			$("#beginDate").val(beginDate);
			$("#count").text("时间段内总登陆数为 " + count + "人次");
		});
	}

	var restSelectSync = function(url, params, f) {
		$.ajax({
			url : url,
			type : "GET",
			dataType : "json",
			data : params,
			async : false,
			success : f,
			error : function(xhr, status, error) {
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
						pie = new AmCharts.AmPieChart();
						pie.dataProvider = pieData;
						pie.titleField = "name";
						pie.valueField = "visits";
						pie.patternField = "pattern";
						pie.outlineColor = "#000000";
						pie.outlineAlpha = 0.6;
						pie.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
						pie.depth3D = 15;
						pie.angle = 30;
						var legendpie = new AmCharts.AmLegend();
						legendpie.markerBorderColor = "#000000";
						legendpie.switchType = undefined;
						legendpie.align = "center";
						pie.addLegend(legend);

						// WRITE
						pie.write("piediv");

						/////////////////////////////////////////////////////////
						////////////////////////////////分割线//////////////////////////////////////////////////////////
						// generate some data first
						generateChartData();

						AmCharts.shortMonthNames = [ '一月', '二月', '三月', '四月',
								'五月', '六月', '七月', '八月', '九月', '十月', '十一月',
								'十二月' ]

						// SERIAL CHART
						chart = new AmCharts.AmSerialChart();
						chart.pathToImages = "../../asset/amcharts/images/";
						chart.dataProvider = logData;
						//chart.dataProvider = chartData;
						chart.categoryField = "date";

						chart.balloon.bulletSize = 5;

						// listen for "dataUpdated" event (fired when chart is rendered) and call zoomChart method when it happens
						chart.addListener("dataUpdated", zoomChart);

						// AXES
						// category
						var categoryAxis = chart.categoryAxis;
						categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
						categoryAxis.minPeriod = "DD"; // our data is daily, so we set minPeriod to DD
						categoryAxis.dashLength = 1;
						categoryAxis.minorGridEnabled = true;
						categoryAxis.twoLineMode = true;
						categoryAxis.dateFormats = [ {
							period : 'fff',
							format : 'JJ:NN:SS'
						}, {
							period : 'ss',
							format : 'JJ:NN:SS'
						}, {
							period : 'mm',
							format : 'JJ:NN'
						}, {
							period : 'hh',
							format : 'JJ:NN'
						}, {
							period : 'DD',
							format : 'DD'
						}, {
							period : 'WW',
							format : 'DD'
						}, {
							period : 'MM',
							format : 'MMM'
						}, {
							period : 'YYYY',
							format : 'YYYY'
						} ];

						categoryAxis.axisColor = "#DADADA";

						// value
						var valueAxis = new AmCharts.ValueAxis();
						valueAxis.axisAlpha = 0;
						valueAxis.dashLength = 1;
						chart.addValueAxis(valueAxis);

						// GRAPH
						var graph = new AmCharts.AmGraph();
						graph.id = "g1";
						graph.title = "家长登陆";
						graph.valueField = "parents";
						graph.bullet = "round";
						graph.bulletBorderColor = "#000000";
						graph.bulletBorderThickness = 2;
						graph.bulletBorderAlpha = 1;
						graph.lineThickness = 2;
						graph.lineColor = "#FF0000";
						graph.negativeLineColor = "#efcc26";
						graph.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
						chart.addGraph(graph);

						var graph1 = new AmCharts.AmGraph();
						graph1.title = "教师登陆";
						graph1.valueField = "teachers";
						graph1.bullet = "round";
						graph1.bulletBorderColor = "#000000";
						graph1.bulletBorderThickness = 2;
						graph1.bulletBorderAlpha = 1;
						graph1.lineThickness = 2;
						graph1.lineColor = "#00FF00";
						graph1.negativeLineColor = "#efcc26";
						graph1.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
						chart.addGraph(graph1);

						// CURSOR
						chartCursor = new AmCharts.ChartCursor();
						chartCursor.cursorPosition = "mouse";
						chartCursor.pan = true; // set it to fals if you want the cursor to work in "select" mode
						chart.addChartCursor(chartCursor);

						// SCROLLBAR
						var chartScrollbar = new AmCharts.ChartScrollbar();
						chartScrollbar.graph = "g1";
						chartScrollbar.scrollbarHeight = 40;
						chartScrollbar.autoGridCount = true, chart
								.addChartScrollbar(chartScrollbar);

						chart.creditsPosition = "bottom-right";

						// LEGEND
						var legend = new AmCharts.AmLegend();
						legend.bulletType = "round";
						legend.equalWidths = false;
						legend.valueWidth = 80;
						legend.color = "#000000";
						legend.useGraphSettings = true;
						chart.addLegend(legend);

						// WRITE
						chart.write("chartdiv");
	}
</script>

<%@ include file="../../footer.jsp"%>