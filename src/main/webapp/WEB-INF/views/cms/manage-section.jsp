<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" ng-app="manageSection">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manage Reports</title>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<style type="text/css">
._md-select-menu-container{
  	z-index: 2000 !important;
  }
  
</style>

</head>

<body ng-controller="ManageSectionController" ng-cloak>
	<jsp:include page="../fragments/cms-headtag.jsp" />

	<div id="wrapper" class="container">
		<jsp:include page="../fragments/cmsHeader.jsp"></jsp:include>
		<serror:Error id="msgBoxlogin" errorList="${formError}"
			cssInfClass="${className}">
								${formError}
							</serror:Error>
		<div class="alert alert-warning text-center" id="errorValidation">
			<strong>{{errorMessage}}</strong>
		</div>
		<div class="col-sm-8 col-sm-offset-2" style="margin-top: 40px;">
			<div class="">
				<md-button type="button" ng-click="addSectionModal()"
					style="background-color: #415684; color: #FFF; margin-top: 20px;padding: 3px 25px;">
				<i class="fa fa-plus-circle" aria-hidden="true"></i> &nbsp;Add
				Section</md-button>
				<md-button type="button" ng-click="addThemeModal()"
					style="background-color: #415684; color: #FFF; margin-top: 20px;padding: 3px 25px;">
				<i class="fa fa-plus-square-o" aria-hidden="true"></i> &nbsp;Add
				Theme</md-button>
			</div>
		</div>
		<div id="reportsAccordion" class="col-sm-8 col-sm-offset-2"
			style="margin-top: 20px;">

			<div class="panel-group" id="accordion" class="col-sm-8">
				<div class="panel panel-default" ng-repeat="section in sections">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="collapse" data-parent="#accordion"
								ng-href="{{'#collapse' +$index}}">{{section.sectionName}}</a> <i
								class="fa fa-pencil-square-o pull-right"
								style="cursor: pointer;" ng-click="editSectionModal(section)"
								aria-hidden="true"></i>
						</h4>
					</div>
					<div id="{{'collapse' +$index}}" class="panel-collapse collapse">
						<div class="panel-body">
							<div class="panel-heading" ng-repeat="theme in sectionAndThemes"
								ng-if="theme.parentId == section.sectionId">
								<h4 class="panel-title">
									<a>{{theme.sectionName}}</a> <i
										class="fa fa-pencil-square-o pull-right"
										style="cursor: pointer;" ng-click="editThemeModal(theme)"
										aria-hidden="true"></i>
								</h4>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
		<div class="col-sm-12">
			<div class="col-sm-12 add-sectionModal modal" role="dialog">
				<form id="add-Section" class="row" name="documentForm"
					action="../newSection" method="post">
					<div class="col-sm-12">
						<h3>Add Section</h3>
					</div>
					<div class="col-sm-12">
						<md-input-container flex="50"> <label>Section
							Name</label> <input required md-maxlength="150" maxlength="150"
							name="sectionName" ng-model="section.title">
						
						</md-input-container>
					</div>
					<div class="col-sm-12">
						<md-input-container class="md-block"> <label
							class="md-mandatory">Section Description *</label> <input
							md-maxlength="300" maxlength="300" md-no-asterisk
							name="description" ng-model="section.description" ng-trim="true"
							required>
						
						</md-input-container>
					</div>
					<input ng-model="parentId" ng-show="false" name="parentId">
					<div class="col-sm-12">
						<md-button type="submit"
							style="background-color: #415684; color: #FFF; margin-top: 20px;">Submit</md-button>
					</div>
				</form>
			</div>
			<div class="col-sm-12 edit-sectionModal modal" role="dialog">
				<form id="edit-Section" class="row" name="documentForm"
					action="../editSection" method="post">
					<div class="col-sm-12">
						<h3>Edit Section</h3>
					</div>
					<div class="col-sm-12">
						<md-input-container flex="50"> <label>Section
							Name</label> <input required md-maxlength="150" maxlength="150"
							name="sectionName" ng-model="selectedSection.title">
						
						</md-input-container>
					</div>
					<div class="col-sm-12">
						<md-input-container class="md-block"> <label
							class="md-mandatory">Section Description *</label> <input
							md-maxlength="300" maxlength="300" md-no-asterisk
							name="description" ng-model="selectedSection.description"
							ng-trim="true" required>
						
						</md-input-container>
					</div>
					<input ng-model="parentId" ng-show="false" name="parentId">
					<input ng-model="selectedSection.sectionId" ng-show="false"
						name="sectionId">
					<div class="col-sm-12">
						<md-button type="submit"
							style="background-color: #415684; color: #FFF; margin-top: 20px;">Submit</md-button>
					</div>
				</form>
			</div>
			<div class="col-sm-12 add-themeModal modal" role="dialog">
				<form id="add-Theme" class="row" name="documentForm"
					action="../newSection" method="post">
					<div class="col-sm-12">
						<h3>Add Theme</h3>
					</div>
					<div class="col-sm-6">
						<!-- <label style="margin-bottom: 2px;
        color: #b5b3b3;     font-weight: lighter;">
							Section *</label> <br> -->
							<select name="sec" placeholder="select section *" class="defaultSelect" required ng-model="sectionTheme.sectionId">
								<option value="" selected>Select Section</option>
								<option ng-repeat="section in sections"
							ng-value="section.sectionId">{{section.sectionName}}</option>
						</select>
					</div>
					<div class="col-sm-12">
						<md-input-container flex="50"> <label>Theme
							Name</label> <input required md-maxlength="150" maxlength="150"
							name="sectionName" ng-model="sectionTheme.title">
						
						</md-input-container>
					</div>
					<div class="col-sm-12">
						<md-input-container class="md-block"> <label
							class="md-mandatory">Theme Description *</label> <input
							md-maxlength="300" maxlength="300" md-no-asterisk
							name="description" ng-model="sectionTheme.description" ng-trim="true"
							required>
						
						</md-input-container>
					</div>
					<div class="col-sm-12">
						<md-chips ng-model="section.tags" readonly="ctrl.readonly"
							md-removable="ctrl.removable" placeholder="Enter a tag"
							delete-button-label="Remove Tag"
							delete-hint="Press delete to remove tag"
							secondary-placeholder="+Tag"></md-chips>

					</div>
					<input ng-show="false" name="tags" ng-if="section.tags"
						ng-model="section.tags.toString()"> 
					<input
						ng-model="sectionTheme.sectionId" ng-show="false" name="parentId">
					<div class="col-sm-12">
						<md-button type="submit"
							style="background-color: #415684; color: #FFF; margin-top: 20px;">Submit</md-button>
					</div>
				</form>
			</div>
			<div class="col-sm-12 edit-themeModal modal" role="dialog">
				<form id="edit-Theme" class="row" name="documentForm"
					action="../editSection" method="post">
					<div class="col-sm-12">
						<h3>Edit Theme</h3>
					</div>
					<div class="col-sm-12">
						<md-input-container flex="50"> <label>Theme
							Name</label> <input required md-maxlength="150" maxlength="150"
							name="sectionName" ng-model="selectedSection.title">
						<div ng-messages="documentForm.sectionName.$error">
							<div ng-message="required">This is required.</div>
						</div>
						</md-input-container>
					</div>
					<div class="col-sm-12">
						<md-input-container class="md-block"> <label
							class="md-mandatory">Theme Description *</label> <input
							md-maxlength="300" maxlength="300" md-no-asterisk
							name="description" ng-model="selectedSection.description"
							ng-trim="true" required>
						<div ng-messages="documentForm.description.$error">
							<div ng-message="required">This is required.</div>
							<div ng-message="md-maxlength">The description must be less
								than 300 characters long.</div>
						</div>
						</md-input-container>
					</div>
					<div class="col-sm-12">
						<md-input-container flex="50"> <label>Add a Tag</label> 
						<input
						name="tags">
				<div ng-messages="documentForm.tags.$error">
							<div ng-message="required">This is required.</div>
						</div>
				</md-input-container>
						
					</div>
					<input
						ng-model="selectedSection.parentId" ng-show="false"
						name="parentId"> 
						<input
						ng-model="selectedSection.sectionId" ng-show="false"
						name="sectionId">
					<div class="col-sm-12">
						<md-button type="submit"
							style="background-color: #415684; color: #FFF; margin-top: 20px;">Submit</md-button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			$("#msgBox").delay(2000).fadeOut(2000);
			$("#msgBoxlogin").delay(2000).fadeOut(2000);
		});
		
		(function($) {
		    $(document).ready(function() {
		        $('input').blur(function() {
		            var value = $.trim( $(this).val() );
		            $(this).val( value );
		        });
		       
		    });
		    
		})(jQuery);
		
	
	</script>
	<jsp:include page="../fragments/footer.jsp" />
	<!-- Angular Material Library -->

	<script src="../resources/js/angular.min.js"></script>
	<script src="../resources/js/ui-bootstrap-tpls-1.3.2.min.js"></script>
	<script src="../resources/js/angular-animate.min.js"></script>
	<script src="../resources/js/angular-aria.min.js"></script>
	<script src="../resources/js/angular-messages.min.js"></script>
	<script
		src="http://ajax.googleapis.com/ajax/libs/angular_material/1.1.0-rc2/angular-material.min.js"></script>
	<script
		src="../resources/js/angularController/cmsManageSectionController.js"></script>
	<script src="../resources/js/angularService/cmsService.js"></script>
</body>
</html>