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
		<h3 class="panel-title">
			<fmt:message key='stats.class.title' />
		</h3>
	</div>
	<div class="col-md-4">
	<h5 id="count" style="padding-top: 10px;"></h5>
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
						<fmt:message key='select' />
					</button> </span>
			</div>
		</div>
	</div>

	<div id="chartdiv" style="width: 100%; height: 400px;"></div>
	</div>
	<div style="border:1px solid #999;padding:3px;background:#ADD8E6;">
	<h5>
		<fmt:message key='stats.class.title1' />
	</h5>

	<div id="piediv" style="width: 100%; height: 400px;" ></div>


	<table id="table" class="table table-striped table-bordered"
		style="width: 100%;" >
		<thead>
			<tr>
				<th name="date" width="100px"><fmt:message
						key='stats.school.date' />
				</th>
				<th name="classWeibo" width="100px"><fmt:message
						key='stats.class.classWeibo' />
				</th>
				<th name="broadcast" width="100px"><fmt:message
						key='stats.class.broadcast' />
				</th>
				<%-- <th name="parentWeibo" width="100px"><fmt:message
						key='stats.class.parentWeibo' />
				</th> --%>
				<th name="stars" width="100px"><fmt:message
						key='stats.class.stars' />
				</th>
				<th name="uploads" width="100px"><fmt:message
						key='stats.class.uploads' />
				</th>

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
		"name" : "Australia",
		"visits" : 109.9
	}, {
		"name" : "Austria",
		"visits" : 108.3
	}, {
		"name" : "UK",
		"visits" : 65
	}, {
		"name" : "Belgium",
		"visits" : 50
	} ];
	var date = new Date();
	var bDate = new Date();
	var chartCursor;
	var url = "../../rest/stats/class";
	var logData, pieData = [];
	var classesId;
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
		classesId = $("#classesId").val();
		restSelect(url, {
			"beginDate" : beginDate,
			"endDate" : endDate,
			"schoolId" : schoolId,
			"classId" : classesId,
		}, function(json) {
			logData = json;
			var jData = [];
			var j = logData.length - 1;
			for ( var i = 0; i < 4; i++) {
				pieData[i] = {};
				pieData[i].visits = 0;
			}
			/* classWeibo: null
			date: "2014-12-22"
			broadcast: null
			parentWeibo: null
			stars: null
			uploads: null */
			pieData[0].name = "<fmt:message key='stats.class.classWeibo'/>";
			pieData[1].name = "<fmt:message key='stats.class.broadcast'/>";
			//pieData[2].name = "<fmt:message key='stats.class.parentWeibo'/>";
			pieData[2].name = "<fmt:message key='stats.class.stars'/>";
			pieData[3].name = "<fmt:message key='stats.class.uploads'/>";
			for ( var i = 0; i < 10; i++, j--) {
				jData[i] = logData[j];
				if (jData[i].classWeibo == null)
					jData[i].classWeibo = 0;
				if (jData[i].broadcast == null)
					jData[i].broadcast = 0;
				/* if (jData[i].parentWeibo == null)
					jData[i].parentWeibo = 0; */
				if (jData[i].stars == null)
					jData[i].stars = 0;
				if (jData[i].uploads == null)
					jData[i].uploads = 0;
				pieData[0].visits += jData[i].classWeibo;
				pieData[1].visits += jData[i].broadcast;
				//pieData[2].visits += jData[i].parentWeibo;
				pieData[2].visits += jData[i].stars;
				pieData[3].visits += jData[i].uploads;
			}
			for ( var i = 0; i < logData.length; i++) {
				count += logData[i].classWeibo;
				count += logData[i].broadcast;
				//count += logData[i].parentWeibo;
				count += logData[i].stars;
				count += logData[i].uploads;
			}
			spinner.stop();//加载进度条关闭
			draw();
			refreshTable($("#table"), jData, null, null, false, null);
			$("#endDate").val(endDate);
			$("#beginDate").val(beginDate);
			$("#count").text("时间段内该班内总发送条目数为" + count + "条");
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
						//pie.dataProvider = chartData2;
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
						graph.title = "<fmt:message key='stats.class.classWeibo'/>";
						graph.valueField = "classWeibo";
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
						graph.id = "g2";
						graph1.title = "<fmt:message key='stats.class.broadcast'/>";
						graph1.valueField = "broadcast";
						graph1.bullet = "round";
						graph1.bulletBorderColor = "#000000";
						graph1.bulletBorderThickness = 2;
						graph1.bulletBorderAlpha = 1;
						graph1.lineThickness = 2;
						graph1.lineColor = "#00FF00";
						graph1.negativeLineColor = "#efcc26";
						graph1.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
						chart.addGraph(graph1);

						var graph2 = new AmCharts.AmGraph();
						graph2.id = "g3";
						graph2.title = "<fmt:message key='stats.class.parentWeibo'/>";
						graph2.valueField = "parentWeibo";
						graph2.bullet = "round";
						graph2.bulletBorderColor = "#000000";
						graph2.bulletBorderThickness = 2;
						graph2.bulletBorderAlpha = 1;
						graph2.lineThickness = 2;
						graph2.lineColor = "#0000FF";
						graph2.negativeLineColor = "#efcc26";
						graph2.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
						//chart.addGraph(graph2);

						var graph3 = new AmCharts.AmGraph();
						graph3.id = "g4";
						graph3.title = "<fmt:message key='stats.class.stars'/>";
						graph3.valueField = "stars";
						graph3.bullet = "round";
						graph3.bulletBorderColor = "#000000";
						graph3.bulletBorderThickness = 2;
						graph3.bulletBorderAlpha = 1;
						graph3.lineThickness = 2;
						graph3.lineColor = "#FFFF00";
						graph3.negativeLineColor = "#efcc26";
						graph3.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
						chart.addGraph(graph3);

						var graph4 = new AmCharts.AmGraph();
						graph4.id = "g5";
						graph4.title = "<fmt:message key='stats.class.uploads'/>";
						graph4.valueField = "uploads";
						graph4.bullet = "round";
						graph4.bulletBorderColor = "#000000";
						graph4.bulletBorderThickness = 2;
						graph4.bulletBorderAlpha = 1;
						graph4.lineThickness = 2;
						graph4.lineColor = "#000000";
						graph4.negativeLineColor = "#efcc26";
						graph4.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
						chart.addGraph(graph4);
						// CURSOR
						chartCursor = new AmCharts.ChartCursor();
						chartCursor.cursorPosition = "mouse";
						chartCursor.pan = true; // set it to fals if you want the cursor to work in "select" mode
						chart.addChartCursor(chartCursor);

						// SCROLLBAR
						var chartScrollbar = new AmCharts.ChartScrollbar();
						/* chartScrollbar.graph="g1";
						chartScrollbar.scrollbarHeight=40; */
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