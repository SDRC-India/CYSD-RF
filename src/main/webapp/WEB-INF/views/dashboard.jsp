<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html lang="en" ng-app="dashboardApp">

<jsp:include page="fragments/headtag.jsp" />

<body>

	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div ng-controller='DashboardController' ng-init="visew='Dashboard'">
			<div class="container-fluid">
				<div class="margin-top-20">
					<ul class="nav nav-tabs">
					  <li ng-class="{active:selectedTab == 'Program'}" ng-click="selectTab('Program')"><a>Programme</a></li>
					  <li ng-class="{active:selectedTab == 'Operation'}" ng-click="selectTab('Operation')"><a>Operations </a></li>
					</ul>
				</div>
				<div class="sectors" data-html2canvas-ignore="true" ng-if="selectedTab == 'Program'">
					<nav class="sector" role="navigation">
						<div class="thumb">
							<button class="navbar-toggle" data-toggsamikshaMaple="collapse"
								data-target="#navsector">
								<span class="icon-bar"></span> <span class="icon-bar"></span> <span
									class="icon-bar"></span>
							</button>
							<div class="sector_wrap">
								<ul class="sectorlists" ng-style="myStyle">
									<li ng-repeat="sector in sectors" class="sectorlist pointorCursor"><a
										ng-class="{active:selectedSector.value == sector.value}"
										ng-click="selectSector(sector)"> {{sector.value}}</a></li>
								</ul>

							</div>
						</div>
					</nav>
					<div class="thumb_slide_ctrl">
						<a href="javascript:void(0)" class="left-arrow"
							ng-click="myStyle={'margin-left': moveLeft()}"
							ng-disabled="leftdisabled"> <i class="fa fa-6 fa-caret-left"
							style="font-size: 2em"></i>
						</a> <a href="javascript:void(0)" class="right-arrow"
							ng-click="myStyle={'margin-left': moveRight()}"
							ng-disabled="rightdisabled"> <i
							class="fa fa-6 fa-caret-right" style="font-size: 2em"></i>
						</a>
					</div>
				</div>
				<div class="sectors" data-html2canvas-ignore="true" ng-if="selectedTab == 'Operation'">
					<nav class="sector" role="navigation">
						<div class="thumb">
							<button class="navbar-toggle" data-toggsamikshaMaple="collapse"
								data-target="#navsector">
								<span class="icon-bar"></span> <span class="icon-bar"></span> <span
									class="icon-bar"></span>
							</button>
							<div class="sector_wrap">
								<ul class="sectorlists" ng-style="myStyle">
									<li ng-repeat="sector in operationSectors" class="sectorlist pointorCursor"><a
										ng-class="{active:selectedOperationSector.value == sector.value}"
										ng-click="selectOperationSector(sector)"> {{sector.value}}</a></li>
								</ul>

							</div>
						</div>
					</nav>
					<div class="thumb_slide_ctrl">
						<a href="javascript:void(0)" class="left-arrow"
							ng-click="myStyle={'margin-left': moveLeft()}"
							ng-disabled="leftdisabled"> <i class="fa fa-6 fa-caret-left"
							style="font-size: 2em"></i>
						</a> <a href="javascript:void(0)" class="right-arrow"
							ng-click="myStyle={'margin-left': moveRight()}"
							ng-disabled="rightdisabled"> <i
							class="fa fa-6 fa-caret-right" style="font-size: 2em"></i>
						</a>
					</div>
				</div>
				<h2 class="page-header cysdColor" data-html2canvas-ignore="true">Dashboard</h2>
				<div ng-show="selectedTab == 'Program'">
					<div id="leftfilter" class="left-div">
						<section class="margin-bottom-15" data-html2canvas-ignore="true">
							<div>
								<label>Select Project:</label>
							</div>
							<div class="btn-toolbar" role="toolbar">
								<div class="input-group">
									<input type="text" placeholder="Select Project"
										ng-model='selectedProject.value' class="form-control mapInput"
										readonly></input>
									<div class="input-group-btn" style="position: relative;">
										<button data-toggle="dropdown" id="ind-list"
											class="btn btn-primary dropdown-toggle mapInput" type="button">
											<i class="fa fa-list"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li ng-repeat='project in allProjects'
												ng-click='selectProject(project)'><a href="">{{project.value}}</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</section>
						<section class="margin-bottom-15" data-html2canvas-ignore="true">
							<div>
								<label>Select Financial Year:</label>
							</div>
							<div class="btn-toolbar" role="toolbar">
								<div class="input-group">
									<input type="text" placeholder="Select Year"
										ng-model="selectedYear.value" class="form-control mapInput"
										readonly></input>
									<div class="input-group-btn" style="position: relative;">
										<button data-toggle="dropdown" id="ind-list"
											class="btn btn-primary dropdown-toggle mapInput" type="button">
											<i class="fa fa-list"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li ng-repeat="tp in timePeriodList" ng-click='selectYear(tp)'>
												<a href="">{{tp.value}}</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</section>
						<section class="margin-bottom-15" data-html2canvas-ignore="true">
							<div>
								<label>Select Quarter:</label>
							</div>
							<div class="btn-toolbar" role="toolbar">
								<div class="input-group">
									<input type="text" placeholder="Select Time Period"
										ng-model="selectedTimePeriod.value"
										class="form-control mapInput" readonly></input>
									<div class="input-group-btn" style="position: relative;">
										<button data-toggle="dropdown" id="ind-list"
											class="btn btn-primary dropdown-toggle mapInput" type="button">
											<i class="fa fa-list"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li ng-repeat="tp in quarterList"
												ng-click='selectTimePeriod(tp)'><a href="">{{tp.value}}</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</section>
						<section class="margin-bottom-15" data-html2canvas-ignore="true">
							<div>
								<label>Select Subsector:</label>
							</div>
							<div class="btn-toolbar" role="toolbar">
								<div class="input-group">
									<input type="text" placeholder="Select Subsector"
										ng-model="selectedSubSector.value"
										class="form-control mapInput" readonly></input>
									<div class="input-group-btn" style="position: relative;">
										<button data-toggle="dropdown" id="ind-list"
											class="btn btn-primary dropdown-toggle mapInput" type="button">
											<i class="fa fa-list"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li ng-repeat='subsector in subSectorList'
												ng-click="selectSubSector(subsector)"><a href="">{{subsector.value}}</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</section>
						<div class="otherBtnDiv" ng-show='showMap'>
							<button type="button" ng-click='showView("state")'
								ng-class="{'btn-primary':selectedView == 'state'}"
								class="btn btn-default  mar-top-15"
								data-html2canvas-ignore="true">State</button>
							<button type="button" ng-click='showView("district")'
								ng-class="{'btn-primary':selectedView == 'district'}"
								class="btn btn-default mar-top-15" data-html2canvas-ignore="true">
								District</button>
						</div>
						<button id="backBtn" type="button" ng-click='selectMapAreaType()'
							ng-hide='showMap' class="btn btn-primary backbtn mar-top-15"
							data-html2canvas-ignore="true">
							<i class="fa fa-lg fa-arrow-circle-o-left"></i>&nbsp;&nbsp;Back
						</button>
					</div>
					<div class="rightPanel">
						<label class="indicatorHeader">Sector</label> <label
							class="indicatorLabel">{{selectedSector.value}}</label> <label
							class="indicatorHeader">Subsector</label> <label
							class="indicatorLabel">{{selectedSubSector.value}}
							(Percent)</label> <label
							ng-if="selectedAreaName!= null && selectedAreaName!=''"
							class="indicatorHeader"> Area</label> <label
							class="indicatorLabel">{{selectedAreaName}}</label>
					</div>
					<section>
						<div class="direction" ng-show='showMap'>
							<img class="img-responsive" alt="Responsive image"
								src="resources/images/north_arrow_new.png">
						</div>
					</section>
					<section id="legendsection" class="legends hide_smooth "
						ng-class="{'show_smooth': legends.length != 0 && legends != null }">
						<h4>LEGEND</h4>
						<ul>
							<li class="legend_list"><span class="legend_key">Less
									than 60%</span> <span class="firstslices legendblock"> </span></li>
							<li class="legend_list"><span class="legend_key">Between
									60%-70%</span> <span class="secondslices legendblock"> </span></li>
							<li class="legend_list"><span class="legend_key">More
									than 70%</span> <span class="fourthslices legendblock"> </span></li>
							<li class="legend_list"><span class="legend_key">No
									data available</span> <span class="fifthslices legendblock"> </span></li>
						</ul>
					</section>
					<div class="map_popover">
						<div class="map_popover_content" id="popoverContent">{{
							grid.dataValue}}</div>
						<div class="map_popover_close"></div>
						<div class="map_popover_value"></div>
					</div>
					<!-- Map loading portion -->
					<div ng-show='showMap'>
						<div style="width: 100%;text-align: center;">
							<p ng-show="selectedView == 'state'">Click on the state to view the corresponding Dashboard <br>
							P.S: This database includes Odisha only.</p>
						</div>						
						<chloropleth-map ng-style="style()"
							style="display:block;"></chloropleth-map>
						<!---- End of map loading portion -------->
					</div>
					<section>
						<div ng-hide='showMap'>
							<div class="col-md-3 col-lg-3 col-sm-3 col-xs-3"></div>
							<div class="col-md-6 col-lg-6 col-sm-6 col-xs-6">
								<div ng-repeat="grid in gridSubSectorList"
									ng-class="grid.outerClass" ng-click="showTrendChart(grid)">
									<div ng-class="{active: selectedGrid.indicatroNid == grid.indicatroNid}" class="{{grid.cssClass}}" tooltips 
										tooltip-template="Value: {{grid.dataValue == null?'Not Available':grid.dataValue}}">
										<label>{{grid.indicatorName}}</label>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-lg-3 col-sm-3 col-xs-3"></div>
						</div>
					</section>
				</div>
				
				<div  ng-show="selectedTab == 'Operation'">
					<div id="leftfilter" class="left-div">
						<section class="margin-bottom-15" data-html2canvas-ignore="true">
							<div>
								<label>Select Project:</label>
							</div>
							<div class="btn-toolbar disableMultiSelect" role="toolbar">
								<div class="input-group">
									<input type="text" placeholder="Select Project"
										ng-model='selectedOperationProject.value' class="form-control mapInput"
										readonly></input>
									<div class="input-group-btn" style="position: relative;">
										<button data-toggle="dropdown" id="ind-list"
											class="btn btn-primary dropdown-toggle mapInput" type="button">
											<i class="fa fa-list"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li ng-repeat='project in allOperationProjects'
												ng-click='selectOperationProject(project)'><a href="">{{project.value}}</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</section>
						<section class="margin-bottom-15" data-html2canvas-ignore="true">
							<div>
								<label>Select Financial Year:</label>
							</div>
							<div class="btn-toolbar" role="toolbar">
								<div class="input-group">
									<input type="text" placeholder="Select Year"
										ng-model="selectedOperationYear.value" class="form-control mapInput"
										readonly></input>
									<div class="input-group-btn" style="position: relative;">
										<button data-toggle="dropdown" id="ind-list"
											class="btn btn-primary dropdown-toggle mapInput" type="button">
											<i class="fa fa-list"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li ng-repeat="tp in operationTimePeriodList" ng-click='selectOperationYear(tp)'>
												<a href="">{{tp.value}}</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</section>
						<section class="margin-bottom-15" data-html2canvas-ignore="true">
							<div>
								<label>Select Quarter:</label>
							</div>
							<div class="btn-toolbar" role="toolbar">
								<div class="input-group">
									<input type="text" placeholder="Select Time Period"
										ng-model="selectedOperationTimePeriod.value"
										class="form-control mapInput" readonly></input>
									<div class="input-group-btn" style="position: relative;">
										<button data-toggle="dropdown" id="ind-list"
											class="btn btn-primary dropdown-toggle mapInput" type="button">
											<i class="fa fa-list"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li ng-repeat="tp in operationQuarterList"
												ng-click='selectOperationTimePeriod(tp)'><a href="">{{tp.value}}</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</section>
						<button type="button"  ng-show="operationView=='Sub-sector'"
							ng-click="operationView = 'Sector'"
							class="btn btn-primary backbtn mar-top-15"
							data-html2canvas-ignore="true">
							<i class="fa fa-lg fa-arrow-circle-o-left"></i>&nbsp;&nbsp;Back
						</button>
					</div>
					<div class="rightPanel">
						<label class="indicatorHeader">Sector</label> <label
							class="indicatorLabel">{{selectedOperationSector.value}}</label> <label
							class="indicatorHeader" ng-if="selectedOperationSubsectorGrid!=''">Subsector</label> <label
							class="indicatorLabel" ng-if="selectedOperationSubsectorGrid!=''">{{selectedOperationSubsectorGrid.indicatorName}}
							(Percent)</label> <label
							ng-if="selectedAreaName!= null && selectedAreaName!=''"
							class="indicatorHeader"> Area</label> <label
							class="indicatorLabel">{{selectedAreaName}}</label>
					</div>
					<div>
						<div class="col-md-3 col-lg-3 col-sm-3 col-xs-3"></div>
						<div class="col-md-6 col-lg-6 col-sm-6 col-xs-6" ng-show="operationView=='Sector'">
							<div ng-repeat="grid in operationSubsectorGridData"
								ng-click="showOperationIndicatorGridView(grid)"
								ng-class="grid.outerClass">
								<div ng-class="{active: selectedGrid.indicatroNid == grid.indicatroNid}" class="{{grid.cssClass}}" tooltips 
									tooltip-template="Value: {{grid.dataValue == null?'Not Available':grid.dataValue}}" style="width: 100%;">
									<label class="margin-top-10 pointorCursor">{{grid.indicatorName}}</label>
								</div>
							</div>
						</div>
						<div class="col-md-6 col-lg-6 col-sm-6 col-xs-6" ng-show="operationView=='Sub-sector'">
							<div ng-repeat="grid in operationIndicatorList"
								ng-click="showOperationTrendChartView(grid)"
								ng-class="grid.outerClass">
								<div ng-class="{active: selectedGrid.indicatroNid == grid.indicatroNid}" class="{{grid.cssClass}}" tooltips 
									tooltip-template="Value: {{grid.dataValue == null?'Not Available':grid.dataValue}}">
									<label class="margin-top-1 pointorCursor">{{grid.indicatorName}}</label>
								</div>
							</div>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-3 col-xs-3"></div>
					</div>
				</div>
			</div>
			<div class="trend-viz animate-show" ng-show="isTrendVisible">
				<button class="close" aria-hidden="true" type="button"
					ng-click="isTrendVisible= false">
					<span class="glyphicon glyphicon-remove-circle"></span>
				</button>

				<div class="container-fluid" ng-show="selectedTab == 'Program'">
					<div class="row show-grid" >
						<div class="col-xs-6 col-md-3 left">
							<h3>{{selectedAreaName}}</h3>
						</div>
						<div class="col-xs-6 col-md-9 middle">
							<h6>{{selectedGrid.indicatorName}} ({{unit}})</h6>
						</div>
					</div>
				</div>
				<div class="container-fluid" ng-show="selectedTab == 'Operation'">
					<div class="row show-grid" >
						<div class="col-xs-6 col-md-9 middle">
							<h6>{{selectedOperationIndicator.indicatorName}} ({{unit}})</h6>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="line-separator"></div>
						<div class="row">
							<div class="col-md-10">
								<trend-chart dataprovider="indicatorList"></trend-chart>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div Style="height: 10px;"></div>
			<div id="noDataModal" class="modal" modal="showNoDataModal"
				ng-show="showNoDataModal">
				<div class="modal-dialog parentOfVerticallyCenteredElement">
					<div class="modal-content verticallyCenteredElement">
						<div class="modal-body">
							<div class="container-fluid">
								<div class="col-md-12 text-center">
									<label>No data available</label>
								</div>
								<div class="col-md-12 text-center margin-top-10">
									<button class="btn btn-default"
										ng-click="showNoDataModal=!showNoDataModal">OK</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="noDataProjectModal" class="modal" modal="showNoProjectDataModal"
				ng-show=showNoProjectDataModal>
				<div class="modal-dialog parentOfVerticallyCenteredElement">
					<div class="modal-content verticallyCenteredElement">
						<div class="modal-body">
							<div class="container-fluid">
								<div class="col-md-12 text-center">
									<label>No data available for the selected specific object</label>
								</div>
								<div class="col-md-12 text-center margin-top-10">
									<button class="btn btn-default"
										ng-click="showNoProjectDataModal=!showNoProjectDataModal">OK</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="circularG" style="position: absolute;top: 45%;left: 44%;" ng-show="showLoaderModal">
				<div id="circularG_1" class="circularG"></div>
				<div id="circularG_2" class="circularG"></div>
				<div id="circularG_3" class="circularG"></div>
				<div id="circularG_4" class="circularG"></div>
				<div id="circularG_5" class="circularG"></div>
				<div id="circularG_6" class="circularG"></div>
				<div id="circularG_7" class="circularG"></div>
				<div id="circularG_8" class="circularG"></div>
			</div>
<!-- 			<div id="loaderModal" class="modal" modal="showLoaderModal" -->
<!-- 				ng-show="showLoaderModal"> -->
<!-- 				<div id="circularG" -->
<!-- 					style="position: fixed; margin-top: 250px; margin-left: 650px;"> -->
<!-- 					<div id="circularG_1" class="circularG"></div> -->
<!-- 					<div id="circularG_2" class="circularG"></div> -->
<!-- 					<div id="circularG_3" class="circularG"></div> -->
<!-- 					<div id="circularG_4" class="circularG"></div> -->
<!-- 					<div id="circularG_5" class="circularG"></div> -->
<!-- 					<div id="circularG_6" class="circularG"></div> -->
<!-- 					<div id="circularG_7" class="circularG"></div> -->
<!-- 					<div id="circularG_8" class="circularG"></div> -->
<!-- 				</div> -->
<!-- 			</div> -->
		</div>
	</div>

	<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
		var="angularmin" />
	<script src="${angularmin}" type="text/javascript"></script>

	<spring:url value="/webjars/d3js/3.4.6/d3.min.js" var="d3js" />
	<script src="${d3js}"></script>

	<spring:url value="/resources/js/topojson.v1.min.js" var="topojson" />
	<script src="${topojson}"></script>
	
	<jsp:include page="fragments/footer.jsp" />
	
	<spring:url value="/resources/js/angular_bootstrap_ui.js" var="bootstrapUI" />
	<script src="${bootstrapUI}"></script>
	
	<spring:url value="/resources/js/ui-bootstrap-tpls-1.3.2.min.js" var="bootstraPtpls" />
	<script src="${bootstraPtpls}"></script>

	<spring:url value="/resources/js/angular-tooltips.js" var="angularTooltips" />
	<script src="${angularTooltips}"></script>

	<spring:url value="/resources/js/dashboard.js" var="dashboardJS" />
	<script src="${dashboardJS}"></script>
	
	<spring:url value="/resources/js/angularController/dashboardController.js" var="dashboardCtrlJS" />
	<script src="${dashboardCtrlJS}"></script>
	
	<spring:url value="/resources/js/angularService/projectService.js" var="projectServiceJS" />
	<script src="${projectServiceJS}"></script>

	<spring:url value="/resources/js/startupscroll.js" var="startupScroll" />
	<script src="${startupScroll}"></script>
</body>

</html>
