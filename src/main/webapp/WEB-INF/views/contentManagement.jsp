<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html lang="en" ng-app="cmsApp">
<script type="text/ng-template" id="nodes_renderer.html">
  <div id="uiTree" ui-tree class="tree-node tree-node-content">
    <a class="btn btn-success btn-xs" ng-if="node.children && node.children.length > 0" data-nodrag ng-click="toggle(this)"><span
        class="glyphicon"
         	ng-class="{
          'glyphicon-chevron-right': collapsed,
          'glyphicon-chevron-down': !collapsed}"></span></a>
 	<a id="childEl" class="btn btn-success btn-xs" ng-if="node.children.length == 0" data-nodrag ng-click="selectCategory(node.categoryName)">
		<span>*</span>
	</a>
   {{node.categoryName}}
  </div>
  <ol ui-tree-nodes="" ng-model="node.children" ng-class="{hidden: collapsed}">
    <li ng-repeat="node in node.children" ui-tree-node ng-include="'nodes_renderer.html'">
    </li>
  </ol>
</script>
<jsp:include page="fragments/headtag.jsp" />
<body>
	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div ng-controller='CMSController'>
			<div class="container-fluid">
				<div class="col-md-4" >

					<!-- 			---ui-tree code starts here -->

					<div class="row">
						<div class="col-sm-12">
							<h3>CYSD CMS</h3>

							<button ng-click="expandAll()">Expand all</button>
							<button ng-click="collapseAll()">Collapse all</button>
						</div>
					</div>

					<div ui-tree id="tree-root" style="margin-top: 20px;" >
						<ol ui-tree-nodes="" ng-model="categoryData"
							data-nodrop-enabled="true" >
							<li ng-repeat="node in categoryData" ui-tree-node data-nodrag
								ng-include="'nodes_renderer.html'">{{node.categoryName}}</li>

						</ol>
					</div>
				</div>
				<div class="col-md-8" ng-show=isFilesVisible>
					<ul style="margin-top: 20px;" id="fileMenu">
						<li ng-repeat="file in fileList">
							<a href="#" >
							<img ng-src="{{file.value}}" ng-click="fileDownload(file.key)" title="Download {{file.key.split('//')[1]}}">
							</a>
						</li>
					</ul>
				</div>	
				<!-- end-- -->

			</div>
		</div>
	</div>
	<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
		var="angularmin" />
	<script src="${angularmin}" type="text/javascript"></script>

	<spring:url
		value="/webjars/angular-ui-tree/2.13.0/angular-ui-tree.min.js"
		var="angularUiTreeJs" />
	<script src="${angularUiTreeJs}"></script>

	<script src="resources/js/topojson.v1.min.js"></script>
	<jsp:include page="fragments/footer.jsp" />

	<script src="resources/js/angularController/contentManagementController.js"></script>
</body>
<spring:url
	value="/webjars/angular-ui-tree/2.13.0/angular-ui-tree.min.css"
	var="angularUiTreeCss" />
<link href="${angularUiTreeCss}" rel="stylesheet" />
</html>
