
Ext.require([ 'Ext.Window',
              'Ext.form.*',
              'Ext.data.*',
              'Ext.chart.*',
              'Ext.grid.*',
              'Ext.layout.container.Column',
              'Ext.fx.target.Sprite',
              'Ext.layout.container.Fit' ]);

swReportType = {
	CHART : '1',
	MATRIX : '2',
	TABLE : '3'
};
	
swChartType = {
	LINE : "line",
	AREA : "area",
	BAR : "bar",
	COLUMN : "column",
	PIE : "pie",
	GAUGE : "gauge",
	RADAR : "radar",
	SCATTER : "scatter",
	DEFAULT : this.LINE
};

function ReportInfo() 
{
		this.reportType = swReportType.CHART;
		this.chartType = swChartType.DEFAULT;
		this.is3Dimension = false;
		this.isStacked = false;
		this.isChartView = true;
		this.isShowLegend = true;
		this.stringLabelRotation = 'auto';
		this.target = null;
		this.width = 1024/2;
		this.height = 768/2;
		this.columnSpans =  1;
		this.xFieldName = null;
		this.yValueName = null;
		this.xGroupName = null;
		this.yGroupName = null;
		this.groupNames = null;
		this.values = null;
		this.labelRotate = null;	
};

swReportInfo = new ReportInfo();

swReportResizing = false;

Ext.onReady(function () {

	smartChart = {
		companyId : currentUser.companyId,
		userId : currentUser.userId,
		requestUrl : "get_report_data.sw",
		labelFont : '11px dotum,Helvetica,sans-serif',
		reportInfos : {},
	
		getFields : function(target) {
			if(!isEmpty(target)){
				swReportInfo = smartChart.reportInfos[target];
			}
			var fields = new Array();
			fields.push({name: swReportInfo.xFieldName});
			if(!isEmpty(swReportInfo.xGroupName))
				fields.push({name: swReportInfo.xGroupName});
			if(!isEmpty(swReportInfo.yGroupName))
				fields.push({name: swReportInfo.yGroupName});
			for ( var i = 0; i < swReportInfo.groupNames.length; i++)
				fields.push({name: swReportInfo.groupNames[i]});
			return fields;
		},
		
		getTheme : function(chartType, target){
			if(!isEmpty(target)){
				swReportInfo = smartChart.reportInfos[target];
			}
			if(chartType === swChartType.LINE)
				return "Base";
			else if(chartType === swChartType.AREA)
				return "Base";
			else if(chartType === swChartType.BAR)
				return "Base";
			else if(chartType === swChartType.PIE)
				return "Category2";
			else if(chartType === swChartType.COLUMN)
				return "Base";
			else if(chartType === swChartType.GUAGE)
				return "Base";
			else if(chartType === swChartType.RADAR)
				return "Category2";
			else if(chartType === swChartType.SCATTER)
				return "Base";
		},
		
		getAxes : function(chartType, target) {
			if(!isEmpty(target)){
				swReportInfo = smartChart.reportInfos[target];
			}
			var yAxisPosition = "left";
			var xAxisPosition = "bottom";
			var yAxisGrid = true;
			if(chartType === swChartType.AREA)
				yAxisGrid = false;
			if(chartType === swChartType.BAR){
				yAxisPosition = "bottom";
				xAxisPosition = "left";
			}
			
			if(chartType === swChartType.BAR
					|| chartType === swChartType.PIE
					|| chartType === swChartType.GAUGE
					|| chartType === swChartType.RADAR){
				swReportInfo.labelRotate = {
					font: smartChart.labelFont
				};
			}
			
			var numericLabel = {
					renderer: Ext.util.Format.numberRenderer('0,0'),
					font: smartChart.labelFont
				};
			var stringLabel = {
					font: smartChart.labelFont
				};

			if(chartType === swChartType.PIE) return [];
			else if(chartType === swChartType.RADAR){
				return [{
	                type: 'Radial',
	                position: 'radial',
	                label: {
	                    display: true,
	                    font: smartChart.labelFont
	                }
				}];		
			}else if(chartType === swChartType.GAUGE){
				return [{
	                type: 'gauge',
	                position: 'gauge',
	                title: swReportInfo.xfieldName,
	                minimum: 0,
	                maximum: 100,
	                steps: 10,
	                margin: -10
	            }];
			}else if(chartType === swChartType.SCATTER){
				return [{
					        type: 'Numeric',
					        position: 'left',
					        fields: swReportInfo.groupNames,
					        title: swReportInfo.yValueName,
					        grid: true,
					        minimum: 0,
					        label : numericLabel
					    }, {
					        type: 'Category',
					        position: 'bottom',
					        fields: [ swReportInfo.xFieldName ],
					        title: swReportInfo.xFieldName,
					        label: swReportInfo.labelRotate
					    }];
			}else if(chartType === swChartType.LINE 
					|| chartType === swChartType.AREA
					|| chartType === swChartType.BAR
					|| chartType === swChartType.COLUMN){ 
				return [{
					type : 'Numeric',
					minimum : 0,
					position : yAxisPosition,
					grid : yAxisGrid,
					fields : swReportInfo.groupNames,
					title : swReportInfo.yValueName,
					minorTickSteps : 1,
					label: numericLabel
				}, {
					type : 'Category',
					position : xAxisPosition,
					fields : [ swReportInfo.xFieldName ],
					title : swReportInfo.xFieldName,
					label: swReportInfo.labelRotate
				} ];
			}
		},
	
		getSeriesForPIE : function(index, target){
			if(!isEmpty(target)){
				swReportInfo = smartChart.reportInfos[target];
			}
			var series = new Array();
			series = [{
			    type: swReportInfo.chartType,
			    field: swReportInfo.groupNames[index],
			    showInLegend: true,
			    donut: 20,
			    highlight: {
			      segment: {
			        margin: 20
			      }
			    },
                tips: {
                    trackMouse: true,
                    height : 32,
                    width : 100,
                    renderer: function(storeItem, item) {
                    	var total = 0;
                    	for(var i=0; i<swReportInfo.values.length; i++){
                    		total += swReportInfo.values[i][ swReportInfo.groupNames[index]];
                    	}
                    	this.setTitle(storeItem.data[ swReportInfo.xFieldName] + "<br>" + storeItem.data[swReportInfo.groupNames[index]] + "  (" + Math.round(storeItem.data[swReportInfo.groupNames[index]]/total * 100) + "%)");
                    }
                },
			    label: {
			        field: swReportInfo.xFieldName,
			        display: 'rotate',
			        contrast: true,
			        font: smartChart.labelFont
			    }		}];
		    return series;
		},
		
		getSeries : function(chartType, target) {			
			if(!isEmpty(target)){
				swReportInfo = smartChart.reportInfos[target];
			}
			var markerConfig = {
					type: 'circle',
					radius: 3,
					size: 3,							
				}; 
			var highlight = {
                    size: 7,
                    radius: 7
                };
			var axis = "left";
			if(chartType === swChartType.BAR) axis = "bottom";
			
			if(chartType === swChartType.LINE){
				var series = new Array();
				for(var i=0; i<swReportInfo.groupNames.length; i++){
					series.push({
						type : chartType,
						axis : axis,
						xField : swReportInfo.xFieldName,
						yField : swReportInfo.groupNames[i],
						showInLegend: swReportInfo.is3Dimension,
		                highlight: highlight,
		                markerConfig: markerConfig,
		                style:{
							'stroke-width': 0		                	
		                },
		                tips: {
		                    trackMouse: true,
		                    height : 32,
		                    width : 100,
		                    renderer: function(storeItem, item) {
		                    	this.setTitle(item.series.yField + "<br>" + item.value[1]);
		                    }
		                }
					});
				}
				return series;
			}else if(chartType === swChartType.RADAR){
				var series = new Array();
				for(var i=0; i<swReportInfo.groupNames.length; i++){
					series.push({
						type : chartType,
						xField : swReportInfo.xFieldName,
						yField : swReportInfo.groupNames[i],
						showInLegend: swReportInfo.is3Dimension,
						showMarkers: true,
						markerConfig: markerConfig,
		                tips: {
		                    trackMouse: true,
		                    height : 32,
		                    width : 100,
		                    renderer: function(storeItem, item) {
		                    	this.setTitle(item.series.yField + "<br>" + storeItem.data[item.series.yField] );
		                    }
		                },
						style:{
							'stroke-width': 2,
							fill: 'none'
						}
					});
				}
				return series;
				
			}else if(chartType === swChartType.SCATTER){
				var series = new Array();
				for(var i=0; i<swReportInfo.groupNames.length; i++){
					series.push({
				        type: chartType,
					    showInLegend: swReportInfo.is3Dimension,
		                highlight: highlight,
		                label: {
		                	orientation: swReportInfo.labelOrientation
		                },
		                markerConfig: markerConfig,
		                tips: {
		                    trackMouse: true,
		                    height : 32,
		                    width : 100,
		                    renderer: function(storeItem, item) {
		                    	this.setTitle(item.series.yField + "<br>" + storeItem.data[item.series.yField] );
		                    }
		                },
		                style : {
		                    'stroke-width': 0
		                },
				        axis: 'left',
				        xField: swReportInfo.xFieldName,
				        yField: swReportInfo.groupNames[i]
					});
				}
				return series;
				
			}else if(chartType === swChartType.AREA){
				return [{
					type : chartType,
					axis : axis,
					xField : swReportInfo.xFieldName,
					yField : swReportInfo.groupNames,
				    showInLegend: swReportInfo.is3Dimension,
					highlight : false,
	                tips: {
	                    trackMouse: true,
	                    height : 32,
	                    width : 100,
	                    renderer: function(storeItem, item) {
	                    	this.setTitle(item.storeField + "<br>" + storeItem.data[item.storeField] );
	                    }
	                }
				}];
				
			}else if( chartType === swChartType.GAUGE
					|| chartType === swChartType.COLUMN
					|| chartType === swChartType.BAR){
				return [{
					type : chartType,
	                gutter: 80,
					axis : axis,
					xField : swReportInfo.xFieldName,
					yField : swReportInfo.groupNames,
				    showInLegend: swReportInfo.is3Dimension,
					highlight : true,
					stacked : swReportInfo.isStacked,
	                tips: {
	                    trackMouse: true,
	                    height : 32,
	                    width : 100,
	                    renderer: function(storeItem, item) {
	                    	this.setTitle(item.yField + "<br>" + item.value[1]);
	                    }
	                }
				}];
			}
		},
	
		getChartData : function(reportId, target, removeAfterLoad) {
			$.ajax({
				url : smartChart.requestUrl,
				data : {
					companyId : smartChart.companyId,
					userId : smartChart.userId,
					reportId : reportId
				},
				success : function(data, status, jqXHR) {
					if(data){
						if(!isEmpty(target)){
							swReportInfo = smartChart.reportInfos[target];
						}
						swReportInfo.xFieldName = data.xFieldName;
						swReportInfo.yValueName = data.yValueName;
						swReportInfo.xGroupName = data.xGroupName;
						swReportInfo.yGroupName = data.yGroupName;
						swReportInfo.groupNames = data.groupNames;
						swReportInfo.values = data.values;
						if(swReportInfo.isChartView && swReportInfo.reportType === swReportType.CHART){
							if((swReportInfo.stringLabelRotation === "auto" && (swReportInfo.values.length>12 || swReportInfo.width<600)) || swReportInfo.stringLabelRotation === "rotated" ){
								swReportInfo.labelRotate = {
					                	rotate : {
					                		degrees : 45
					                	},
										font: smartChart.labelFont
					                };
							}else{
								swReportInfo.labelRotate = {
										font: smartChart.labelFont
								};
							}
							
							if(swReportInfo.groupNames.length > 1){
								if(swReportInfo.chartType == swChartType.BAR || swReportInfo.chartType == swChartType.COLUMN){
									$('.js_work_report_view_page .js_stacked_chart').show();
								}else{
									$('.js_work_report_view_page .js_stacked_chart').hide();								
								}
								swReportInfo.is3Dimension = true;
							}else{
								$('.js_work_report_view_page .js_stacked_chart').hide();
								swReportInfo.is3Dimension = false;
							}
							if(!isEmpty(target)){
								smartChart.reportInfos[target] = swReportInfo;
							}
							smartChart.createChart(target);
							if(!isEmpty(target))
								smartChart.resizePane($('#' + target).parent());

						}else if(!swReportInfo.isChartView || swReportInfo.reportType === swReportType.MATRIX){
							if(!isEmpty(target)){
								smartChart.reportInfos[target] = swReportInfo;
							}
							smartChart.createMatrix(target);						
						}else if(swReportInfo.reportType === swReportType.TABLE){
							
						}
						if(!isEmpty(removeAfterLoad)){
							removeAfterLoad.remove();
						}
					}
				},
				error : function(xhr, ajaxOptions, thrownError){
					
				}
			});
		},
	
		load : function(reportType, reportId, chartType, isStacked, target) {
			swReportInfo = new ReportInfo();
			swReportInfo.reportType = reportType;
			if(isEmpty(chartType)) chartType = swChartType.DEFAULT;
			swReportInfo.chartType = chartType;
			swReportInfo.isStacked = isStacked;
			swReportInfo.target = target;
			$('#'+target).html('');
			swReportInfo.width = $('#' + target).width();
			smartChart.getChartData(reportId);
		},
		
		loadPane : function(reportType, reportId, chartType, isStacked, isChartView, isShowLegend, stringLabelRotation, target, columnSpans, removeAfterLoad) {
			var reportInfo = new ReportInfo();
			reportInfo.reportType = reportType;
			if(isEmpty(chartType)) chartType = swChartType.DEFAULT;
			reportInfo.chartType = chartType;
			reportInfo.isStacked = isStacked;
			reportInfo.isChartView = isChartView;
			reportInfo.isShowLegend = isShowLegend;
			reportInfo.stringLabelRotation = stringLabelRotation;
			reportInfo.target = target;
			var targetDiv = $('#'+target);
			reportInfo.width = targetDiv.parents('.js_dashboard_pane_row').width()/columnSpans-5;
			targetDiv.html('').parents('.js_work_report_pane_page').width(reportInfo.width);
			reportInfo.columnSpans = columnSpans;
			smartChart.reportInfos[target] = reportInfo;
			smartChart.getChartData(reportId, target, removeAfterLoad);
		},
		
		loadWithData : function(reportType, data, chartType, isStacked, target) {
			if(isEmpty(swReportInfo)){
				reportInfo = new ReportInfo();
				smartChart.reportInfos[target] = reportInfo;
				swReportInfo = reportInfo;
			}
			swReportInfo.reportType = reportType;
			if(isEmpty(chartType)) chartType = swChartType.DEFAULT;
			swReportInfo.chartType = chartType;
			swReportInfo.isStacked = isStacked;
			swReportInfo.target = target;
			$('#'+target).html('');
			swReportInfo.width = $('#' + target).width();
			if(data){
				swReportInfo.xFieldName = data.xFieldName;
				swReportInfo.yValueName = data.yValueName;
				swReportInfo.xGroupName = data.xGroupName;
				swReportInfo.yGroupName = data.yGroupName;
				swReportInfo.groupNames = data.groupNames;
				swReportInfo.values = data.values;
				if((swReportInfo.stringLabelRotation === "auto" && (swReportInfo.values.length>12 || swReportInfo.width<600)) || swReportInfo.stringLabelRotation === "rotated" ){
					swReportInfo.labelRotate = {
		                	rotate : {
		                		degrees : 45
		                	},
							font: smartChart.labelFont
		                };
				}else{
					swReportInfo.labelRotate = {
							font: smartChart.labelFont
					};
				}
				if(swReportInfo.reportType === swReportType.CHART){
					smartChart.createChart();
				}else if(swReportInfo.reportType === swReportType.MATRIX){
					smartChart.createMatrix();				
					smartChart.resize();
				}else if(swReportInfo.reportType === swReportType.TABLE){
					
				}
			}
		},
		
		reload : function(chartType, isStacked, isChartView){
			swReportInfo.chartType = chartType;
			swReportInfo.isStacked = isStacked;
			$('#'+swReportInfo.target).html('');
			swReportInfo.width = $('#' + swReportInfo.target).width();
			if((swReportInfo.stringLabelRotation === "auto" && (swReportInfo.values.length>12 || swReportInfo.width<600)) || swReportInfo.stringLabelRotation === "rotated" ){
				swReportInfo.labelRotate = {
	                	rotate : {
	                		degrees : 45
	                	},
						font: smartChart.labelFont
	                };
			}else{
				swReportInfo.labelRotate = {
						font: smartChart.labelFont
				};
			}
			if(swReportInfo.reportType === swReportType.CHART && isChartView){
				smartChart.createChart();
			}else if((swReportInfo.reportType === swReportType.MATRIX) || (swReportInfo.reportType === swReportType.CHART && !isChartView)){
				smartChart.createMatrix();				
			}else if(swReportInfo.reportType === swReportType.TABLE){
				
			}
		},
		
		resize : function(){
			$('#'+swReportInfo.target).html('');
			swReportInfo.width = $('#' + swReportInfo.target).width();
			if((swReportInfo.stringLabelRotation === "auto" && (swReportInfo.values.length>12 || swReportInfo.width<600)) || swReportInfo.stringLabelRotation === "rotated" ){
				swReportInfo.labelRotate = {
	                	rotate : {
	                		degrees : 45
	                	},
						font: smartChart.labelFont
	                };
			}else{
				swReportInfo.labelRotate = {
						font: smartChart.labelFont
				};
			}
			
			if(swReportInfo.reportType === swReportType.CHART){
				smartChart.createChart();
			}else if(swReportInfo.reportType === swReportType.MATRIX){
				smartChart.createMatrix();				
			}else if(swReportInfo.reportType === swReportType.TABLE){
				
			}
		},
		
		resizePane : function(workReportPane){
			if(!isEmpty(workReportPane)){
				var target = "chart_target_" + workReportPane.attr('panePosition');
				swReportInfo = smartChart.reportInfos[target];
				swReportInfo.columnSpans = parseInt(workReportPane.attr('paneColumnSpans'));
				var targetDiv = $("#" + target);
				if(isEmpty(targetDiv)) return;
				swReportInfo.width = targetDiv.parents('.js_dashboard_pane_row').width()/swReportInfo.columnSpans-5;
				targetDiv.html('').parents('.js_work_report_pane_page').width(swReportInfo.width);
				if((swReportInfo.stringLabelRotation === "auto" && (swReportInfo.values.length>12 || swReportInfo.width<600)) || swReportInfo.stringLabelRotation === "rotated" ){
					swReportInfo.labelRotate = {
		                	rotate : {
		                		degrees : 45
		                	},
							font: smartChart.labelFont
		                };
				}else{
					swReportInfo.labelRotate = {
							font: smartChart.labelFont
					};
				}
				smartChart.reportInfos[target] = swReportInfo;
				if(swReportInfo.isChartView && swReportInfo.reportType === swReportType.CHART){
					smartChart.createChart(target);
				}else if(!swReportInfo.isChartView || swReportInfo.reportType === swReportType.MATRIX){
					smartChart.createMatrix(target);				
				}else if(swReportInfo.reportType === swReportType.TABLE){
					
				}				
			}else{
				var chartTargetPane = $(".js_chart_target_pane");
				if(isEmpty(chartTargetPane)) return;
				
				for(var i=0; i<chartTargetPane.length; i++){
					var target = $(chartTargetPane[i]).attr('id');
					swReportInfo = smartChart.reportInfos[target];
					var targetDiv = $('#'+target);
					swReportInfo.width = targetDiv.parents('.js_dashboard_pane_row').width()/swReportInfo.columnSpans-5;
					targetDiv.html('').parents('.js_work_report_pane_page').width(swReportInfo.width);
					if((swReportInfo.stringLabelRotation === "auto" && (swReportInfo.values.length>12 || swReportInfo.width<600)) || swReportInfo.stringLabelRotation === "rotated" ){
						swReportInfo.labelRotate = {
			                	rotate : {
			                		degrees : 45
			                	},
								font: smartChart.labelFont
			                };
					}else{
						swReportInfo.labelRotate = {
								font: smartChart.labelFont
						};
					}
					smartChart.reportInfos[target] = swReportInfo;
					if(swReportInfo.isChartView && swReportInfo.reportType === swReportType.CHART){
						smartChart.createChart(target);
					}else if(!swReportInfo.isChartView || swReportInfo.reportType === swReportType.MATRIX){
						smartChart.createMatrix(target);				
					}else if(swReportInfo.reportType === swReportType.TABLE){
						
					}
				}
			}
		},
		
		getColumns : function(target){
			if(!isEmpty(target)){
				swReportInfo = smartChart.reportInfos[target];
			}
        	var columns = new Array();
        	columns.push({
        		id: swReportInfo.xFieldName,
        		text: swReportInfo.xFieldName,
        		flex: 1,
        		sortable: false,
        		dataIndex: swReportInfo.xFieldName,
                summaryType: 'count',
                summaryRenderer: function(value, summaryData, dataIndex) {
                	if(value == swReportInfo.values.length){
                		return smartMessage.get("reportGrandTotal") + '(' + value + ')';
                	}else{
                		return smartMessage.get("reportSubTotal") + '(' + value + ')';
                	}
                }        	});
        	for(var i=0; i<swReportInfo.groupNames.length; i++){
	        	columns.push({
	        		text: swReportInfo.groupNames[i],
	        		align: 'right',
	        		sortable: false,
	                summaryType: 'sum',
	        		dataIndex: swReportInfo.groupNames[i]
	        	});		        		
        	}
        	return columns;			
		},
		
		createMatrix : function(target){
			if(!isEmpty(target)){
				swReportInfo = smartChart.reportInfos[target];
			    Ext.create('Ext.grid.Panel', {
			        renderTo:  Ext.get(swReportInfo.target),
			        collapsible: false,
			        frame: false,
			        frameHeader: false,
			        bodyBorder: false,
			        sortableColumns: false,
			        enableColumnHide: false,
			        enableColumnMove: false,
			        draggable: false,
			        columnLines: false,
					store : Ext.create('Ext.data.JsonStore', {
						fields : smartChart.getFields(target),
						groupField: swReportInfo.xGroupName,
						data : swReportInfo.values
					}),
					width: swReportInfo.width,
					height: swReportInfo.height,
			        features: [{
			            id: 'group',
			            ftype: 'groupingsummary',
			            groupHeaderTpl: swReportInfo.xGroupName + ' : {name}',
			            hideGroupedHeader: false,
			            enableGroupingMenu: false,
			            enableNoGroups: false
			        },{
			            id: 'summary',
			            ftype: 'summary'
			        }],
			        columns: smartChart.getColumns(target)
			    });
			}else{
			    Ext.create('Ext.grid.Panel', {
			        renderTo:  Ext.get(swReportInfo.target),
			        collapsible: false,
			        frame: false,
			        frameHeader: false,
			        bodyBorder: false,
			        sortableColumns: false,
			        enableColumnHide: false,
			        enableColumnMove: false,
			        draggable: false,
			        columnLines: false,
					store : Ext.create('Ext.data.JsonStore', {
						fields : smartChart.getFields(target),
						groupField: swReportInfo.xGroupName,
						data : swReportInfo.values
					}),
					width: swReportInfo.width,
					minHeight: swReportInfo.height,
			        features: [{
			            id: 'group',
			            ftype: 'groupingsummary',
			            groupHeaderTpl: swReportInfo.xGroupName + ' : {name}',
			            hideGroupedHeader: false,
			            enableGroupingMenu: false,
			            enableNoGroups: false
			        },{
			            id: 'summary',
			            ftype: 'summary'
			        }],
			        columns: smartChart.getColumns(target)
			    });
			}
		    var chartTarget = $(".js_work_report_view_page > #chart_target");
		    if(isEmpty(chartTarget)) chartTarget = $(".js_work_report_pane_page > .js_chart_target_pane");

			chartTarget.find("div:first > div").css("border-color", "#c7c7c7");
		    $(".js_work_report_pane_page").css("display", "inline-block");

			var repeatTimeout = 5;
			var intervalId = setInterval(function(){
				if(!isEmpty(chartTarget.find("tr.x-grid-row-summary > td.x-grid-cell")) || repeatTimeout == 0){
					clearInterval(intervalId);
					chartTarget.find("div.x-column-header").css("font-family", "dotum,Helvetica,sans-serif").css("font-size", "12px").css("font-weight", "bold").css("text-align", "center");
					chartTarget.find("div.x-grid-group-title").css("font-family", "dotum,Helvetica,sans-serif").css("font-size", "12px").css("font-weight", "bold");
					chartTarget.find("td.x-grid-cell").css("font-family", "dotum,Helvetica,sans-serif");//.css("font-size", "12px");
					chartTarget.find("tr.x-grid-row-summary > td.x-grid-cell").css("font-family", "dotum,Helvetica,sans-serif").css("font-size", "12px").css("font-weight", "bold");
				}
				repeatTimeout--;
			}, 100);
		},
		
		createChart : function(target){
			if(!isEmpty(target)){
				swReportInfo = smartChart.reportInfos[target];
			}
			
			var legendOption= {
					visible : false
			};
			if(swReportInfo.isShowLegend){
				legendOption = {
						position : 'right'						
				};
			}

			if(swReportInfo.chartType === swChartType.PIE){
				for(var i=0; i< swReportInfo.groupNames.length; i++)
					Ext.create('Ext.chart.Chart',{						
						width: swReportInfo.height-60,
						height: swReportInfo.height,
						margin: '20 10 20 10' ,
				        html: '<span style="font-weight: bold; font-size: 14px; font-family: dotum,Helvetica,sans-serif;">' + swReportInfo.groupNames[i] + '</span>',
						animate: true,
						renderTo : Ext.get(swReportInfo.target),
						store : Ext.create('Ext.data.JsonStore', {
							fields : smartChart.getFields(target),
							data : swReportInfo.values
						}),
						shadow : true,
						axes : smartChart.getAxes(swReportInfo.chartType, target),
						series : smartChart.getSeriesForPIE(i, target)
				  	});
			}else if(swReportInfo.chartType === swChartType.GAUGE){
				for(var i=0; i<swReportInfo.groupNames.length; i++)
					Ext.create('Ext.chart.Chart', {
						width: 300,
						height: 200,
						minHeight: 300,
						minWidth: 200,
			            style: 'background:#fff',
			            animate: {
			                easing: 'elasticIn',
			                duration: 1000
			            },
			            renderTo: Ext.get(swReportInfo.target),
			            store : Ext.create('Ext.data.JsonStore', {
							fields : smartChart.getFields(target),
							data : swReportInfo.values
			            }),
			            
			            insetPadding: 25,
			            flex: 1,					
			            axes: smartChart.getAxes(swReportInfo.chartType, target),
			            series: [{
			                type: swReportInfo.chartType,
			                field: swReportInfo.groupNames[i],
			                donut: 30,
			                colorSet: ['#F49D10', '#ddd']
			            }]
			 				});
	
			}else if(swReportInfo.chartType === swChartType.SCATTER){
					Ext.create('Ext.chart.Chart', {
						width: swReportInfo.width,
						height: swReportInfo.height,
						animate: true,
						theme: 'Category2',
						resizable: true,
			            style: 'background:#fff',
			            renderTo: Ext.get(swReportInfo.target),
			            store : Ext.create('Ext.data.JsonStore', {
							fields : smartChart.getFields(target),
							data : swReportInfo.values
			            }),
			            
						legend : legendOption,
			            flex: 1,					
			            axes: smartChart.getAxes(swReportInfo.chartType, target),
			            series: smartChart.getSeries(swReportInfo.chartType, target)
					});
	
			}else{
				Ext.create('Ext.chart.Chart',{
					width: swReportInfo.width,
					height: swReportInfo.height,
					animate: true,
					theme: 'Category2',
					resizable: false,
					autoSize: true,
					insetPadding: 20,// radar
					renderTo : Ext.get(swReportInfo.target),
					store : Ext.create('Ext.data.JsonStore', {
						fields : smartChart.getFields(target),
						data : swReportInfo.values
					}),
					shadow : true,
					legend : legendOption,
					axes : smartChart.getAxes(swReportInfo.chartType, target),
					series : smartChart.getSeries(swReportInfo.chartType, target)
				});
			}
			$(".js_work_report_view_page text[text='" + swReportInfo.xFieldName + "']").css("font-size", "14px");
			$(".js_work_report_view_page text[text='" + swReportInfo.yValueName + "']").css("font-size", "14px");
			$(".js_work_report_pane_page div.x-surface").css("vertical-align", "top");
			
		    $(".js_work_report_pane_page").css("display", "inline-block");
		    
		}
	};
});
