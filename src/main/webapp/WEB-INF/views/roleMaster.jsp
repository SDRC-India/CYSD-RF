<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="datatables"
	uri="http://github.com/dandelion/datatables"%>

<html lang="en">

<jsp:include page="fragments/headtag.jsp" />
<script src="resources/js/roleMaster.js"></script>
<body>
	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div>
			<div id="alert_placeholder"></div>
			<div class="container-fluid">
				<h2 class="page-header cysdColor" data-html2canvas-ignore="true">Manager User Role</h2>
				<div class="col-md-12 margin-top-20">
					<datatables:table id="userRoleTable" url="getUsersOfNoRole"
						cdn="true" theme="bootstrap2" cssClass="table table-striped"
						paginate="true">

						<datatables:column title="User Name" property="userName" sortable="true" />
<%-- 						<datatables:column title="Role" property="roleName" sortable="true" /> --%>
						<datatables:column title="Email" property="email" sortable="true" />
<%-- 						<datatables:column title="Contact Number" property="contactNum" sortable="true" /> --%>
						<datatables:column title="Contact Number" property="contactNum" sortable="true" />
						<datatables:column title="Assign Role" renderFunction="assignRoleBtn"/>
<%-- 						<datatables:column title="Area Name" property="areaName" /> --%>

					</datatables:table>
				</div>
			</div>
		</div>
		
	</div>
	<jsp:include page="fragments/footer.jsp" />

</body>

</html>
