var app = angular.module("viewApp", ['ui.bootstrap','ngMaterial', 'ngMessages' ]);

app.filter('startFrom', function () {
	return function (input, start) {
		if (input) {
			start = +start;
			return input.slice(start);
		}
		return [];
	};
});

app.controller("ViewController", ['$scope', '$http', 'GetAllSection', 'GetAllReport', 'filterFilter', 'GetAllReportType', 'GetAllTags', '$timeout',  
                                  function($scope, $http, GetAllSection, GetAllReport, filterFilter, GetAllReportType, GetAllTags, $timeout){
	
	$scope.sections=[];
	$scope.themes = [];
	$scope.selectedSection = '';
	$scope.allReports = [];
	$scope.allTags = [];
	$scope.selectedReport = {};
	$scope.selectedReport.tags = "";
	$scope.userId = userId;
	$scope.role = role;
	var oldPhotos=[];
	/*$scope.searchKey = function (newVal) {
		$scope.search = undefined;
		
		if($scope.searchKeyword){
			$scope.filtered = [];
			for(var i=0; i<$scope.allReports.length; i++){
				if($scope.allReports[i].reportName.toLowerCase().includes($scope.searchKeyword.toLowerCase()) 
						|| $scope.allReports[i].description.toLowerCase().includes($scope.searchKeyword.toLowerCase())){
					$scope.filtered.push($scope.allReports[i]);
				}
			}
		}	
	
    };
    
    $scope.$watch('searchKeyword', function(newVal, oldVal){
    	$scope.searchKey(newVal);
    })*/
	
	

	$scope.resetFilters = function () {
		// needs to be a function or it won't trigger a $watch
		$scope.search = {};
	};

	$scope.initSearchFilter = function(){
		// pagination controls
		$scope.search = {};
		$scope.currentPage = 1;
		$scope.totalItems = $scope.allReports.length;
		$scope.entryLimit = 5; // items per page
		$scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);
	};
	
	
	// $watch search to update pagination
	$scope.$watch('search', function (newVal, oldVal) {
		$scope.filtered = filterFilter($scope.allReports, newVal);
		$scope.totalItems = $scope.filtered.length;
		$scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);
		$scope.currentPage = 1;
	}, true);
	$(".loader").show();
	//get section and theme
	GetAllSection.getAll().then(function(sections){
		$scope.sectionAndThemes = sections;
		for(var i=0; i<$scope.sectionAndThemes.length; i++){
			if($scope.sectionAndThemes[i].parentId == -1){
				$scope.sections.push($scope.sectionAndThemes[i]);
			}
		}
		
	});
	GetAllReport.getAll().then(function(reports){
		$(".loader").fadeOut(500);
		$scope.allReports = reports;
		$scope.noOfPages = Math.ceil($scope.allReports / $scope.entryLimit);
		$scope.initSearchFilter();
	});
	GetAllReportType.getAll().then(function(reportTypes){
		$scope.allReportTypes = reportTypes;
	});
	GetAllTags.getAll().then(function(tags){
		$scope.allTags = tags;
	});
	
	$scope.setQuery = function(tag){
		$scope.query = tag.tagName;
		$scope.selected = true;
	};
	
	$scope.openEditModal = function(report){
		$(".edit-modal").modal('show');
		$scope.selectedReport.tags = report.tags ? report.tags.split(','): [];
		$scope.selectedReport.description = report.description;
		$scope.selectedReport.title = report.reportName;
		$scope.selectedReport.reportId = report.reportId;
	};
	$scope.viewReportModal = function(report){
		console.log(report);
		if(report.type == "Photographs"){
			$scope.photos = report.url.split("}");
			var slider = $('.flexslider');
			if(slider.data('flexslider')!=null && !(angular.equals($scope.photos, oldPhotos)))
				{
			while (slider.data('flexslider').count > 0) 
			                    slider.data('flexslider').removeSlide(0);
			$('.flexslider').removeData("flexslider");
				
				}
			oldPhotos=report.url.split("}");
			$(".view-image-modal").modal('show');
			$timeout(function(){
				$('.flexslider').flexslider({
				    animation: "slide"	,
				    	controlNav: true,
				        animationLoop: false,
				  });
			}, 500);
		}
		else if(report.type == "Visuals"){
			window.open(report.url, '_blank');
		}
		else{
		
			window.open("../downloadFile?reportId="+report.reportId);
		}
	};
	$scope.deleteReport = function(report){
		window.location.href = "../deleteRecord?reportId="+report.reportId;
	};
	$scope.searchKeyword = function(){
		$(".loader").show().delay(500).fadeOut(500);
		$scope.querySearch = $scope.query;
		$scope.selected = true;
		$timeout(function(){
			$scope.totalItems = $scope.filtered.length;
			$scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);
			$scope.currentPage = 1;
		}, 500);
		
	};
	$scope.showLoader = function(){
		$(".loader").show().delay(500).fadeOut(500);
	};
	$scope.resetOther = function(level){
		if(level == 1){
			$scope.search.themeId = undefined;
			$scope.search.type = undefined;
			$scope.query = null;
			$scope.querySearch = null;
		}
		if(level == 2){
			$scope.search.type = undefined;
			$scope.query = null;
			$scope.querySearch = null;
		}
		if(level == 3){
			$scope.query = null;
			$scope.querySearch = null;
		}
	};
}]);