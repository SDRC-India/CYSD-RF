<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="datatables"
	uri="http://github.com/dandelion/datatables"%>

<html lang="en">
<jsp:include page="fragments/headtag.jsp" />
<spring:url value="resources/js/userManagement.js" var="UserManagementJS" />
<script src="${UserManagementJS}"></script>
<body>
	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div>
			<div id="alert_placeholder"></div>
			<div class="container-fluid">
				<h2 class="page-header cysdColor" data-html2canvas-ignore="true">User Management</h2>
				<div class="col-md-12 margin-top-20">
						<button class="btn btn-primary" data-toggle="collapse" data-target="#newUserDiv"
							aria-expanded="false" aria-controls="newUserDiv">
							<i class="fa fa-plus newUser"></i>&nbsp;&nbsp;Add New User
						</button>
				</div>
				<div class="col-md-12 margin-top-20 collapse" id="newUserDiv">
					<div class="col-md-1"></div>
					<div class="col-md-10">
						<form method="post" action="createNewUser">
							<div class="col-md-12">
								<div class="col-md-12">
									<p>
										<span>Please provide your details below:</span>
									</p>
									<div class="well well-sm"><b>*</b> represents required Field</div>
									</div>
							</div>
							<div class="col-md-12">
								<div class="col-md-6">
									<div class="form-group">
										<label for="InputName">Name*</label>
										<div >
											<input type="text" required="required" placeholder="Enter Name"
												id="InputName" name="userName" class="form-control cysdInput">
										</div>
									</div>
									<div class="form-group">
										<label for="InputName">Contact Number*</label>
										<div >
											<input type="text" required="required"
												placeholder="Enter Contact Number" id="InputContactNumber"
												name="contactNum" class="form-control cysdInput">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label for="InputEmail">Email Id*</label>
										<div >
											<input type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" required="required"
												placeholder="Enter Email" name="emailId" id="InputEmail"
												class="form-control cysdInput">
										</div>
									</div>
									<div class="form-group">
										<label for="InputName">Password*</label>
										<div>
											<input type="password" required="required"
												placeholder="Enter Password" id="InputPassword"
												name="password" class="form-control cysdInput">
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-md-12 text-right">
									<input type="submit" class="btn btn-primary" value="Create"
											id="submit" name="submit">
								</div>
							</div>
						</form>
					</div>
					<div class="col-md-1"></div>
				</div>
				<div class="col-md-12 margin-top-20">
					<datatables:table id="userManagementTran" url="getAllUser"
						cdn="true" theme="bootstrap2" cssClass="table table-striped"
						paginate="true">

						<datatables:column title="User Name" property="userName" sortable="true" cssCellClass="middleAlign"/>
						<datatables:column title="Email" property="email" sortable="true" cssCellClass="middleAlign"/>
						<datatables:column title="Contact Number" property="contactNum" sortable="true" cssCellClass="middleAlign"/>
						<datatables:column title="Active/Deactive" renderFunction="activeBtn" cssCellClass="middleAlign"/>

					</datatables:table>
				</div>
			</div>
		</div>
		
	</div>
	<jsp:include page="fragments/footer.jsp" />

</body>

</html>
