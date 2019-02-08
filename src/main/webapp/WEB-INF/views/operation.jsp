<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>

<html lang="en" ng-app="operationApp">

<jsp:include page="fragments/headtag.jsp" />
<spring:url value="/resources/css/bootstrap-multiselect.css" var="style" />
<spring:url value="/resources/js/bootstrap-multiselect.js"
	var="multiSelectJS" />
<link rel="stylesheet" href="${style}" type="text/css" />
<script type="text/javascript" src="${multiSelectJS}"></script>
<body>
	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div ng-controller='OperationController'>
			<div class="container-fluid">
				<div class="col-md-12 margin-top-20">
					<div class="col-md-1"></div>
					<div class="col-md-10 menuBackground">
						<div class="col-md-12">
							<div class="col-md-6 margin-top-10 padding-right-10">
								<label class="col-md-4 margin-top-8">Time Period:</label>
								<div class="col-md-8  padding-right-0">
									<div class="btn-toolbar" role="toolbar">
										<div class="input-group">
											<input type="text" placeholder="Select Time Period"
												ng-model='selectedTimePeriod.value'
												class="form-control" readonly></input>
											<div class="input-group-btn" style="position: relative;">
												<button data-toggle="dropdown" id="ind-list"
													class="btn btn-primary dropdown-toggle" type="button" >
													<i class="fa fa-list"></i>
												</button>
												<ul class="dropdown-menu" role="menu">
													<li ng-repeat='tp in currentTimeperiod'
													ng-click='selectTimePeriod(tp)'>
														<a href="">{{tp.value}}</a>
													</li>
												</ul>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6 margin-top-10 padding-right-10">
								<label class="col-md-4 margin-top-8">Projects:</label>
								<div class="col-md-8  padding-right-0">
									<div class="btn-toolbar" role="toolbar">
										<div class="input-group">
											<input type="text" placeholder="Select Project"
												ng-model='selectedProject.value'
												class="form-control" readonly></input>
											<div class="input-group-btn" style="position: relative;">
												<button data-toggle="dropdown" id="ind-list"
													class="btn btn-primary dropdown-toggle" type="button" >
													<i class="fa fa-list"></i>
												</button>
												<ul class="dropdown-menu" role="menu">
													<li ng-repeat='project in allProjects'
													ng-click='selectProject(project)'>
														<a href="">{{project.value}}</a>
													</li>
												</ul>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-12 text-right padding-right-25" ng-hide="isValidUpload">
							<label class="uploadWarningLabel">Please upload a valid .xls file</label>
						</div>
						<div class="col-md-12 margin-left-30 margin-top-20 text-center">
							<div class="fileUpload btn btn-primary"
								title="Upload achieved file">
								    <span><i class="fa fa-upload"></i>&nbsp;&nbsp;Upload</span>
								    <input type="file" class="upload"  file-model = "myFile" />
								</div>
								<button class="btn btn-primary margin-left-30">
									<i class="fa fa-download"></i>&nbsp;&nbsp;Download
								</button>
						</div>
					</div>
				</div>
			</div>
			<div Style="height: 10px;"></div>
			<div id="loaderModal" class="modal" modal="showLoaderModal"
				ng-show="showLoaderModal">
				<div id="circularG"
					style="position: fixed; margin-top: 250px; margin-left: 650px;">
					<div id="circularG_1" class="circularG"></div>
					<div id="circularG_2" class="circularG"></div>
					<div id="circularG_3" class="circularG"></div>
					<div id="circularG_4" class="circularG"></div>
					<div id="circularG_5" class="circularG"></div>
					<div id="circularG_6" class="circularG"></div>
					<div id="circularG_7" class="circularG"></div>
					<div id="circularG_8" class="circularG"></div>
				</div>
			</div>
			<div id="validationModal" class="modal" modal="showConfirmationModal"
				ng-show="showConfirmationModal">
				<div class="modal-dialog parentOfVerticallyCenteredElement">
					<div class="modal-content verticallyCenteredElement">
						<div class="modal-body">
							<div class="container-fluid">
								<div class="col-md-12 text-center">
									<label>Are you sure you want to upload the file
										{{myFile.name}} ?</label>
								</div>
								<div class="col-md-12 text-center margin-top-10 ">
									<button class="btn btn-primary" ng-click="uploadFile()">Yes</button>
									<button class="btn btn-danger margin-left-10"
										ng-click="showConfirmationModal = false">Cancel</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
		var="angularmin" />
	<script src="${angularmin}" type="text/javascript"></script>

	<jsp:include page="fragments/footer.jsp" />


	<spring:url value="/resources/js/angular_bootstrap_ui.js"
		var="bootstrapUI" />
	<spring:url value="/resources/js/ui-bootstrap-tpls-1.3.2.min.js"
		var="bootstrapTpl" />
	<spring:url value="/resources/js/operation.js" var="operationJS" />
	<spring:url value="/resources/js/angularService/projectService.js"
		var="projectService" />
	<spring:url
		value="/resources/js/angularController/operationController.js"
		var="operationCtrlJS" />
	<script src="${bootstrapUI}"></script>
	<script src="${bootstrapTpl}"></script>
	<script src="${operationJS}"></script>
	<script src="${projectService}"></script>
	<script src="${operationCtrlJS}"></script>
</body>

</html>
