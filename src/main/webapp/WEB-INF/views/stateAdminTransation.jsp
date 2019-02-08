<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="datatables"
	uri="http://github.com/dandelion/datatables"%>

<html lang="en" ng-app="txnApp">

<jsp:include page="fragments/headtag.jsp" />
<spring:url value="/resources/css/bootstrap-multiselect.css" var="style" />
<spring:url value="/resources/js/bootstrap-multiselect.js" var="multiSelectJS" />
<spring:url value="/webjars/angularjs/1.5.5/angular.min.js" var="angularmin"/>
<spring:url value="/resources/js/stateAdminTXN.js" var="StateAdminTXNJS"/>

<link rel="stylesheet" href="${style}" type="text/css" />
<script type="text/javascript" src="${multiSelectJS}"></script>
<script src="${angularmin}" type="text/javascript"></script>
<script src="${StateAdminTXNJS}"></script>

<body>
	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div ng-controller='TransactionController'>
			<div class="container-fluid">
			<div id="alert_placeholder"></div>
				<h2 class="page-header cysdColor" data-html2canvas-ignore="true">All Transactions</h2>
				<div class="col-md-12">
					<div class="col-md-6 margin-top-10 padding-right-10 padding-left-0">
						<label class="col-md-2 margin-top-8 padding-left-0">Periodicity:</label>
						<div class="col-md-6  padding-right-0">
							<div class="btn-toolbar" role="toolbar">
								<div class="input-group">
									<input type="text" placeholder="Select Year"
										ng-model='selectedYear.value' class="form-control"
										readonly></input>
									<div class="input-group-btn" style="position: relative;">
										<button data-toggle="dropdown" id="ind-list"
											class="btn btn-primary dropdown-toggle" type="button">
											<i class="fa fa-list"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li ng-repeat='tp in timePeriodList'
												ng-click='selectYear(tp)'><a href="">{{tp.value}}</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-6 margin-top-10 text-right padding-right-0" >
						<div class="col-md-3"></div>
						<label class="col-md-3 margin-top-8">Time Period:</label>
						<div class="col-md-6  padding-right-0">
							<div class="btn-toolbar" role="toolbar">
								<div class="input-group">
									<input type="text" placeholder="Select Time Period"
										ng-model='selectedTimePeriod.value' class="form-control"
										readonly></input>
									<div class="input-group-btn" style="position: relative;">
										<button data-toggle="dropdown" id="ind-list"
											class="btn btn-primary dropdown-toggle" type="button">
											<i class="fa fa-list"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li ng-repeat='tp in quarterList'
												ng-click='selectTimePeriod(tp)'><a href="">{{tp.value}}</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
					
				</div>
				<div class="col-md-12 margin-top-20">
					<datatables:table id="adminTemplateTran" url="getAllUserTxnForms?timePeriodId=2"
						cdn="true" theme="bootstrap2" cssClass="table table-striped"
						paginate="true">

						<datatables:column visible="false" title="User Id" property="userId" />
						<datatables:column visible="false" title="TXN Id" property="txnTemplateId" />
						<datatables:column title="User Name" property="userName" sortable="true" cssCellClass="middleAlign" />
						<datatables:column title="Area Name" property="areaName" cssCellClass="middleAlign" />
						<datatables:column title="Framework Type" property="frameworkType" cssCellClass="middleAlign" />
						<datatables:column title="Time Period" property="timePeriod" cssCellClass="middleAlign" />
<%-- 						<datatables:column title="Status" property="status" /> --%>
						<datatables:column title="Planned" cssCellClass="bottomAlign" 
							renderFunction="downloadTargetFile" />
						<datatables:column title="Achieved" cssCellClass="bottomAlign" 
							renderFunction="downloadAchiveFile" />
						<datatables:column title="Upload Achieved File" cssCellClass="bottomAlign" 
							renderFunction="uploadAchiveFile" />
						<datatables:column title="Approve Achieved File" cssCellClass="bottomAlign" 
							renderFunction="approveFile" />
					</datatables:table>
				</div>
			</div>
			<div Style="height: 10px;"></div>
			<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog parentOfVerticallyCenteredElement">
					<div class="modal-content verticallyCenteredElement">
						<div class="modal-body">
					  	  	<div class="container-fluid">
								<div class="col-md-12 text-center">
									<label id="modalLabel">Are you sure you want to upload the file?</label>
								</div>
								<div class="col-md-12 text-center margin-top-10 ">
									<button class="btn btn-primary" onclick="onYesClick()">Yes</button>
									<button class="btn btn-danger margin-left-10" onclick="onNoClick()">Cancel</button>
								</div>
							</div>
					      </div>
						</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="fragments/footer.jsp" />
	<spring:url value="/resources/js/ui-bootstrap-tpls-1.3.2.min.js" var="BootstrapTPLS"/>
	<spring:url value="/resources/js/angularController/transactionController.js" var="TransactionControllerJS"/>

	<script type="text/javascript" src="${BootstrapTPLS}"></script>
	<script src="${TransactionControllerJS}"></script>
</body>

</html>
