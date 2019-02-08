<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>

<html lang="en" ng-app="superAdminApp">

<jsp:include page="fragments/headtag.jsp" />
<body>
	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div ng-controller='SuperAdminController'>
			<div class="container-fluid">
			<div id="alert_placeholder"></div>
				<h2 class="page-header cysdColor" data-html2canvas-ignore="true">Admin</h2>
				<div class="col-md-3"></div>
				<div class="col-md-6 menuBackground margin-top-10">
					<div class="col-md-12">
						<label class="col-md-4 margin-top-8">Project Name:</label>
						<div class="form-group col-md-8">
							<input class="form-control" ng-model="projectName">
						</div>
					</div>
					<div class="col-md-12">
						<div class="col-md-3">
							<label><input type="radio" value="state"
							ng-model='view' >&nbsp;&nbsp;State</label>
						</div>
						<div class="col-md-3">
							<label><input type="radio" value="district"
							ng-model='view' >&nbsp;&nbsp;District</label>
						</div>
					</div>
					<div class="col-md-12 text-right padding-right-30">
							<button class="btn btn-primary" ng-click = "saveProject()">
								<i class="fa fa-floppy-o"></i>&nbsp;&nbsp;Save
							</button>
					</div>
				</div>
				<div class="col-md-3"></div>
			</div>
			<div Style="height: 10px;"></div>
		</div>
	</div>

	<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
		var="angularmin" />
	<spring:url value="/resources/js/ui-bootstrap-tpls-1.3.2.min.js" var="BootstrapTPLS"/>
	<spring:url value="/resources/js/angularController/superAdminController.js" var="SuperAdminControllerJS"/>
	<spring:url value="/resources/js/superAdmin.js" var="SuperAdminJS"/>
	
	<script src="${angularmin}" type="text/javascript"></script>

	<jsp:include page="fragments/footer.jsp" />

	<script src="${BootstrapTPLS}"></script>
	<script src="${SuperAdminJS}"></script>
	<script src="${SuperAdminControllerJS}"></script>
</body>

</html>
