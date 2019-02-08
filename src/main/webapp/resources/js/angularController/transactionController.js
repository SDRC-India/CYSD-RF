var tp="";

txnApp.controller('TransactionController',['$scope','$http',
                                               function($scope,$http){
	$scope.timePeriodList = [];
	$scope.quarterList=[];
	$scope.selectedYear='';
	$scope.timePeriodListString='';
	
	$scope.start = function(){
		
		$http.get('/CYSDRF/getAllTimePeriod').success(function(data){
    		var year = Object.keys(data);
    		var timePeriod=[];
    		for(i=0;i<year.length;i++){
    			var quarter=[];
    			var quarterLength = Object.keys(data[Object.keys(data)[i]]).length;
    			var quarterKeys = Object.keys(data[Object.keys(data)[i]]);
    			for(j=0;j<quarterLength;j++){
    				quarter.push({
    					key:quarterKeys[j],
    					value:data[Object.keys(data)[i]][quarterKeys[j]]
    				})
    			}
    			var time = {
    					value:year[i],
    					quarter:quarter
    			};
    			timePeriod.push(time);
    		}
    		$scope.timePeriodList=timePeriod;
    		$scope.selectedYear = $scope.timePeriodList[0];
    		$scope.selectYear($scope.selectedYear);
    	});
		
	}
	
	$scope.start();
	
	  $scope.selectYear = function(year){
		  $scope.timePeriodListString='';
		  $scope.quarterList=[];
		  $scope.selectedTimePeriod ='';
		  $scope.selectedYear = year;
		  $scope.quarterList = year.quarter;
		  $scope.selectedTimePeriod = $scope.quarterList[0];
		  tp= $scope.selectedTimePeriod.key;
		  for(i=0;i<year.quarter.length;i++){
			  if(i==0)
				  $scope.timePeriodListString += year.quarter[i].key;
			  else
				  $scope.timePeriodListString += ","+year.quarter[i].key;
				  
		  }
		  if(tp){
			  customReload();
		  }
	  }
	  
	$scope.getTimePeriods = function(periodicity){
		$http.get('cysd/getTimePeriodByPeriodicity?periodicity='+periodicity.value).success(function(data){
			$scope.allTimePeriods = [];
			$scope.allTimePeriods = data;
			$scope.selectedTimePeriod = $scope.allTimePeriods[0];
			tp= $scope.selectedTimePeriod.key;
		});
	}
	
	$scope.selectTimePeriod = function(timePeriod){
		$scope.selectedTimePeriod = timePeriod;
		tp = $scope.selectedTimePeriod.key;
		customReload();
	}
}]);