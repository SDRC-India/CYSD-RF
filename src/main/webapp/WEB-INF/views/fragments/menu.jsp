<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.sdrc.cysdrf.model.UserRoleFeaturePermissionMappingModel"%>
<%@ page import="org.sdrc.cysdrf.model.UserDetailModel"%>
<%@ page import="org.sdrc.cysdrf.model.RoleModel"%>
<%@ page import="org.sdrc.cysdrf.util.Constants"%>

<%
					UserDetailModel user = null;
					Integer role = 0;
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
							.getRole().getRoleId() : null : null;
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
									.getRoleId();
							features.add(ursm.getRoleFeaturePermissionSchemeModel()
									.getFeaturePermissionMapping().getFeature()
									.getFeatureName());
							permissions.add(ursm.getRoleFeaturePermissionSchemeModel()
									.getFeaturePermissionMapping().getPermission()
									.getPermissionName());
						}
					}
					
				%>
<button class="navbar-toggle" data-toggle="collapse"
	data-target=".navHeaderCollapse2">
	<span class="icon-bar"></span> <span class="icon-bar"></span> <span
		class="icon-bar"></span>
</button>
<div
	class="collapse navbar-collapse navHeaderCollapse2 pull-left navbar-width">
	<ul class="nav navbar-nav navbar-left">
		<li><a class="<%= request.getRequestURI().contains("welcome")?"menu-active":"" %>"
			href="<spring:url value="/" htmlEscape="true" />"> <i
				class="fa fa-lg fa-home"></i>&nbsp;Home
		</a></li>
		<li><a class="<%= request.getRequestURI().contains("about")?"menu-active":"" %>"
			href="<spring:url value="/about" htmlEscape="true" />"> <i
				class="fa fa-lg fa-info-circle"></i>&nbsp;About
		</a></li>
		<li><a class="<%= request.getRequestURI().contains("dashboard")?"menu-active":"" %>"
			href="<spring:url value="/dashboard" htmlEscape="true" />"> <i
				class="fa fa-lg fa-bar-chart-o"></i>&nbsp;Dashboard
		</a></li>
<!-- 		 <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Open Small Modal</button> -->
		<li><a data-toggle="modal" href="#myModal" onclick="getPeriodicity();">
		<i class="fa fa-lg fa-stack-exchange" aria-hidden="true"></i>&nbsp;Factsheet</a></li>
		<li><a href="http://uat.sdrc.co.in/cysdcbo" target="_blank">
		<i class="fa fa-lg fa-tachometer"></i>&nbsp;CYSD-CBO
		</a></li>
		<%
			if(role!=null && (role == 1 || role ==2 || role==4)){
		%>
		<li><a class="<%= request.getRequestURI().contains("workspace")?"menu-active":"" %>"
			href="<spring:url value="/cysd/workspace" htmlEscape="true" />"> <i
				class="fa fa-lg fa-retweet"></i>&nbsp;Workspace
		</a></li>
		<li><a class="<%= request.getRequestURI().contains("transaction")?"menu-active":"" %>"
			href="<spring:url value="/cysd/transaction" htmlEscape="true" />"> <i
				class="fa fa-lg fa-exchange"></i>&nbsp;Transaction Management
		</a></li>
		<%} else if(role!=null && role==7){%>
		<li><a class="<%= request.getRequestURI().contains("roleManagement")?"menu-active":"" %>"
			href="<spring:url value="/roleManagement" htmlEscape="true" />"> <i
				class="fa fa-lg fa-cogs"></i>&nbsp;Role Management
		</a></li>
		<li><a class="<%= request.getRequestURI().contains("userManagement")?"menu-active":"" %>"
			href="<spring:url value="/userManagement" htmlEscape="true" />"> <i
				class="fa fa-lg fa-users"></i>&nbsp;User Management
		</a></li>
		<li><a class="<%= request.getRequestURI().contains("stateAdminTXN")?"menu-active":"" %>"
			href="<spring:url value="/cysd/stateAdminTXN" htmlEscape="true" />"> <i
				class="fa fa-lg fa-exchange"></i>&nbsp;State Admin Transaction
		</a></li>
		<li><a class="<%= request.getRequestURI().contains("superAdmin")?"menu-active":"" %>"
			href="<spring:url value="/cysd/superAdmin" htmlEscape="true" />"> <i
				class="fa fa-lg fa-user"></i>&nbsp;Project Management
		</a></li>
		<%} %>
	</ul>
	<%
			if(features.contains("view-reports")){
		%>
	<ul class="nav navbar-nav navbar-right">
		<li><a class="<%= request.getRequestURI().contains("cms")?"menu-active":"" %>"
			href="<spring:url value="/cms/view-reports" htmlEscape="true" />"> <i
				class="fa fa-lg fa-edit"></i>&nbsp;CMS
		</a></li>
	</ul>
	<%} %>
	<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm">
      <div class="modal-content" style="width: 123%;height:240px;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Factsheet</h4>
        </div>
				<div class="modal-body">
					<form>
						<div>
						<div class="btn-toolbar" role="toolbar" >
							<div class="input-group">
											<input type="text" placeholder="Select Framework Type"
												class="form-control" readonly id="frameworkInput"></input>
											<div class="input-group-btn" style="position: relative;">
												<button data-toggle="dropdown" id="ind-list"
													class="btn btn-primary dropdown-toggle" type="button" >
													<i class="fa fa-list"></i>
												</button>
												<ul class="dropdown-menu" role="menu" id="frameworkId">
<!-- 													<li ng-repeat='framework in typeOfFrameworks' -->
<!-- 														ng-click='selectFramework(framework)'> -->
<!-- 														<a href="">{{framework.value}}</a> -->
<!-- 													</li> -->
												</ul>
											</div>
										</div>
						</div>
						<div class="btn-toolbar" role="toolbar" style="margin-top:5px;">
							<div class="input-group">
								<input type="text" placeholder="Select Periodicity" id="periodicityId" name="periodicity"
									 class="form-control"
									readonly></input>
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown" style="height: 34px;" 
										class="btn btn-primary dropdown-toggle mapInput" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu" role="menu" id="timeperiodID">
<!-- 									<li name="timeList"></li> -->
									</ul>
								</div>
							</div>
						</div>
						
						<div class="btn-toolbar" role="nextToolbar">
							<div class="input-group" style="margin-top: 5px;">
								<input type="text" placeholder="Select Quarter" id="quarterId" name="quarter"
									 class="form-control"
									readonly></input>
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown" style="height: 34px;" 
										class="btn btn-primary dropdown-toggle mapInput" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu" role="menu" id="quarterID">
										<li ><a href=""></a>
										</li>
									</ul>
								</div>
								<input type="hidden" id = "selectedQuarter">
							</div>
						</div>
							<button type="button" class="btn btn-primary text-right-btn" onclick="downloadExcel()">
								<i class="fa fa-download"></i>&nbsp;&nbsp;Download
							</button>
						</div>
					</form>
				</div>

			</div>
    </div>
  </div>
</div>
<div class="modal fade" id="loaderModal" tabindex="-1"
role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
<div id="loaderDiv">
<div id="circularG" style="position: absolute;top: 45%;left: 44%;">
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
</div>

