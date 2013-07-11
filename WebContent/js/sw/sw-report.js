
Ext.require([ 'Ext.Window',
              'Ext.form.*',
              'Ext.data.*',
              'Ext.chart.*',
              'Ext.grid.Panel',
              'Ext.layout.container.Column',
              'Ext.fx.target.Sprite',
              'Ext.layout.container.Fit' ]);
Ext.onReady(function () {
	swReportType = {
		CHART : 1,
		MATRIX : 2,
		TABLE : 3
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
	
	smartChart = {
		isResizing : false,
		reportType : swReportType.CHART,
		chartType : swChartType.DEFAULT,
		is3Dimension : false,
		isStacked : false,
		target : null,
		width : 1024/2,
		height : 768/2,
		companyId : currentUser.companyId,
		userId : currentUser.userId,
		xFieldName : null,
		yValueName : null,
		groupNames : null,
		values : null,
		requestUrl : "get_report_data.sw",
		labelFont : '11px Arial',
		labelRotate : null,
	
		getFields : function() {
			var fields = new Array();
			fields.push({name: smartChart.xFieldName});
			for ( var i = 0; i < smartChart.groupNames.length; i++)
				fields.push({name: smartChart.groupNames[i]});
			return fields;
		},
		
		getTheme : function(chartType){
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
		
		getAxes : function(chartType) {
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
				labelRotate = null;
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
	                title: smartChart.xfieldName,
	                minimum: 0,
	                maximum: 100,
	                steps: 10,
	                margin: -10
	            }];
			}else if(chartType === swChartType.SCATTER){
				return [{
					        type: 'Numeric',
					        position: 'left',
					        fields: smartChart.groupNames,
					        title: smartChart.yValueName,
					        grid: true,
					        minimum: 0,
					        label : numericLabel
					    }, {
					        type: 'Category',
					        position: 'bottom',
					        fields: [ smartChart.xFieldName ],
					        title: smartChart.xFieldName,
					        label: stringLabel
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
					fields : smartChart.groupNames,
					title : smartChart.yValueName,
					minorTickSteps : 1,
					label: numericLabel
				}, {
					type : 'Category',
					position : xAxisPosition,
					fields : [ smartChart.xFieldName ],
					title : smartChart.xFieldName,
	                label: stringLabel
				} ];
			}
		},
	
		getSeriesForPIE : function(index){
			var series = new Array();
			series = [{
			    type: smartChart.chartType,
			    field: smartChart.groupNames[index],
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
                    	for(var i=0; i<smartChart.values.length; i++){
                    		total += smartChart.values[i][ smartChart.groupNames[index]];
                    	}
                    	this.setTitle(storeItem.data[ smartChart.xFieldName] + "<br>" + storeItem.data[smartChart.groupNames[index]] + "  (" + Math.round(storeItem.data[smartChart.groupNames[index]]/total * 100) + "%)");
                    }
                },
			    label: {
			        field: smartChart.xFieldName,
			        display: 'rotate',
			        contrast: true,
			        font: smartChart.labelFont
			    }		}];
		    return series;
		},
		
		getSeries : function(chartType) {			
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
				for(var i=0; i<smartChart.groupNames.length; i++){
					series.push({
						type : chartType,
						axis : axis,
						xField : smartChart.xFieldName,
						yField : smartChart.groupNames[i],
						showInLegend: smartChart.is3Dimension,
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
				for(var i=0; i<smartChart.groupNames.length; i++){
					series.push({
						type : chartType,
						xField : smartChart.xFieldName,
						yField : smartChart.groupNames[i],
						showInLegend: smartChart.is3Dimension,
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
				for(var i=0; i<smartChart.groupNames.length; i++){
					series.push({
				        type: chartType,
					    showInLegend: smartChart.is3Dimension,
		                highlight: highlight,
		                label: {
		                	orientation: smartChart.labelOrientation
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
				        xField: smartChart.xFieldName,
				        yField: smartChart.groupNames[i]
					});
				}
				return series;
				
			}else if(chartType === swChartType.AREA){
				return [{
					type : chartType,
					axis : axis,
					xField : smartChart.xFieldName,
					yField : smartChart.groupNames,
				    showInLegend: smartChart.is3Dimension,
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
					xField : smartChart.xFieldName,
					yField : smartChart.groupNames,
				    showInLegend: smartChart.is3Dimension,
					highlight : true,
					stacked : smartChart.isStacked,
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
	
		getChartData : function(reportId) {
			$.ajax({
				url : smartChart.requestUrl,
				data : {
					companyId : smartChart.companyId,
					userId : smartChart.userId,
					reportId : reportId
				},
				success : function(data, status, jqXHR) {
					if(data){
						smartChart.xFieldName = data.xFieldName;
						smartChart.yValueName = data.yValueName;
						smartChart.groupNames = data.groupNames;
						smartChart.values = data.values;
						if(data.values.length>15){
							smartChart.labelRotate = {
				                	rotate : {
				                		degrees : 270
				                	}
				                };
						}else{
							smartChart.labelRotate = null;
						}
						
						if(smartChart.groupNames.length > 1){
							if(smartChart.chartType == swChartType.BAR || smartChart.chartType == swChartType.COLUMN){
								$('.js_work_report_view_page .js_stacked_chart').show();
							}else{
								$('.js_work_report_view_page .js_stacked_chart').hide();								
							}
							smartChart.is3Dimension = true;
						}else{
							$('.js_work_report_view_page .js_stacked_chart').hide();
							smartChart.is3Dimension = false;
						}
						smartChart.createChart();
					}
				},
				error : function(xhr, ajaxOptions, thrownError){
					
				}
			});
		},
	
		load : function(reportType, reportId, chartType, isStacked, target) {
			smartChart.reportType = reportType;
			if(isEmpty(chartType)) chartType = swChartType.DEFAULT;
			smartChart.chartType = chartType;
			smartChart.isStacked = isStacked;
			smartChart.target = target;
			$('#'+target).html('');
			smartChart.width = $('#' + target).width();
			smartChart.getChartData(reportId);
		},
		
		loadWithData : function(reportType, data, chartType, isStacked, target) {
			smartChart.reportType = reportType;
			if(isEmpty(chartType)) chartType = swChartType.DEFAULT;
			smartChart.chartType = chartType;
			smartChart.isStacked = isStacked;
			smartChart.target = target;
			$('#'+target).html('');
			smartChart.width = $('#' + target).width();
			if(data){
				smartChart.xFieldName = data.xFieldName;
				smartChart.yValueName = data.yValueName;
				smartChart.groupNames = data.groupNames;
				smartChart.values = data.values;
				smartChart.createChart();
			}
		},
		
		reload : function(chartType, isStacked){
			smartChart.chartType = chartType;
			smartChart.isStacked = isStacked;
			$('#'+smartChart.target).html('');
			smartChart.width = $('#' + smartChart.target).width();
			smartChart.createChart();
		},
		
		resize : function(){
			$('#'+smartChart.target).html('');
			smartChart.width = $('#' + smartChart.target).width();
			smartChart.createChart();
		},
		
		getColumns : function(){
        	var columns = new Array();
        	columns.push({
        		id: smartChart.xFieldName,
        		text: smartChart.xFieldName,
        		flex: 1,
        		sortable: true,
        		dataIndex: smartChart.xFieldName
        	});
        	for(var i=0; i<smartChart.groupNames.length; i++){
	        	columns.push({
	        		text: smartChart.groupNames[i],
	        		align: 'right',
	        		sortable: true,
	        		dataIndex: smartChart.groupNames[i]
	        	});		        		
        	}
        	return columns;			
		},
		
		createChart : function(){

			if(smartChart.chartType === swChartType.PIE){
				for(var i=0; i< smartChart.groupNames.length; i++)
					Ext.create('Ext.chart.Chart',{						
						width: smartChart.height-60,
						height: smartChart.height,
						margin: '20 10 20 10' ,
				        html: '<span style="font-weight: bold; font-size: 14px; font-family: Arial;">' + smartChart.groupNames[i] + '</span>',
						animate: true,
						renderTo : Ext.get(smartChart.target),
						store : Ext.create('Ext.data.JsonStore', {
							fields : smartChart.getFields(),
							data : smartChart.values
						}),
						shadow : true,
						axes : smartChart.getAxes(smartChart.chartType),
						series : smartChart.getSeriesForPIE(i)
				  	});
			}else if(smartChart.chartType === swChartType.GAUGE){
				for(var i=0; i<smartChart.groupNames.length; i++)
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
			            renderTo: Ext.get(smartChart.target),
			            store : Ext.create('Ext.data.JsonStore', {
							fields : smartChart.getFields(),
							data : smartChart.values
			            }),
			            
			            insetPadding: 25,
			            flex: 1,					
			            axes: smartChart.getAxes(smartChart.chartType),
			            series: [{
			                type: smartChart.chartType,
			                field: smartChart.groupNames[i],
			                donut: 30,
			                colorSet: ['#F49D10', '#ddd']
			            }]
			 				});
	
			}else if(smartChart.chartType === swChartType.SCATTER){
					Ext.create('Ext.chart.Chart', {
						width: smartChart.width,
						height: smartChart.height,
						animate: true,
						theme: 'Category2',
						resizable: true,
			            style: 'background:#fff',
			            renderTo: Ext.get(smartChart.target),
			            store : Ext.create('Ext.data.JsonStore', {
							fields : smartChart.getFields(),
							data : smartChart.values
			            }),
			            
						legend : {
							position : 'right'
						},
			            flex: 1,					
			            axes: smartChart.getAxes(smartChart.chartType),
			            series: smartChart.getSeries(smartChart.chartType)
					});
	
			}else{
				Ext.create('Ext.chart.Chart',{
					width: smartChart.width,
					height: smartChart.height,
					animate: true,
					theme: 'Category2',
					resizable: false,
					autoSize: true,
					insetPadding: 20,// radar
					renderTo : Ext.get(smartChart.target),
					store : Ext.create('Ext.data.JsonStore', {
						fields : smartChart.getFields(),
						data : smartChart.values
					}),
					shadow : true,
					legend : {
						position : 'right'
					},
					axes : smartChart.getAxes(smartChart.chartType),
					series : smartChart.getSeries(smartChart.chartType)
				});
			}
			$(".js_work_report_view_page text[text='" + smartChart.xFieldName + "']").css("font-size", "14px");
			$(".js_work_report_view_page text[text='" + smartChart.yValueName + "']").css("font-size", "14px");
		}
	};
});
