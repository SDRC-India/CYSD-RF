workspaceApp.controller('WorkspaceController',['$scope','$timeout','$http','cService','$rootScope','fileUpload','$window',
                                               function($scope,$timeout,$http,cService,$rootScope,fileUpload,$window){
	$scope.isCollapsed = true;
	$scope.isStartDatePickerOpen = false;
	$scope.isEndDatePickerOpen = false;
	$scope.isUploadDatePickerOpen = false;
	$scope.option="target";
	$scope.timePeriodList = [];
	$scope.selectedProjects = [];
	$scope.selectedTimePeriod ='';
	$scope.timePeriods = [];
	$scope.allProjects = [];
	$scope.typeOfFrameworks = [];
	$rootScope.view = 'workspace';
	$scope.isUpload = false;
	$scope.fileName='';
	$scope.activeClasses = '';
	$scope.isTarget = true;
	$scope.isValidUpload = true;
	$scope.showLoaderModal = true;
	$scope.currentTimeperiod = [];
	$scope.isSaveActive = false;
	$scope.wasSaveActive = false;
	
	$scope.showConfirmationModal = false;
	$scope.downloadTooltipText = '';
	
	
	$scope.openStartDatePicker = function($event) {
	    $event.preventDefault();
	    $event.stopPropagation();
	    $scope.isStartDatePickerOpen = true;
	    $scope.isEndDatePickerOpen = false;
	};
	
	$scope.openEndDatePicker = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.isStartDatePickerOpen = false;
		$scope.isEndDatePickerOpen = true;
	};
	
	$scope.openUploadDatePicker = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.isUploadDatePickerOpen = true;
	};
	
	
	
	$scope.addDropDown = function(){
		$('#projectSelect').empty();
		$("#projectSelect option:selected").removeAttr("selected");
		for(var k = 0; k < $scope.allProjects.length; k++){
			$('#projectSelect').append($('<option>', {
				value : $scope.allProjects[k].key,
				text : $scope.allProjects[k].value,
			}));
		}	
		$('#projectSelect').multiselect('rebuild');
	}
	
	$scope.start = function(){
		
		$http.get('getCurrentTimeperiod').success(function(data){
			$scope.currentTimeperiod = data;
			$scope.selectedTimePeriod = $scope.currentTimeperiod[0];
    		if($scope.selectedTimePeriod){
    			$http.get('getAllFramework').success(function(data){
    				$scope.typeOfFrameworks = data;
    				$scope.selectedFramework = $scope.typeOfFrameworks[0];
    				if($scope.selectedTimePeriod && $scope.selectedFramework){
    					$scope.getActiveClasses();
    				}
    			});
    		}
    	});

		$scope.getActiveClasses = function(){
			$http.get('getActiveClass?timePeriodId='
					+$scope.selectedTimePeriod.key+'&frameworkTypeId='+$scope.selectedFramework.key
					+'&frameworkTypeName='+$scope.selectedFramework.value).success(function(data){
				$scope.activeClasses = data;
				if($scope.activeClasses.targetUploadActive && $scope.activeClasses.projectActive){
					$scope.downloadTooltipText = 'Download saved blank planned template';
				}else if(!$scope.activeClasses.targetUploadActive && $scope.activeClasses.projectActive){
					$scope.downloadTooltipText = 'Download saved blank planned template';
				}else if(!$scope.activeClasses.targetUploadActive && !$scope.activeClasses.projectActive){
					$scope.downloadTooltipText = 'Download uploaded planned file';
				}
				if($scope.activeClasses.targetActive == false){
					$scope.isTarget = false;
					$scope.option = "achieved"
				}
				$scope.showLoaderModal = false;
			});
		}
		
		cService.getProject();
		
		$('#projectSelect').multiselect({
	      buttonWidth : '100%',
		  includeSelectAllOption : true,
		  enableFiltering : true,
		  enableCaseInsensitiveFiltering : true,
		  onDropdownHidden : function(event) {
			  var selected = [];
			  selected = $('#projectSelect').val();
			  if(selected != null && selected.length!=0){
				  if($scope.activeClasses.saveActive){
					  $scope.isSaveActive = true;
					  $scope.$apply();
				  }
				  $scope.selectedProjects =[];
				  for(i=0;i<$scope.allProjects.length;i++){
					  for(j=0;j<selected.length;j++){
						  if($scope.allProjects[i].key==selected[j]){
//							  $scope.selectedProjects.push({
//								  key:$scope.allProjects[i].key,
//								  value:$scope.allProjects[i].value
//							  });
							  
							  $scope.selectedProjects.push($scope.allProjects[i].key);
							  
						  }
					  }
				  }
				  $('#projectSelect').multiselect('rebuild');
			  }else{
				  $scope.isSaveActive = false;
				  $scope.$apply();
			  }
			}
	      });
		
		$('#operationProjectSelect').multiselect({
			buttonWidth : '100%',
			includeSelectAllOption : true,
			enableFiltering : true,
			enableCaseInsensitiveFiltering : true,
			onDropdownHidden : function(event) {
				
			}
		});
		
	};
	
	$scope.start();
	
	$scope.selectTimePeriod = function(tp){
		$scope.selectedTimePeriod = tp;
		$('#projectSelect').multiselect("clearSelection");
		$scope.isSaveActive = false;
		$scope.getActiveClasses();
	}
	
	$scope.selectFramework = function(framework){
		if(framework.value=='Operations')
			$scope.isSaveActive = false;
		
		if(framework.value=='Programme' && $('#projectSelect').val()!=null)
			$scope.isSaveActive = true;
		$scope.selectedFramework = framework;
		$scope.getActiveClasses();
	}
	
	$rootScope.$on('projectDataPopulated', function(event, data) {
		$scope.allProjects = data
		$scope.addDropDown();
	});
	
	
	
	$scope.downloadTemplate = function(){
		$scope.showLoaderModal= true;
		$http({
			url: 
				'fileName?downloadType='+$scope.option+
				'&timePeriod='+$scope.selectedTimePeriod.value+
				'&timePeriodId='+$scope.selectedTimePeriod.key+
				'&frameworkType='+$scope.selectedFramework.key+
				'&frameworkName='+$scope.selectedFramework.value,
			method: 'POST',
//			data: JSON.stringify($scope.selectedProjects)
		}
				).success(function(data){
					if(null!=data){
						$scope.fileName = data;
						var fileName = {"fileName" :data};
						$.download("downloadFile",fileName,'POST');
						$scope.getActiveClasses();
					}else{
						$("#alert_placeholder").show();
	             		$("#alert_placeholder").html('<div style="text-align: center;" class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><span>No file is present for the current selection!</span></div>');
	             		$("#alert_placeholder").delay(2000).fadeOut(2000);
					}
				});
	}
	$scope.saveFile = function(){
		$scope.showLoaderModal= true;
		$http({
			url: 
				'saveFile?timePeriodId='+$scope.selectedTimePeriod.key+
				'&timePeriod='+$scope.selectedTimePeriod.value+
				'&frameworkType='+$scope.selectedFramework.key+
				'&uploadType='+$scope.option+
				'&project='+JSON.stringify($scope.selectedProjects),
//				'&fileName='+$scope.fileName,
			method: 'POST',
			data: JSON.stringify($scope.selectedProjects)
		}
				).success(function(data){
					$scope.addDropDown();
					$scope.getActiveClasses();
					$scope.isSaveActive = false;
					$scope.showLoaderModal= false;
					$("#alert_placeholder").show();
             		$("#alert_placeholder").html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>Planned template has been created for the selected projects and quarter ('+$scope.selectedTimePeriod.value+')</span></div>');
             		$("#alert_placeholder").delay(3000).fadeOut(2000);
				});
	}
	
	$scope.uploadFile = function(){
		$scope.showLoaderModal = true;
		$scope.showConfirmationModal = false;
        var file = $scope.myFile;
        var ext = file.name.substr(file.name.lastIndexOf('.')+1)
        if(ext == 'xls'){
        	 $scope.isValidUpload = true;
        	 var projects = JSON.stringify($scope.selectedProjects);
             var uploadUrl = 'uploadTarget?timePeriodId='+$scope.selectedTimePeriod.key+
     				'&timePeriod='+$scope.selectedTimePeriod.value+
     				'&frameworkType='+$scope.selectedFramework.key+
     				'&uploadType='+$scope.option+
     				'&frameworkName='+$scope.selectedFramework.value;
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
	
}]);

//angular directive for file upload
workspaceApp.directive('fileModel', ['$parse', function ($parse) {
    return {
       restrict: 'A',
       link: function(scope, element, attrs) {
          var model = $parse(attrs.fileModel);
          var modelSetter = model.assign;
          
          element.bind('click', function(){
	          scope.$apply(function(){
	        	  element.val(null);
	          });
	       });
          
          element.bind('change', function(){
             scope.$apply(function(){
                modelSetter(scope, element[0].files[0]);
                scope.showConfirmationModal = true;
             });
          });
       }
    };
 }]);

//service to upload file
workspaceApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl, projects, callback){
       var fd = new FormData();
//       fd.append('project',projects);
       fd.append('file', file);
    
       var request = new XMLHttpRequest();
       request.open('POST', uploadUrl, true);
       
       request.onreadystatechange = function() {
    	    if (request.readyState == 4) {
    	      // defensive check
    	      if (typeof callback == "function") {
    	        // apply() sets the meaning of "this" in the callback
    	        callback.apply(request);
    	      }
    	    }
    	  }
      request.send(fd);
    }
 }]);