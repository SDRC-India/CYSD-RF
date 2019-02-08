superAdminApp.controller('SuperAdminController',['$scope','$http',function($scope, $http){
	
	$scope.view = "state"

	
	$scope.saveProject = function(){
		$http.post('createNewProject?icName='+$scope.projectName+'&icType='+$scope.view)
			.success(function(data){
			if(data==true){
				$scope.projectName = "";
				$("#alert_placeholder").show();
         		$("#alert_placeholder").html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>New '+$scope.view+' project has been successfully created.</span></div>');
         		$("#alert_placeholder").delay(3000).fadeOut(2000);
			}
		})
	};	
		

	$scope.projectName = "";

}]);


