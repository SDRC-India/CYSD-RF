<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="datatables"
	uri="http://github.com/dandelion/datatables"%>

<html lang="en">

<jsp:include page="fragments/headtag.jsp" />
<spring:url value="/resources/css/bootstrap-multiselect.css" var="style" />
<spring:url value="/resources/js/bootstrap-multiselect.js" var="multiSelectJS" />
<spring:url value="/resources/js/transaction.js" var="transactionJS" />

<link rel="stylesheet" href="${style}" type="text/css" />
<script type="text/javascript" src="${multiSelectJS}"></script>
<script src="${transactionJS}"></script>


<body>
	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div>
			<div class="container-fluid">
				<h2 class="page-header cysdColor" data-html2canvas-ignore="true">Transaction</h2>
				<div class="col-md-12">
					<div class="row" id="txnGridDiv">
						<div class="col-md-12 mar-top-15">

							<datatables:table id="templateTran" url="getUserTXNFiles"
								cdn="true" theme="bootstrap2" cssClass="table table-striped"
								paginate="true">
								<datatables:column title="Area Name" property="areaName"
									sortable="true" />
								<datatables:column title="Framework Type" property="frameworkType" />
								<datatables:column title="Time Period" property="timePeriod" />
								<datatables:column title="Planned" renderFunction="downloadTargetFile" />
								<datatables:column title="Achieved" renderFunction="downloadAchiveFile"/>
							</datatables:table>
						</div>
					</div>
				</div>
			</div>
			<div Style="height: 10px;"></div>
		</div>
	</div>

	<jsp:include page="fragments/footer.jsp" />

</body>

</html>
