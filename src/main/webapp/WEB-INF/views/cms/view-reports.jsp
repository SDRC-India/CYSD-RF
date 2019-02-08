<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>

<html lang="en">

<jsp:include page="../fragments/cms-headtag.jsp" />

<body ng-app="viewApp" ng-controller="ViewController" ng-cloak>

	<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
	<script src="${jQuery}"></script>

	<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
		var="bootstrapjs" />

	<script src="${bootstrapjs}"></script>

	<div id="spinner" class="loader" style="display: none;"></div>
	<div id="loader-mask" class="loader" style="display: none;"></div>
	<div id="wrapper" class="container">
		<jsp:include page="../fragments/cmsHeader.jsp"></jsp:include>
		<serror:Error id="msgBoxlogin" errorList="${formError}"
			cssInfClass="${className}">
								${formError}
							</serror:Error>
		<div class="alert alert-warning text-center" id="errorValidation">
			<strong>{{errorMessage}}</strong>
		</div>
		<div id="main-row" class="col-sm-12">
			<div class="col-sm-12 filter-options">
				<div class="col-sm-12">
					<h3>Uploaded Documents</h3>
				</div>
			</div>
			<div class="col-sm-12 filter-options" style="margin-top: 30px;">
				<div class="col-sm-3">
					<md-input-container flex="100"> <label>Filter
						Section</label> <md-select name="type" ng-model="search.sectionId">
					<md-option ng-repeat="section in sections"
						ng-value="section.sectionId"
						ng-click="setThemes(); showLoader(); resetOther(1)">{{section.sectionName}}</md-option>
					</md-select> </md-input-container>
				</div>

				<div class="col-sm-3">

					<md-input-container flex="100"> <label>Filter
						Theme</label> <md-select name="type" ng-model="search.themeId">
					<md-option ng-repeat="theme in sectionAndThemes"
						ng-click="showLoader(); resetOther(2)"
						ng-disabled="!search.sectionId"
						ng-if="theme.parentId == search.sectionId"
						ng-value="theme.sectionId">{{theme.sectionName}}</md-option> </md-select> </md-input-container>
				</div>

				<div class="col-sm-3">
					<md-input-container flex="100"> <label>Filter
						Type</label> <md-select name="reportType" ng-model="search.type">
					<md-option ng-repeat="type in allReportTypes"
						ng-if="search.sectionId && search.themeId"
						ng-click="showLoader(); resetOther(3)"
						ng-value="type.reportTypeName">{{type.reportTypeName}}</md-option>
					</md-select> </md-input-container>
				</div>
				<div class="col-sm-3">
					<form>
						<input type="text" ng-model="query" ng-change="selected = false"
							class="query-search" placeholder="Search Keywords"><i
							class="fa query-submit fa-search" aria-hidden="true"
							style="cursor: pointer" ng-click="searchKeyword()"></i>
						<div class="query-suggestion"
							ng-show="query.length > 1 && !selected">
							<div ng-repeat="tag in allTags | filter:query">
								<a style="cursor: pointer; display: block; padding: 5px;"
									ng-click="setQuery(tag)">{{tag.tagName}}</a>
							</div>
							<div ng-show="!(allTags | filter:query).length">No
								Suggestion Available</div>
						</div>
					</form>

				</div>
			</div>
			<div class="col-sm-12 report-list">
				<div class="report col-sm-12"
					ng-repeat="report in filtered = (allReports | filter:search | filter:querySearch) | startFrom:(currentPage-1)*entryLimit | limitTo:entryLimit">
					<div class="createdInfo col-sm-12">
						<div class="col-sm-6">Uploaded By : {{report.uploadedBy}}</div>
						<div class="col-sm-6">Uploaded Date :
							{{report.uploadedDate}}</div>
					</div>
					<div class="reportInfo col-sm-12 text-center">
						<div class="col-sm-3 title-report">
							<b>Title</b>
							<p class="text-left">{{report.reportName}}</p>
						</div>
						<div class="col-sm-3 desc-report">
							<b>Description</b>
							<p class="text-left">{{report.description}}</p>
						</div>
						<div class="col-sm-3 type-report">
							<b>Type</b>
							<p class="text-left">{{report.type}}</p>
						</div>
						<div class="col-sm-3 tags-report">
							<b>Tags</b> <span class="text-left">{{report.tags}}</span>
						</div>
					</div>
					<div class="manageInfo col-sm-12">
						<button
							class="md-icon-button right-corner md-button md-ink-ripple"
							type="button" ng-click="viewReportModal(report)"
							aria-label="review document">

							<i class="fa fa-eye ng-scope" aria-hidden="true"
								title="view report"></i>
							<div class="md-ripple-container"></div>
						</button>

						<button ng-if="userId == report.userId || role == 7 || report.confidential==true"
							class="md-icon-button right-corner md-button md-ink-ripple"
							type="button" ng-click="openEditModal(report)" aria-label="edit">

							<i class="fa fa-pencil-square-o ng-scope" aria-hidden="true"
								title="edit report"></i>
						</button>
						<button ng-if="userId == report.userId || role == 7 || report.confidential==true"
							class="md-icon-button right-corner md-button md-ink-ripple"
							type="button" ng-click="deleteReport(report)" aria-label="delete">

							<i class="fa fa-trash-o" aria-hidden="true" title="delete report"></i>
						</button>
					</div>
				</div>
			</div>
			<div class="col-sm-12">
				<div class="col-sm-12">
					<pagination page="currentPage" max-size="noOfPages"
						total-items="totalItems" items-per-page="entryLimit"></pagination>
				</div>

			</div>

		</div>
		<div class="col-sm-12">
			<div class="col-sm-12 edit-modal modal" role="dialog">
				<form id="edit-report" class="row" name="documentForm"
					action="../editReport" method="post">
					<div class="col-sm-12">
						<h3>Edit Report</h3>
					</div>
					<div class="col-sm-12">
						<md-input-container flex="50"> <label>Report
							Title</label> <input required md-maxlength="150" maxlength="150"
							name="reportName" ng-model="selectedReport.title">
						<div ng-messages="documentForm.reportName.$error">
							<div ng-message="required">This is required.</div>
						</div>
						</md-input-container>
					</div>
					<div class="col-sm-12">
						<md-input-container class="md-block"> <label
							class="md-mandatory">Document Description</label> <input
							md-maxlength="300" maxlength="300" md-no-asterisk
							name="description" ng-model="selectedReport.description"
							ng-trim="true" required>
						<div ng-messages="documentForm.description.$error">
							<div ng-message="required">This is required.</div>
							<div ng-message="md-maxlength">The description must be less
								than 300 characters long.</div>
						</div>
						</md-input-container>
					</div>
					<div class="col-sm-12">
						<md-chips ng-model="selectedReport.tags" readonly="ctrl.readonly"
							md-removable="ctrl.removable" placeholder="Enter a tag"
							delete-button-label="Remove Tag"
							delete-hint="Press delete to remove tag"
							secondary-placeholder="+Tag"></md-chips>

					</div>
					<input ng-show="false" name="tags"
						ng-if="selectedReport.tags.length"
						ng-model="selectedReport.tags.toString()"> <input
						ng-show="false" name="reportId" ng-model="selectedReport.reportId">

					<div class="col-sm-12">
						<md-button type="submit"
							style="background-color: #415684; color: #FFF; margin-top: 20px;">Submit</md-button>
					</div>
				</form>
			</div>
			<div class="col-sm-12 view-image-modal modal" role="dialog">
				<div id="myCarousel1" class="carousel slide" data-ride="carousel">


					<!-- Wrapper for slides -->
					<!-- <div class="carousel-inner1 carousel-inner" role="listbox">
						<div class="item" ng-repeat="photo in photos">
							<img src="{{photo}}" alt="gallery" width="460" height="345">
						</div>


					</div> -->
					<div class="flexslider">
						<ul class="slides">
							<li ng-repeat="photo in photos track by $index"><img src="{{photo}}" alt="gallery" /></li>
						</ul>
					</div>

					<!-- Left and right controls -->
				</div>
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
					var value = $.trim($(this).val());
					$(this).val(value);
				});
			});
		})(jQuery);
	</script>
	<jsp:include page="../fragments/footer.jsp" />
	<script src="../resources/js/jquery.flexslider.js"></script>
	<!-- Angular Material Library -->
	<script src="../resources/js/angular.min.js"></script>
	<script
		src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.10.0.js"
		type="text/javascript"></script>
	<script src="../resources/js/angular-animate.min.js"></script>
	<script src="../resources/js/angular-aria.min.js"></script>
	<script src="../resources/js/angular-messages.min.js"></script>
	<script
		src="http://ajax.googleapis.com/ajax/libs/angular_material/1.1.0-rc2/angular-material.min.js"></script>
	<script src="../resources/js/angularController/cmsViewController.js"></script>
	<script src="../resources/js/angularService/cmsService.js"></script>
</body>

</html>
