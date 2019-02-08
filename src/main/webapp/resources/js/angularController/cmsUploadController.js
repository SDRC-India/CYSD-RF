var app = angular.module("uploadApp", [ 'ui.bootstrap', 'ngMaterial',
		'ngMessages' ]);

app.config([ '$mdIconProvider', function($mdIconProvider) {
	$mdIconProvider.icon('md-close', 'img/icons/ic_close_24px.svg', 24);
} ])

app
		.controller(
				'UploadController',
				[
						'$scope',
						'$http',
						'GetAllSection',
						'GetAllReportType',
						'$timeout',
						'CategoriesService',
						function($scope, $http, GetAllSection,
								GetAllReportType, $timeout, CategoriesService) {
							$scope.sections = [];
							$scope.themes = [];
							$scope.allReportTypes = [];
							$scope.selectedReport = {};
							$scope.selectedReport.tags = [];
							$scope.errorMessage = "";
							$scope.parentId = 0;
							// get section and theme
							GetAllSection
									.getAll()
									.then(
											function(sections) {
												$scope.sectionAndThemes = sections;
												for (var i = 0; i < $scope.sectionAndThemes.length; i++) {
													if ($scope.sectionAndThemes[i].parentId == -1) {
														$scope.sections
																.push($scope.sectionAndThemes[i]);
													}
												}
											});
							GetAllReportType.getAll().then(
									function(reportTypes) {
										$scope.allReportTypes = reportTypes;
									});

							$scope.setPathFileDoc = function(file, x, y) {
								document.getElementById(x).value = "";
								$scope.showValue = "";
								$scope.biggerSizeDoc = false;
								$scope.wrongType = false;

								$scope.totalFileSize = 0;
								for (var i = 0; i < document.getElementById(y).files.length; i++) {

									$scope.totalFileSize += document
											.getElementById(y).files[i].size;

								}

								if (x == 'uploadFileDoc') {
									if ($scope.totalFileSize > 10000000) {
										$scope.biggerSizeDoc = true;
										$("#errorValidation strong")
												.text(
														"File Size is Must not exceed 10MB");

										$scope.$apply();
										document.getElementById(y).value = "";
									}
									$(".loader").css("display", "block");
									for (var i = 0; i < document
											.getElementById(y).files.length; i++) {
										$scope.ext = document.getElementById(y).files[i].name
												.split('.').pop();
										$scope.fileName = document
												.getElementById(y).files[i].name
												.split("\\").pop();

										if ($scope.ext.toLowerCase() != "pdf"
												&& $scope.ext.toLowerCase() != "txt"
												&& $scope.ext.toLowerCase() != "docx"
													&& $scope.ext.toLowerCase() != "doc"
												&& $scope.ext.toLowerCase() != "xls"
												&& $scope.ext.toLowerCase() != "xlsx") {
											$scope.wrongType = true;
											$("#errorValidation strong")
													.text(
															"All file type sholud be text/word/excel");
											document.getElementById(y).value = "";
										} else {

											$scope.showValue += $scope.fileName;
											if (i != document.getElementById(y).files.length - 1) {
												$scope.showValue += ",";
											}
										}
									}
									$(".loader").css("display", "none");
								} else if (x == 'uploadFileAud') {
									if ($scope.totalFileSize > 10000000) {
										$scope.biggerSizeDoc = true;
										$("#errorValidation strong").text(
												"File Size Must be below 10MB");

										$scope.$apply();
										document.getElementById(y).value = "";
									}
									$(".loader").css("display", "block");
									for (var i = 0; i < document
											.getElementById(y).files.length; i++) {
										$scope.ext = document.getElementById(y).files[i].name
												.split('.').pop();
										$scope.fileName = document
												.getElementById(y).files[i].name
												.split("\\").pop();

										if ($scope.ext.toLowerCase() != "mp3"
												&& $scope.ext.toLowerCase() != "wma"
												&& $scope.ext.toLowerCase() != "wav") {
											$scope.wrongType = true;
											$("#errorValidation strong")
													.text(
															"All file type sholud be mp3/wma/wav");
											document.getElementById(y).value = "";
										} else {

											$scope.showValue += $scope.fileName;
											if (i != document.getElementById(y).files.length - 1) {
												$scope.showValue += ",";
											}
										}
									}
									$(".loader").css("display", "none");
								}

								else if (x == 'uploadFileImg') {
									if ($scope.totalFileSize > 10000000) {
										$scope.biggerSizeDoc = true;
										$("#errorValidation strong").text(
												"File Size must be below 10MB");

										$scope.$apply();
										document.getElementById(y).value = "";
									}
									$(".loader").css("display", "block");
									for (var i = 0; i < document
											.getElementById(y).files.length; i++) {
										$scope.ext = document.getElementById(y).files[i].name
												.split('.').pop();
										$scope.fileName = document
												.getElementById(y).files[i].name
												.split("\\").pop();

										if ($scope.ext.toLowerCase() != "jpg"
												&& $scope.ext.toLowerCase() != "jpeg"
												&& $scope.ext.toLowerCase() != "png"
												&& $scope.ext.toLowerCase() != "gif") {
											$scope.wrongType = true;
											$("#errorValidation strong")
													.text(
															"Please upload only images");
											document.getElementById(y).value = "";
										} else {

											$scope.showValue += $scope.fileName;
											if (i != document.getElementById(y).files.length - 1) {
												$scope.showValue += ",";
											}
										}
									}
									$(".loader").css("display", "none");
								}
								if (!$scope.biggerSizeDoc && !$scope.wrongType) {
									document.getElementById(x).value = $scope.showValue;
								} else {
									$("#errorValidation").show().delay(3000)
											.fadeOut();
								}

							};
							$scope.$watch("$viewContentLoaded", function() {
								$timeout(function() {
									$(".require").next().next("select").attr(
											"required", "required");
									$("input").change(function() {
										$(this).value = $.trim($(this).value);
									});
								}, 500);

							});
							$scope.resetOther = function(level) {
								if (level == 1) {
									$scope.selectedReport.themeId = undefined;
								}

							};

							/*
							 * chips suggestion
							 */

							var vm = $scope;

							vm.selectedReport.tags = [];
							vm.autocomplete = {
								selectedItem : null,
								searchText : null
							};

							vm.onCategoryRemoved = function($chip, $index) {
								console.log('onCategoryRemoved', $chip, $index);
								if (!vm.autocomplete.searchText) {
									vm.autocomplete.searchText = null;
								}
							};
							vm.onCategoryAdded = function($chip, $index) {
								console.log('onCategoryAdded', $chip, $index);
								if (typeof $chip == "string") {
									$chip = {
										name : $chip
									};
								}
							};

							vm.onSearchTextChanged = function() {
								for (var i = 0; i < vm.selectedReport.tags.length; i++) {
									if (typeof vm.selectedReport.tags[i] == "string") {
										vm.selectedReport.tags[i] = {
											name : vm.selectedReport.tags[i]
										};
									}
								}
								$scope.tagsInstring = "";
								for (var i = 0; i < vm.selectedReport.tags.length; i++) {
									if (vm.selectedReport.tags[i].name
											&& i != vm.selectedReport.tags.length - 1) {
										$scope.tagsInstring += vm.selectedReport.tags[i].name
												+ ",";
									} else {
										$scope.tagsInstring += vm.selectedReport.tags[i].name;
									}
								}
							};
							vm.searchCategories = function() {
								$scope.selectedTheme = {};
								for (var i = 0; i < $scope.sectionAndThemes.length; i++) {
									if ($scope.sectionAndThemes[i].sectionId == $scope.selectedReport.themeId) {
										$scope.selectedTheme = $scope.sectionAndThemes[i];
									}
								}
								$scope.tagElement = {};
								$scope.tagList = [];
								if ($scope.selectedTheme.tagName != ""
										&& $scope.selectedTheme.tagName != null) {
									$scope.tagArray = $scope.selectedTheme.tagName
											.split(",");
									for (var i = 0; i < $scope.tagArray.length; i++) {
										$scope.tagList[i] = {
											name : $scope.tagArray[i]
										};
									}
								}

								var se = CategoriesService.find(
										vm.autocomplete.searchText
												.toLowerCase(),
										vm.selectedReport.tags, $scope.tagList);

								return se;
							};

						} ]);