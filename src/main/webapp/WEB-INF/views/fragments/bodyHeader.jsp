<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="org.sdrc.cysdrf.util.Constants"%>
<%@page import="org.sdrc.cysdrf.model.UserRoleFeaturePermissionMappingModel"%>
<%@ page import="org.sdrc.cysdrf.model.UserDetailModel"%>
<%@ page import="org.sdrc.cysdrf.model.RoleModel"%>
<!-- Top bar -->

<header class="navbar navbar-static-top bar" id="head1">
	<div class="container-fluid">
		<div class="navbar-header">
			<spring:url value="/resources/images/logo-final-cysd2.png" var="cysdLogo" />
			<a class="navbar-brand" href="/"> <img alt="logo" style="width: 115px;"
				src="${cysdLogo}" class="img-responsive">
			</a>
		</div>
		<div class="navbar-collapse">
			<nav class="nav navbar-nav navbar-right" role="navigation">
<!-- 				<form class="navbar-form navbar-right searchform" role="search" -->
<!-- 					action="gcsearch" method="post"> -->
<!-- 					<input class="form-control" type="text" placeholder="Search" -->
<!-- 						name="search"> -->
<!-- 					<button type="submit" class="btn btn-sm mainnav-form-btn"> -->
<!-- 						<i class="fa fa-lg fa-search"></i> -->
<!-- 					</button> -->
<!-- 				</form> -->
				
				
<!-- 				==jstl code -->


<%
					UserDetailModel user = null;
					String role = "";
					String area = "";
					List<String> features = new ArrayList<String>();
					List<String> permissions = new ArrayList<String>();
				%>
				<%
					user = (UserDetailModel) request.getSession().getAttribute(
							Constants.USER_PRINCIPAL);
					role = user != null ? user.getUserRoleFeaturePermissionMappings() != null
							&& !user.getUserRoleFeaturePermissionMappings().isEmpty() ? user
							.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionSchemeModel()
							.getRole().getRoleName() : null : null;
					area = user != null ? user.getUserRoleFeaturePermissionMappings() != null
							&& !user.getUserRoleFeaturePermissionMappings().isEmpty() ? user
							.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionSchemeModel()
							.getSchemeName().split("_")[0] : null : null;

					List<UserRoleFeaturePermissionMappingModel> ursMappings = new ArrayList<UserRoleFeaturePermissionMappingModel>();
					ursMappings = user != null ? user.getUserRoleFeaturePermissionMappings()
							: null;
					if (ursMappings != null && !ursMappings.isEmpty()) {
						for (UserRoleFeaturePermissionMappingModel ursm : ursMappings) {

							role = ursm.getRoleFeaturePermissionSchemeModel().getRole()
									.getRoleName();
							features.add(ursm.getRoleFeaturePermissionSchemeModel()
									.getFeaturePermissionMapping().getFeature()
									.getFeatureName());
							permissions.add(ursm.getRoleFeaturePermissionSchemeModel()
									.getFeaturePermissionMapping().getPermission()
									.getPermissionName());
						}
					}
				%>
				
<!-- end---- -->
				<ul class="nav navbar-nav navbar-right samikshya-nav" style="margin-top: 20px;">
					<%
						if (user==null && role == null) {
					%>
					<li><a class="" href="#" data-toggle="modal"
						data-target="#loginmodal"><i class="fa fa-lg fa-user"> </i>
							Login </a></li>
					
								<%
						}else if(user!=null && role==null){
					%>
					<li><a class="" href="javascript:void(0)">Welcome,&nbsp;&nbsp;<%=user.getUserName()%>
					</a>
					<li><a id="logoutid" class=""
							href="<c:url value="/logout" />"><i
								class="fa fa-lg fa-power-off"> </i>&nbsp;&nbsp;Logout </a></li>
								<%
						}else if(role!=null && area!=null && user!=null){
					%>
					<li><a class="" href="javascript:void(0)">Welcome &nbsp;<%=role%>&nbsp;(&nbsp;<%=area%>&nbsp;),&nbsp;&nbsp;<%=user.getUserName()%>
					</a>
					<li><a id="logoutid" class=""
							href="<c:url value="/logout" />"><i
								class="fa fa-lg fa-power-off"> </i>&nbsp;&nbsp;Logout </a></li>
								<%} %>
				</ul>
			</nav>
		</div>
	</div>

</header>
<!-- END Top Bar	 -->
<div class="navbar navbar-default  bar" id="head2">
	<div class="container-fluid">
		<jsp:include page="menu.jsp" />
	</div>
</div>

<div class="login_pop">
	<div class="modal fade" id="loginmodal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title text-center" id="myModalLabel">Sign In
						to your Account</h4>
				</div>
				<div class="modal-body">
					<form action="/login" method="post" class="form-signin">
						<div class="row text-center">
							<input name="userEmail" type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" placeholder="Email Id"
								class="form-control" required="required" autofocus="autofocus">
						</div>
						<div class="row text-center">
							<input name="userPassword" type="password" placeholder="Password"
								class="form-control" required="required">
						</div>
						<div class="row text-center">
							<div class="pull-right">
								<button type="submit" class="btn btn-large btn-primary">
									Sign in&nbsp;<i class="fa fa-angle-right"></i>
								</button>
							</div>
						</div>
					</form>
						<div class="forgotPassContainer">
						<div class="row text-center forgot_password" id="forgotPass">
							<a class="btn btn-lg btn-link">Forgot Password?</a>
						</div>
						 <div class="forgotPassContent">
							<form action="forgotPass" method="post" class="form-signin">
							<div class="row text-center">
								<input name="emailId" type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" placeholder="Email Id"
								class="form-control" required="required">
								</div>
								<div class="row text-center">
								<div class="pull-right">
								<button type="submit" class="btn btn-xs btn-primary">
									Request Password&nbsp;<i class="fa fa-angle-right"></i>
								</button>
								</div>
								</div>
							</form>
    					</div>
    					</div>
					
				</div>
			</div>
		</div>
	</div>
</div>
<script>
$("#forgotPass").click(function() {

	$header = $(this);
	// getting the next element
	$content = $header.next();
	// open up the content needed - toggle the slide- if visible, slide up,
	// if not slidedown.
	$content.slideToggle(500, function() {
		// execute this after slideToggle is done
		// change text of header based on visibility of content div
		// $header.text(function () {
		// change text based on condition
		// return $content.is(":visible") ? "Forgot Password?" : "Forgot
		// Password?";
		// });
	});

});
</script>


