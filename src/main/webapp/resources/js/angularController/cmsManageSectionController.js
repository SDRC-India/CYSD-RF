var app = angular.module('manageSection', ['ui.bootstrap','ngMaterial', 'ngMessages' ]);

app.controller('ManageSectionController', ['$scope', '$http', 'GetAllSection', function($scope, $http, GetAllSection){
	$scope.sections=[];
	$scope.themes = [];
	$scope.errorMessage = "";
	$scope.parentId = -1;
	$scope.selectedSection = {};
	$scope.selectedSection.tags = [];
	$scope.section = {};
	$scope.section.tags = [];
	$scope.sectionTheme = {};
	$scope.sectionTheme.sectionId = "";
	//get section and theme
	GetAllSection.getAll().then(function(sections){
		$scope.sectionAndThemes = sections;
		for(var i=0; i<$scope.sectionAndThemes.length; i++){
			if($scope.sectionAndThemes[i].parentId == -1){
				$scope.sections.push($scope.sectionAndThemes[i]);
				console.log(sections);
			}
		}	
	});
	$scope.editSectionModal = function(section){
		$(".edit-sectionModal").modal("show");
		$scope.selectedSection.title = section.sectionName;
		$scope.selectedSection.description = section.description;
		$scope.selectedSection.sectionId = section.sectionId;
	};
	$scope.editThemeModal = function(section){
		$(".edit-themeModal").modal("show");
		$scope.selectedSection.title = section.sectionName;
		$scope.selectedSection.description = section.description;
		$scope.selectedSection.sectionId = section.sectionId;
		$scope.selectedSection.parentId = section.parentId;
		$scope.selectedSection.tags = section.tagName.split(",");
	};
	$scope.addSectionModal = function(){
		$(".add-sectionModal").modal("show");
		$scope.section = {};
		$scope.documentForm.$setPristine();
		
		setTimeout(function(){
			$(".md-input-invalid").removeClass("md-input-invalid");
		}, 50);
	};
	$scope.addThemeModal = function(){
		$(".add-themeModal").modal("show");
		$scope.sectionTheme = {};
		$scope.documentForm.$setPristine();
		$scope.section.tags = [];
		setTimeout(function(){
			$(".md-input-invalid").removeClass("md-input-invalid");
		}, 50);
		
	};
}]);