operationApp.controller('OperationController',['$scope','$timeout','$http','cService','$rootScope','fileUpload','$window',
                                               function($scope,$timeout,$http,cService,$rootScope,fileUpload,$window){
	
	$scope.currentTimeperiod = [];
	$scope.isValidUpload = true;
	$rootScope.view = 'operation'
		
	$scope.start = function(){
		$http.get('cysd/getCurrentTimeperiod').success(function(data){
			$scope.currentTimeperiod = data;
			$scope.selectedTimePeriod = $scope.currentTimeperiod[0];
    	});
		
	};
	
	$scope.selectTimePeriod = function(tp){
		$scope.selectedTimePeriod = tp;
	};
	
	$rootScope.$on('projectDataPopulated', function(event, data) {
		$scope.allProjects = data;
		$scope.selectedProject = data[0];
	});
	
	cService.getProject();
	
	$scope.selectProject = function(project){
		$scope.selectedProject = project;
	}
	
	$scope.uploadFile = function(){
		$scope.showConfirmationModal = false;
        var file = $scope.myFile;
        var ext = file.name.substr(file.name.lastIndexOf('.')+1)
        if(ext == 'xls'){
        	 $scope.isValidUpload = true;
        	 $scope.showLoaderModal = true;
//        	 var projects = JSON.stringify($scope.selectedProjects);
//             var uploadUrl = 'uploadTarget?timePeriodId='+$scope.selectedTimePeriod.key+
//     				'&timePeriod='+$scope.selectedTimePeriod.value+
//     				'&frameworkType='+$scope.selectedFramework.key+
//     				'&uploadType='+$scope.option;
//     				'&fileName='+$scope.fileName;
             //file, url, list of projects, callback function
             //save txn data**************
             fileUpload.uploadFileToUrl(file, uploadUrl, projects, function() {
                 // "this" is the XHR object here!
                 var resp  = JSON.parse(this.responseText);
                 /* now do something with resp */
                 if(resp.split("_")[1]=="true"){
                	$scope.showLoaderModal = false;
                    $("#alert_placeholder").show();
                    if($scope.option == "target")
                    	$('#alert_placeholder').html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>Planned file uploaded successfully. Please download the uploaded planned file to fill achieve .</span></div>');
                    else
                    	$('#alert_placeholder').html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>Achieved file uploaded successfully !</span></div>');
                    $("#alert_placeholder").delay(3000).fadeOut(3000);
                 }else{
                 	$( "#alert_placeholder").show();
             		$( '#alert_placeholder').html('<div style="text-align: center;" class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><span>File upload failed ! '+resp.split("_")[0]+'</span></div>');
             		$( "#alert_placeholder").delay(3000).fadeOut(3000);
                 }
                 $scope.getActiveClasses();
//                 location.reload();
               });
        }else {
        	$scope.isValidUpload = false;
        }
    };
	
	$scope.start();
}]);