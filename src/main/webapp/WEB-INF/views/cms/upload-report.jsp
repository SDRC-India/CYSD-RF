<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>

<html lang="en">


<jsp:include page="../fragments/cms-headtag.jsp" />

<body ng-app="uploadApp" ng-controller="UploadController" ng-cloak>
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
					<h3>Upload Document</h3>
				</div>
			</div>
			<div class="col-sm-12 filter-options">
				<form id="upload-report" enctype="multipart/form-data"
					name="documentForm" action="../saveReport" method="post">
					<div class="col-sm-4">
						<md-input-container flex="100"> <label>Section *</label>
						<md-select name="sectionIdS" class="require"
							ng-model="selectedReport.sectionId" required> <md-option
							ng-repeat="section in sections" ng-click="resetOther(1)"
							ng-value="section.sectionId">{{section.sectionName}}</md-option>
						</md-select>
						<div class="errors" ng-messages="documentForm.sectionIdS.$error">
							<div ng-message="required">Required</div>
						</div>
						</md-input-container>
						<input name="parentSectionId" ng-show="false"
							ng-model="selectedReport.sectionId">
					</div>

					<div class="col-sm-4">

						<md-input-container flex="100"> 
							<label>Theme *</label>
							<md-select name="themeIdS" class="require"
								ng-model="selectedReport.themeId" required>
								<md-option ng-repeat="theme in sectionAndThemes"
								ng-if="theme.parentId == selectedReport.sectionId"
								ng-value="theme.sectionId">{{theme.sectionName}}</md-option> 
							</md-select>
						<div class="errors" ng-messages="documentForm.themeIdS.$error">
							<div ng-message="required">Required</div></div>
						</md-input-container>
					
						<input name="sectionId" id="secId" ng-show="false"
						ng-model="selectedReport.themeId" required> <input
						name="parentId" ng-show="false" ng-model="parentId">
					</div>

			<div class="col-sm-4">
				<md-input-container flex="100"> <label>Type *</label>
				<md-select name="reportTypeS" class="require"
					ng-model="selectedReport.type" required> <md-option
					ng-repeat="type in allReportTypes" ng-value="type.reportTypeName">{{type.reportTypeName}}</md-option>
				</md-select>
				<div class="errors" ng-messages="documentForm.reportTypeS.$error">
					<div ng-message="required">Required</div>
				</md-input-container>
			</div>
			</md-input-container>
			<input name="reportType" ng-show="false"
				ng-model="selectedReport.type">
		</div>
		<div class="col-sm-12">
			<md-input-container flex="50"> <label>Report
				Title</label> <input required md-maxlength="150" maxlength="150"
				name="reportTitle" ng-model="selectedReport.title">
			<div ng-messages="documentForm.reportTitle.$error">
				<div ng-message="required">This is required.</div>
			</div>
			</md-input-container>
		</div>
		<div class="col-sm-12">
			<md-input-container class="md-block"> <label
				class="md-mandatory">Document Description *</label> <input
				md-maxlength="300" required maxlength="300" md-no-asterisk name="desc"
				ng-model="selectedReport.description" ng-trim="true" >
			<div ng-messages="documentForm.desc.$error">
				<div ng-message="required">This is required.</div>
				<div ng-message="md-maxlength">The description must be less
					than 300 characters long.</div>
			</div>
			</md-input-container>
		</div>
		<div class="col-sm-6">
			<md-chips ng-model="selectedReport.tags"
			md-autocomplete-snap=""
			
				md-on-add="onCategoryAdded($chip, $index)"
				md-on-remove="onCategoryRemoved($chip, $index)"> 
				<md-autocomplete
				md-selected-item="autocomplete.selectedItem"
				md-search-text="autocomplete.searchText"
				md-selected-item-change="onSelectedItemChanged()"
				md-search-text-change="onSearchTextChanged()"
				md-items="item in searchCategories()" md-item-text="item.name"
				md-min-length="1" md-select-on-match="false"
				md-match-case-insensitive="true" md-no-cache="true"
				placeholder="Add Tag"
				secondary-placeholder="+Tag"> <span
				md-highlight-text="autocomplete.searchText"
				md-highlight-flags="i">{{item.name}}</span> 
				</md-autocomplete> 
			<md-chip-template>
			<span> <strong>{{$chip.name}}</strong>
			</span> </md-chip-template> </md-chips>

		</div>
		<md-input-container flex="50" ng-if="selectedReport.tags">
		<input ng-show="false" name="tags" ng-if="selectedReport.tags"
			ng-model="tagsInstring"> </md-input-container>
		<div class="clearfix"></div>
		<div class="col-sm-12" style="margin-top: 15px;">
			<div
				ng-if="selectedReport.type == 'Reports' || selectedReport.type == 'Case Studies' || selectedReport.type == 'Other Reports' || selectedReport.type == 'Concept Notes/Proposals'  || selectedReport.type == 'Confidential' ||selectedReport.type == 'Catalog'">
				<md-input-container flex="50"> <label>Upload
					File(.txt, .docx, .pdf, .xlsx) [Max-size : 10MB]</label> <input required id="uploadFileDoc"
					placeholder="Click Browse" disabled="disabled" name="uploadFileDoc"
					ng-model="selectedReport.uploadFileDocName"
					style="color: #000; min-width: 400px;">
				<div ng-messages="documentForm.uploadFileDocName.$error">
					<div ng-message="required">This is required.</div>
				</div>
				</md-input-container>
				<div class="fileUpload btn">
					<span>Browse</span> <input id="uploadBtnDoc" type="file"
						name="file" ng-model="selectedReport.uploadFileDoc" class="upload"
						onchange="angular.element(this).scope().setPathFileDoc(this, 'uploadFileDoc', 'uploadBtnDoc')"
						required />

				</div>
			</div>
			<div ng-if="selectedReport.type == 'Photographs'">
				<md-input-container flex="50"> <label>Upload
					Photographs(.jpg, .png, .gif) [Max-size : 10MB]</label> <input required id="uploadFileImg"
					placeholder="Click Browse" disabled="disabled" multiple
					name="uploadFileImg" ng-model="selectedReport.uploadFileImgName"
					style="color: #000; min-width: 400px;">
				<div ng-messages="documentForm.uploadFileImgName.$error">
					<div ng-message="required">This is required.</div>
				</div>
				</md-input-container>
				<div class="fileUpload btn">
					<span>Browse</span> <input id="uploadBtnImg" type="file" multiple
						name="file" ng-model="selectedReport.uploadFileImg" class="upload"
						onchange="angular.element(this).scope().setPathFileDoc(this, 'uploadFileImg', 'uploadBtnImg')"
						required />

				</div>
			</div>
			<div ng-if="selectedReport.type == 'Audio'">
				<md-input-container flex="50"> <label>Upload
					Audio(.mp3, .wav, .wma) [Max-size : 10MB]</label> <input required id="uploadFileAud"
					placeholder="Click Browse" disabled="disabled" multiple
					name="uploadFileAud" ng-model="selectedReport.uploadFileAudName"
					style="color: #000; min-width: 400px;">
				<div ng-messages="documentForm.uploadFileAudName.$error">
					<div ng-message="required">This is required.</div>
				</div>
				</md-input-container>
				<div class="fileUpload btn">
					<span>Browse</span> <input id="uploadBtnAud" type="file" multiple
						name="file" ng-model="selectedReport.uploadFileAud" class="upload"
						onchange="angular.element(this).scope().setPathFileDoc(this, 'uploadFileAud', 'uploadBtnAud')"
						required />

				</div>
			</div>
			<div ng-if="selectedReport.type == 'Visuals'">
				<md-input-container flex="50"> <label>Youtube
					Video Link</label> <input required name="url"
					ng-model="selectedReport.video" style="min-width: 325px;">

				<div ng-messages="documentForm.url.$error">
					<div ng-message="required">This is required.</div>
				</div>

				</md-input-container>


			</div>
		</div>

		<!-- <div ng-if="docType == 'weblink'">
						<md-input-container flex="50"> <label>Website
							Link</label> <input required name="url" ng-model="file.weblink"
							style="min-width: 325px;">

						<div ng-messages="documentForm.url.$error">
							<div ng-message="required">This is required.</div>
						</div>

						</md-input-container>


					</div>

					<div ng-if="docType == 'audio'">
						<md-input-container flex="50"> <label>Upload
							File(.mp3, .wav, .wma)</label> <input required id="uploadFileAud"
							placeholder="Click Browse" disabled="disabled"
							name="uploadFileAud" ng-model="file.uploadFileAud"
							style="color: #000; min-width: 400px;">
						<div ng-messages="documentForm.uploadFileAud.$error">
							<div ng-message="required">This is required.</div>
						</div>
						<div ng-show="biggerSize">
							<div style="color: #F00; font-size: 12px; margin-top: 10px;">File
								size should not exceed 2MB</div>
						</div>
						</md-input-container>

						<div class="fileUpload btn">
							<span>Browse</span> <input id="uploadBtnAud" type="file"
								name="filedata" class="upload form-control"
								ng-model="file.uploadFile"
								onchange="angular.element(this).scope().setPathFileAud(this, 'uploadFileAud', 'uploadBtnAud')"
								required />

						</div>
					</div> -->

		<div class="clearfix"></div>


		<md-button type="submit"
			style="background-color: #415684; color: #FFF;">Submit</md-button>
		</form>
	</div>
	</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#msgBox").delay(2000).fadeOut(2000);
			$("#msgBoxlogin").delay(2000).fadeOut(2000);
			$("#upload-report").submit(function() {
				$(".loader").css("display", "block");
			})
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
	<script src="../resources/js/angularController/cmsUploadController.js"></script>
	<script src="../resources/js/angularService/cmsService.js"></script>

</body>

</html>