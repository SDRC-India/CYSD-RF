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
				int userId=user.getUserId();
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
				System.out.println(userId);	
				%>
				<script>
				var userId = <%=userId%>
				var role = <%=role%>
				console.log(role)
				</script>
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
		
		<%
			if(features.contains("upload-report")){
		%>
		<li><a class="<%= request.getRequestURI().contains("upload")?"menu-active":"" %>"
			href="<spring:url value="/cms/upload-report" htmlEscape="true" />"> 
			<i class="fa fa-lg fa-cloud-upload" aria-hidden="true"></i>&nbsp; Upload
		</a></li>
		<%} if(features.contains("view-reports"))
			{%>
		<li><a class="<%= request.getRequestURI().contains("view-reports")?"menu-active":"" %>"
			href="<spring:url value="/cms/view-reports" htmlEscape="true" />"> <i
				class="fa fa-lg fa-book"></i>&nbsp; View
		</a></li>
		<%} if(features.contains("manage-section")){%>
		<li><a class="<%= request.getRequestURI().contains("manage")?"menu-active":"" %>"
			href="<spring:url value="/cms/manage-section" htmlEscape="true" />"> <i
				class="fa fa-lg fa-cogs"></i>&nbsp; Manage Theme
		</a></li>
		
		<%} %>
	</ul>
	
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
