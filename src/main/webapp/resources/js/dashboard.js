var dashboardApp =  angular.module("dashboardApp",['angular-bootstrap-modal','ui.bootstrap','720kb.tooltips','commonServiceModule']);

dashboardApp.directive("chloroplethMap",function() {
			function link(scope, el) {
				var el = el[0];
				var last= 0;
				var touchzoom = false;
				var DELAY = 300, clicks = 0, timer = null;

				//this is to clear map popover if it's visible.
				d3.select(".trend-viz").on("mouseover", function() {
					d3.select(".map_popover").style("display", "none");
				});
				
				

				function onmousemove(d) {
					d3
							.select(".map_popover")
							.style("display", "block")
							.style("left", (d3.event.pageX) - 80 + "px")// TODO:
							// make
							// it
							// dynamic
							// so
							// that position would be
							// according to the text
							// length
							.style("top", (d3.event.pageY - 70) + "px")
							.style("opacity", "1");

				}

				function onover(d) {
					d3.selectAll(".activehover").classed("activehover",
							false);
					var rank;
					var value;
					d3.select(".map_popover_content").html(
							"<strong><span style='color:red'>"
									+ d.properties.NAME1_ + "</span></strong>");

					if (d.properties.mapData) {
						var val= String(d.properties.mapData.dataValue)
						if(val.indexOf('.') == -1)
							value= val+'.0'
						else
							value = d.properties.mapData.dataValue;
					} else {
						value= "Not Available";
					}

					d3.select(".map_popover_value").html(
							"<strong>Value:</strong> <span style='color:red'>"
									+ value + "</span>");

					d3.select(this.parentNode.appendChild(this))
							.classed("activehover", true);
				}

				function drilldown(d) {
					
					if (d.properties.NAME1_) {
						scope.closeViz();
						d3.select(".map_popover").style("display",
								"none");
						scope.selectedAreaName=d.properties.NAME1_;
						if(d.properties.mapData !=null)
							scope.hideMap(d);
					}
				}
				scope.mapSetup = function(url, callbackMethod) {
					d3.select(el).selectAll("*").remove();
					var w = scope.getWindowDimensions();
					var feature = "";
					var width = w.w, height = w.h - 190, centered;
					var projection = d3.geo.mercator().scale(1);
					var path = d3.geo.path().projection(projection);

					var svg = d3.select(el).append("svg").attr("id",
							"mapsvg").attr("width", width).attr(
							"height", height);
					svg.append("rect").attr({
						style : "fill:none;pointer-events:all;"
					}).attr("width", width).attr("height", height).on(
							"click", clicked).on(
							"mouseover",
							function() {
								d3.select(".map_popover").style(
										"display", "none");
								d3.selectAll(".activehover").classed(
										"activehover", false);
							});

					var g = svg.append("g").attr("id", "mapg");

					d3
							.json(
									url,
									function(error, us) {
										feature = topojson.feature(us,
												us.objects.layer1);

										var b = path.bounds(feature), s = 0.95 / Math
												.max(
														(b[1][0] - b[0][0])
																/ width,
														(b[1][1] - b[0][1])
																/ height);
										projection.scale(s);
										b = d3.geo.bounds(feature);
										projection
												.center([
														(b[1][0] + b[0][0]) / 2,
														(b[1][1] + b[0][1]) / 2 ]);
										projection
												.translate([ width / 2,
														height / 2 ]);

										g
												.append("g")
												.attr("id", "districts")
												.selectAll("path")
												.data(
														topojson
																.feature(
																		us,
																		us.objects.layer1).features)
												.enter()
												.append("path")
												.attr("d", path)
												.style("cursor", "pointer")
												.on("touchstart", onTouchStart)
												.on("mousedown",
														clickHandler)
												.on("mouseover", onover);
										

										g.on("mousemove", onmousemove);
										
										
										
										

										if(url == 'resources/geomaps/odisha_map.json'){
											g.selectAll("text").data(topojson.feature(us, us.objects.layer1).features)
											.enter().append("svg:text").text(function(d) {
												return d.properties.NAME1_;
											}).attr("x", function(d) {
												if(d.properties.NAME1_ == "Nabarangapur" )
	                                                return  path.centroid(d)[0]/0.97;
												if(d.properties.NAME1_ == "Bargarh" )
													return  path.centroid(d)[0]/0.97;
												if(d.properties.NAME1_ == "Jagatsinghapur" )
													return  path.centroid(d)[0]/1.02;
	                                        	else
	                                                return  path.centroid(d)[0];
	                                        }).attr("y", function(d) {
	                                        	if(d.properties.NAME1_ == "Sambalpur" )
	                                                return  path.centroid(d)[1]/0.9;
	                                        	if(d.properties.NAME1_ == "Sundargarh" )
	                                        		return  path.centroid(d)[1]/1.4;
	                                        	else
	                                        		return path.centroid(d)[1];
	                                        }).attr("text-anchor", "middle").attr('font-size', '0.8vw').attr(
													"fill", "#000").style("cursor", "pointer").on("click", clickHandler);
										}
										
										if (callbackMethod)
											callbackMethod();
									});
					
					function onTouchStart(d){
						if((d3.event.timeStamp- last)<500){
							drilldown(d);
							d3.event.stopImmediatePropagation();
						}else{
							touchzoom = true;
							clicked(d);
						}
						last = d3.event.timeStamp;
					}
					
					function clickHandler(d) {
						clicks++; // count clicks

						if (clicks === 1) {

							timer = setTimeout(function() {
								
								if(!touchzoom){
									clicked(d);
									touchzoom = false;
								} // perform
								// single-click
								// action
								clicks = 0; // after action performed,
								// reset counter
							}, DELAY);

						} else {
							clearTimeout(timer); // prevent
							// single-click
							// action
							drilldown(d); // perform
							// double-click
							// action
							clicks = 0; // after action performed, reset
							// counter
						}
					}

					function clicked(d) {

						var x, y, k;
						if (d && centered !== d) {
							var centroid = path.centroid(d);
							x = centroid[0];
							y = centroid[1];
							k = 2.5;
							centered = d;
						} else {
							x = (width / 2) - 36;// this is to fix
							// the movement of
							// map when clicked.
							y = height / 2;
							k = 1;
							centered = null;
						}

						g.selectAll("path").classed("active",
								centered && function(d) {
									return d === centered;
								});

						g.transition().duration(750).attr(
								"transform",
								"translate(" + width / 2 + "," + height
										/ 2 + ")scale(" + k
										+ ")translate("
										+ (-x - width * 3 / 100) + ","
										+ -y + ")").style(
								"stroke-width", 1.5 / k + "px");
						scope.disablePdf = (d == null) ? false : scope.selectedArea == null ? true : d == scope.selectedArea ? false : true;
					}
				};
				
				function showData(){
					d3
					.select("#mapsvg")
					.selectAll("path")
					.attr(
							"class",
							function(d) {
								if (!(scope.mapData)|| scope.mapData.length==0){
									d.properties.mapData = null;
								}
								for (var i = 0; i < scope.mapData.length; i++) {
									if (d.properties
											&& d.properties.ID_ == scope.mapData[i].areaCode) {
										d.properties.mapData = scope.mapData[i];
										return scope.mapData[i].cssColor;
									} else {
										if (d.properties) {
											d.properties.mapData = null;
										}
									}
								}
							});
				}
				
				scope
						.$watch(
								"mapData",
								function() {
									d3
											.select("#mapsvg")
											.selectAll("path")
											.attr(
													"class",
													function(d) {
														if (!(scope.mapData)|| scope.mapData.length==0){
															d.properties.mapData = null;
														}
														for (var i = 0; i < scope.mapData.length; i++) {
															if (d.properties
																	&& d.properties.ID_ == scope.mapData[i].areaCode) {
																d.properties.mapData = scope.mapData[i];
																return scope.mapData[i].cssColor;
															} else {
																if (d.properties) {
																	d.properties.mapData = null;
																}
															}
														}
													});

								});
				
				scope.$watch(scope.getWindowDimensions, function(
						newValue, oldValue) {
					scope.svgHeight = (newValue.h - 190);
					scope.svgWidth = (newValue.w);
					scope.style = function() {
						return {
							'height' : (newValue.h - 190) + 'px',
							'width' : (newValue.w) + 'px'
						};
					};
					w = scope.getWindowDimensions();
					width = w.w, height = w.h;
					if(newValue != oldValue){
						scope.mapSetup(scope.primary_url, function() {
							showData();
						});
					}
				}, true);
			}
			return {
				link : link,
				restrict : "E"
			};
		});





dashboardApp
.directive(
		"trendChart",
		function($window) {
			function link(scope, el) {

				var el = el[0];
				var clicks = 0;

				// Render graph based on 'data'
				scope.$watch("dataprovider", function(data) {
					// remove all
					d3.select("#trendsvg").remove();
					var margin = {
						top : 20,
						right : 55,
						bottom : 80,
						left : 80
					}, width = $(document).outerWidth() * 39 / 100

					- margin.left - margin.right, height = 300
							- margin.top - margin.bottom;

					// set the ranges
					var x = d3.scale.ordinal().rangeRoundBands(
							[ 0, width ], 1.0);
					var y = d3.scale.linear().rangeRound([ height, 0 ]);

					// define the axis
					var xAxis = d3.svg.axis().scale(x).orient("bottom")
							.ticks(5);
					var yAxis = d3.svg.axis().scale(y).orient("left")
							.ticks(5);

					// // Define the line
					var lineFunction = d3.svg.line().x(function(d) {
						return x(d.timeText);
					}).y(function(d) {
						return y(d.value);
					}).interpolate("cardinal");

					// Adds the svg canvas
					var svg = d3.select(el).append("svg").attr("id",
							"trendsvg").attr("width",
							width + margin.left + margin.right).attr(
							"height",
							height + margin.top + margin.bottom)
							.append("g").attr(
									"transform",
									"translate(" + margin.left + ","
											+ margin.top + ")").style(
									"fill", "#FFFFFF");

					// Get the data
					data.forEach(function(d) {
						d.date = d.timeText;
						d.value = +d.value;
					});

					x.domain(data.map(function(d) {
						return d.timeText;
					}));
					y.domain([ 0, d3.max(data, function(d) {
						return d.value;
					}) ]);

					// Nest the entries by symbol
					var dataNest = d3.nest().key(function(d) {
						return d.source;
					}).entries(data);

					// Loop through each symbol / key
					var color = d3.scale.category10(); // d3.scale.ordinal().range(
					// [ "#bcbd22", "#e377c2" , "#2ca02c",
					// "#475003", "#9c8305" ,"#101b4d" , "#17becf"]);;
					// Add the X Axis
					svg.append("g").attr("class", "x axis").attr(
							"transform", "translate(0," + height + ")")
							.call(xAxis).append("text").attr("x",
									width - margin.right).attr("y",
							margin.bottom-5).attr("dx", ".71em")
							.style("text-anchor", "middle").text(
									"Time Period").style("fill",
									"#FFFFFF");

					d3.selectAll(".tick text").style("text-anchor",
							"end").attr("dx", "-.8em").attr("dy",
							".15em").attr("transform", function(d) {
						return "rotate(-25)";
					});
					
					d3.selectAll('.x .tick')
				    .data(data)
				    .on("mouseover",
								function(d) {
									showPopover.call(this, d,true);
								}).on("mouseout", function(d) {
							removePopovers();
						});

					// xsvg;

					// Add the Y Axis
					svg.append("g").attr("class", "y axis").call(yAxis)
							.append("text").attr("transform",
									"rotate(-90)").attr("y",-margin.left+30).attr(
									"dy", ".71em").style("text-anchor",
									"end").text("Value").style("fill",
									"#FFFFFF");
					
					
					  
					// adding multiple line chart

//					for (var index = 0; index <  dataNest.length; index++) {
					 dataNest.forEach(function(d, i){

						var series = svg.selectAll(".series")
						.data(dataNest[i].values)
						.enter()
						.append("g")
						.attr("class", "series")
						.attr("id","tag" + dataNest[i].key);

						
						series.select(".line").data(function() {
							return dataNest[i].values;
						}).enter().append("path")
						.attr("class", "line")
						.attr("id", "tag" + dataNest[i].key)
						.attr("d",
								function(d) {
									return lineFunction(dataNest[i].values);
										}).style("stroke", function(d) {
									return color(dataNest[i].key);
								}).style("stroke-width", "1.5px").style(
										"fill", "none");

						series.select(".point").data(function() {
							return dataNest[i].values;
						}).enter().append("circle").attr("id",
								"tag" + dataNest[i].key).attr(
								"class", "point").attr("cx",
								function(d) {
									return x(d.timeText);
								}).attr("cy", function(d) {
							return y(d.value);
						}).attr("r", "3px").style("fill", function(d) {
							return color(dataNest[i].key);
						}).style("stroke", "grey").style(
								"stroke-width", "1.5px").on("mouseover",
								function(d) {
									showPopover.call(this, d);
								}).on("mouseout", function(d) {
							removePopovers();
						});

						svg.append("text").attr("x",width-40)// 
						.attr("y", (i * 30) + 10)// ("y", height
						// +
						// (margin.right
						// / 2) + 5)

						.attr("class", "legend").style("fill",
								function() {
									return color(dataNest[i].key);
								}).text(dataNest[i].key);

						svg.append("rect").attr("x",  width - 60)
						.attr("y", i * 30).attr("rx", 2).attr("ry",
								2).attr("width", 10).attr("height", 10)
								.style("fill", function(d) {
									return color(dataNest[i].key);
								}).attr("id", 'rext' + i).attr(
										"key", dataNest[i].key)
								.style("stroke", "grey")
								.on("click", function() { 
									rectClickHandler.call(this);
								});
						
					});

					// End adding multiple line chart

					// click handler for hiding series data
					function rectClickHandler() {

						var disName;
						var fillColor;
						if (d3
								.select(
										"#tag"
												+ dataNest[parseInt($(this)[0].id
														.substr($(this)[0].id.length - 1))].key)
								.style("display") == "none") {
							disName = "block";
							fillColor = color(dataNest[parseInt($(this)[0].id
									.substr($(this)[0].id.length - 1))].key);
						} else {
							disName = "none";
							fillColor = "#464646";
						}
						svg.selectAll("#" + $(this)[0].id + "").style(
								"fill", fillColor);
						svg
								.selectAll(
										"#tag"
												+ dataNest[parseInt($(this)[0].id
														.substr($(this)[0].id.length - 1))].key)
								.style("display", disName);
					}

					function removePopovers() {
						$('.popover').each(function() {
							$(this).remove();
						});
					}
					function showPopover(d,isAxis) {
						$(this).popover(
								{
									title : '',
									placement : 'auto top',
									container : 'body',
									trigger : 'manual',
									html : true,
									content : function() {
										if(isAxis){
											return "Time Period: " + d.time;
										}else{
											return "Time Period: " + d.time
											+ "<br/>Value: "
											+ d.value;
										}
									}
								});
						$(this).popover('show');
					}
		});
			}
			return {
				restrict : "E",
				scope : {
					dataprovider : "="
				},
				link : link
			};

		});