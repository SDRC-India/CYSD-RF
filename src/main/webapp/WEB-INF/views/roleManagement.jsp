<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="datatables"
	uri="http://github.com/dandelion/datatables"%>

<html lang="en">

<jsp:include page="fragments/headtag.jsp" />
<link rel="stylesheet" href="resources/css/bootstrap-multiselect.css"
	type="text/css" />
<script src="resources/js/roleManagement.js"></script>
<body>
	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div>
			<div class="container-fluid">
				<div id="alert_placeholder"></div>
				<h2 class="page-header cysdColor" data-html2canvas-ignore="true">Role Management</h2>
				<div class="col-md-12 margin-top-20">
					<datatables:table id="roleManagementTran" url="getAllUserRoleSchemes"
						cdn="true" theme="bootstrap2" cssClass="table table-striped"
						paginate="true">

						<datatables:column title="User Name" property="userName" sortable="true" cssCellClass="middleAlign" />
						<datatables:column title="Role" property="roleName" sortable="true" cssCellClass="middleAlign" />
						<datatables:column title="Email" property="emailId" sortable="true" cssCellClass="middleAlign" />
						<datatables:column title="Contact Number" property="contactNum" sortable="true" cssCellClass="middleAlign" />
						<datatables:column title="Attach/Detach" renderFunction="attachBtn" cssCellClass="middleAlign" />
						<datatables:column title="Area Name" property="areaName" cssCellClass="middleAlign" />

					</datatables:table>
				</div>
			</div>
		</div>
		
	</div>
	<jsp:include page="fragments/footer.jsp" />

</body>

</html>
