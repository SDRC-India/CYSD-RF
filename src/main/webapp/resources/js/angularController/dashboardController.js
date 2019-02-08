dashboardApp.controller('DashboardController',['$scope','$window','$http','$timeout','cService','$rootScope',
                                               function($scope,$window,$http,$timeout,cService,$rootScope){
	//$scope.primary_url = mapUrl;
	
	$scope.shouldDrilldown = true;
	$scope.showMap = true;
	$scope.timePeriodList = [];
	$scope.operationTimePeriodList = [];
	$scope.subSectorList = [];
	$scope.sectors=[];
	$scope.operationSectors=[];
	$scope.selectedSector = '';
	$scope.selectedOperationSector = '';
	$scope.gridValue='';
	$scope.selectedAreaName='';
	$scope.selectedGrid='';
	$scope.gridSubSectorList=[];
	$scope.indicatorList=[];
	$scope.allProjects=[];
	$scope.allOperationProjects=[];
	$scope.areaId='';
	$scope.isTrendVisible= false ;
	$scope.quarterList=[];
	$scope.operationQuarterList=[];
	$scope.selectedOperationTimePeriod='';
	$scope.selectedYear='';
	$scope.selectedOperationYear='';
	$scope.timePeriodListString='';
	$scope.operationTimePeriodListString='';
	$scope.selectedProject='';
	$scope.selectedOperationProject='';
	$scope.showLoaderModal=true;
	$scope.showNoDataModal=false;
	$scope.showNoProjectDataModal=false;
	$rootScope.view = 'dashboard';
	$scope.mapData = [];
	$scope.chloroplethData = [];
	$scope.operationSubsectorGridData = [];
	$scope.operationIndicatorList = [];
	$scope.selectedOperationSubsectorGrid = '';
	$scope.selectedOperationIndicator = '';
	$scope.unit = '';
	$rootScope.selectedView = 'district';
	$scope.selectedTab = 'Program';
	$scope.primary_url = 'resources/geomaps/odisha_map.json';
	$scope.operationView = 'Sector';
//	$scope.start = function(){
//		$scope.showGrid();
//	}
	
//	function showLoaderModal(){
//		$timeout(function() {
//			$scope.showLoaderModal=true;
//	    },500);
//	}
	
	
	$scope.showGrid = function(){
		for(var i=0;i<$scope.gridSubSectorList.length;i++){
			if(i==0 || i == $scope.gridSubSectorList.length-1){
				$scope.gridSubSectorList[i].outerClass ="margin-top-10 col-md-12 col-lg-12 col-sm-12 col-xs-12"
			}else {
				$scope.gridSubSectorList[i].outerClass ="margin-top-10 col-md-6 col-lg-6 col-sm-6 col-xs-6"
			}
		}
	}
	
	$scope.marginLeft = 0;
	
	$scope.myStyle = {
	    'float':'left',
	    'margin-left': '0px',
	    'display':'block'
	 };
	
	$scope.hideMap = function(d){
		$scope.$apply(function(){
            $scope.showMap = false;
        });
		$scope.areaId = d.properties.ID_;
		//Unit value =Percent & Subgroup value=Total is hard coded Here 
		if($rootScope.selectedView == 'district')
			getStateProjectsOnDoubleClick();
		else{
			$scope.gridSubSectorList=[];
			getIndicator();
		}
	};
	
	$scope.displayMap = function(){
		$scope.mapSetup($scope.primary_url, function() {
			getChloroplethData();
		});
		$scope.showMap = true;
	};
	
	var w = angular.element($window);
	$scope.getWindowDimensions = function() {
		return {
			"h" : w.height(),
			"w" : (w.width() * 90 / 100)
		};
	};
	// this is to make sure that scope gets changes as window get resized.
	w.on("resize", function() {
		$scope.$apply();
	});
	
	$scope.showViz = function(areacode) {
		if (areacode && $scope.selectedArea != areacode) {
			$scope.isTrendVisible = true;
			$scope.selectedArea = areacode;
		} else {

			$scope.isTrendVisible = false;
			$scope.selectedArea = [];
		}
		$scope.$apply();
	};
	
	$scope.closeViz = function() {
		$scope.isTrendVisible = false;
		$scope.selectedGrid = '';
	};
	
	
	
	$scope.selectMapAreaType = function() {
		$scope.closeViz();
//		$(".backbtn").toggleClass("hidden", true);
//		$(".otherBtnDiv").toggleClass("hidden", true);
		$scope.shouldDrilldown = true;
		$scope.displayMap();
		$scope.selectedAreaName='';
		if($rootScope.selectedView == 'district')
			cService.getProject();
	};
	
	  
   if ($scope.marginLeft === 0) {
      $scope.leftdisabled = true;
    }
    else{
      $scope.leftdisabled = false;
    }
    
     if ($scope.marginLeft === -(($scope.sectors.length-5)*100)) {
      $scope.rightdisabled = true;
    }
    else{
      $scope.rightdisabled = false;
    }
     
    $scope.start = function(){
    	
    	$http.get('getAllSector?frameworkType=0').then(function(data){
    		$scope.sectors=data.data;
    		$scope.selectedSector = $scope.sectors[0];
    		// get subsector list
    		$http.get('getAllSubsector?sectorNid='+$scope.selectedSector.key).success(function(data){
    			$scope.subSectorList=data;
    			$scope.selectedSubSector=$scope.subSectorList[0];
    			$http.get('getAllTimePeriod').success(function(data){
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
    	    		$scope.operationTimePeriodList=timePeriod;
    	    		$scope.selectedYear = $scope.timePeriodList[0];
    	    		$scope.selectYear($scope.selectedYear);
    	    		
    	    		cService.getProject();
    	    	});
    			$scope.showLoaderModal=false;
    		});
    	});
    	
    	
    	$http.get('getAllSector?frameworkType=1').then(function(data){
    		$scope.operationSectors=data.data;
    		$scope.selectedOperationSector = $scope.operationSectors[0];
    		$http.get('getAllTimePeriod').success(function(data){
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
	    		$scope.operationTimePeriodList=timePeriod;
	    		$scope.selectedOperationYear = $scope.operationTimePeriodList[0];
	    		$scope.selectOperationYear($scope.selectedOperationYear);
	    		
	    	});
    	});
    };
    
    function getOperationSubsectorGridData(){
    	 $scope.showLoaderModal=true;
    	$http.get('getSubSectorAsGrid?sectorId='+$scope.selectedOperationSector.key+
    			'&timeperiodNid='+$scope.selectedOperationTimePeriod.key+'&projectId='+$scope.selectedOperationProject.key).success(function(data){
    		$scope.operationSubsectorGridData = data;
    		$scope.showOperationSubsectorGrid();
    		 $scope.showLoaderModal=false;
    	});
    }
    
    $scope.showOperationSubsectorGrid = function(){
		for(var i=0;i<$scope.operationSubsectorGridData.length;i++){
			if(i==0 || i == $scope.operationSubsectorGridData.length-1){
				$scope.operationSubsectorGridData[i].outerClass ="margin-top-10 col-md-12 col-lg-12 col-sm-12 col-xs-12 pointorCursor"
			}else {
				$scope.operationSubsectorGridData[i].outerClass ="margin-top-10 col-md-6 col-lg-6 col-sm-6 col-xs-6 pointorCursor"
			}
		}
	}
    
    $scope.showOperationIndicatorGrid = function(){
    	for(var i=0;i<$scope.operationIndicatorList.length;i++){
    		if(i==0 || i == $scope.operationIndicatorList.length-1){
    			$scope.operationIndicatorList[i].outerClass ="margin-top-10 col-md-12 col-lg-12 col-sm-12 col-xs-12 pointorCursor"
    		}else {
    			$scope.operationIndicatorList[i].outerClass ="margin-top-10 col-md-6 col-lg-6 col-sm-6 col-xs-6 pointorCursor"
    		}
    	}
    }
    
    $scope.showOperationIndicatorGridView = function(grid){
    	 $scope.showLoaderModal=true;
    	$scope.selectedOperationSubsectorGrid = grid;
		$http.post("getAllOperationIndicator?unit=2&subgroup=3&areaid=IND021&key="+
				$scope.selectedOperationSubsectorGrid.indicatroNid+"&timePeriod="+$scope.selectedOperationTimePeriod.key+
				"&projectId="+$scope.selectedOperationProject.key).success(function(data){
					 $scope.showLoaderModal=false;
					if(grid.dataValue != null){
						$scope.operationView = 'Sub-sector';
						$scope.operationIndicatorList = data;
						$scope.showOperationIndicatorGrid();
					}else{
						$scope.showNoDataModal = true;
					}
		})
	}
	

	  $scope.moveRight = function() {
		var w= $scope.getWindowDimensions();
	    if(!$scope.rightdisabled){
	    	$scope.marginLeft = +$scope.myStyle['margin-left'].replace('px', '') + -100;
		    
		    if ($scope.marginLeft === 0) {
		      $scope.leftdisabled = true;
		    }
		    else{
		      $scope.leftdisabled = false;
		    }
		    
		     if ($scope.marginLeft < -200) {
		      $scope.rightdisabled = true;
		    }
		    else{
		      $scope.rightdisabled = false;
		    }
	
	    }
	    $scope['margin-left'] = $scope.marginLeft + 'px';
	    return $scope['margin-left'];
	  }
	  
	  $scope.moveLeft = function() {
	    if(!$scope.leftdisabled){
	    	$scope.marginLeft = +$scope.myStyle['margin-left'].replace('px', '') + 100;
		    
	 	   if ($scope.marginLeft === 0) {
	 	      $scope.leftdisabled = true;
	 	    }
	 	    else{
	 	      $scope.leftdisabled = false;
	 	    }
	 	    
	 	     if ($scope.marginLeft === -200) {
	 	      $scope.rightdisabled = true;
	 	    }
	 	    else{
	 	      $scope.rightdisabled = false;
	 	    }

	    }
	    $scope['margin-left'] = $scope.marginLeft + 'px';
	    return $scope['margin-left'];
	  }
	  
//	  $timeout($scope.start(), 100);
	  
	  $scope.selectSector=function(sector){
		  $scope.showLoaderModal=true;
		  $scope.subSectorList = [];
		  $scope.selectedSubSector = '';
		  $scope.selectedSector = sector;
		  $scope.isTrendVisible= false ;
		  $http.get('getAllSubsector?sectorNid='+$scope.selectedSector.key).success(function(data){
				$scope.subSectorList=data;
				$scope.selectedSubSector=$scope.subSectorList[0];
				
				if(!$scope.showMap)
					getIndicator();
				else{
					$scope.mapSetup($scope.primary_url, function() {
						getChloroplethData();
					});
				}
		  });
		  
	  };
	  
	  $scope.selectOperationSector=function(sector){
		  //$scope.showLoaderModal=true;
		  $scope.operationView='Sector'
		  $scope.selectedOperationSector = sector;
		  $scope.selectedOperationSubsectorGrid = '';
		  $scope.isTrendVisible= false ;
		  getOperationSubsectorGridData();
		  
	  };
	  
	  $scope.selectTimePeriod=function(tp){
		  $scope.showLoaderModal=true;
		  $scope.selectedTimePeriod = tp;
		  $scope.gridSubSectorList=[];
		  $scope.isTrendVisible= false ;
		  if(!$scope.showMap)
			  getIndicator();
		  else{
				$scope.mapSetup($scope.primary_url, function() {
					getChloroplethData();
				});
		  }
	  };
	  
	  $scope.selectOperationTimePeriod=function(tp){
		  $scope.selectedOperationTimePeriod = tp;
		  getOperationSubsectorGridData();
		  $scope.operationView='Sector';
		
	  };
	  
	  function getIndicator(){ 
		  $scope.gridSubSectorList = [];
		  var flag = 0;
		  $http.post("getAllIndicator?unit=2&subgroup=3&areaid="+$scope.areaId+
					"&key="+$scope.selectedSubSector.key+"&timePeriod="+$scope.selectedTimePeriod.key+"&projectId="+$scope.selectedProject.key).success(function(data){
						$scope.gridSubSectorList = data;
						for(var i=0;i<data.length;i++){
							if(data[i].dataValue !=null){
								flag++;
							}
						}
						if(flag == 0)
							$scope.showNoProjectDataModal = true;
						$scope.showGrid();
						$scope.showLoaderModal=false;
			})
	  }
	  
	  function getStateProjectsOnDoubleClick(){
		  $scope.allProjects = [];
		  //$scope.selectedProject ='';
		  $scope.gridSubSectorList=[];
		  $http.post("getAreaSpecificProjects?areaCode="+$scope.areaId).success(function(data){
			  $scope.allProjects = data;
			  console.log(data);
			 // $scope.selectedProject = $scope.allProjects[0];
			  getIndicator();
		  });
	  }
	  
	  $scope.selectSubSector=function(subSector){
		  $scope.showLoaderModal=true;
		  $scope.selectedSubSector = subSector;
		  $scope.gridSubSectorList=[];
		  $scope.isTrendVisible= false ;
		  if(!$scope.showMap)
			  getIndicator();
		  else{
				$scope.mapSetup($scope.primary_url, function() {
					getChloroplethData();
				});
		  }
	  };
	  
	  $scope.showTrendChart = function(grid){
		  var indicatorData = [];
		  var str1 = [];
		  var str2=[];
		  $scope.selectedGrid = grid;
		  $http.post("getTrendValue?subgroup=1,2&key="+$scope.selectedSubSector.key+"&timePeriod="
				  +$scope.timePeriodListString+"&projectId="+$scope.selectedProject.key+
					"&indicator="+$scope.selectedGrid.indicatroNid).success(function(data){
			  $scope.indicatorList = [];
			  $scope.unit = data.unit;
			  if(data.plannedValue !="" ){
				  indicatorData = splitData(data)
				  for(var i=0;i<indicatorData.length;i++){
					 str1 = indicatorData[i].time.split('-')[0].split('.')[1]=="01"?'Q4':
						 indicatorData[i].time.split('-')[0].split('.')[1]=="04"?'Q1':
							 indicatorData[i].time.split('-')[0].split('.')[1]=="07"?'Q2':'Q3';
					 
					 indicatorData[i]['timeText']=str1;
				  }
				  
				  $scope.indicatorList = indicatorData;
				  $scope.isTrendVisible=true;
			  }else{
				  $scope.isTrendVisible=false;
				  $scope.showNoDataModal =  true;
			  }
		  });
	  };
	  
	  $scope.selectYear = function(year){
		  $scope.showLoaderModal=true;
		  $scope.isTrendVisible= false ;
		  $scope.timePeriodListString='';
		  $scope.quarterList=[];
		  $scope.selectedTimePeriod ='';
		  $scope.selectedYear = year;
		  $scope.quarterList = year.quarter;
		  $scope.selectedTimePeriod = $scope.quarterList[0];
		  if(!$scope.showMap){
			  if($scope.areaId!='')
				  getIndicator();
		  }else{
			  if($scope.allProjects.length ==0){
				  $scope.mapSetup($scope.primary_url, function() {
					  getChloroplethData();
				  });
				  
			  }
		  }
		  
		  if($scope.selectedProject !=null){
			  $scope.mapSetup($scope.primary_url, function() {
				  getChloroplethData();
			  });
		  }
		  
		  for(i=0;i<year.quarter.length;i++){
			  if(i==0)
				  $scope.timePeriodListString += year.quarter[i].key;
			  else
				  $scope.timePeriodListString += ","+year.quarter[i].key;
				  
		  }
	  }
	  
	  $scope.selectOperationYear = function(year){
		  $scope.selectedOperationYear = year;
		  $scope.operationQuarterList = year.quarter;
		  $scope.selectedOperationTimePeriod =  $scope.operationQuarterList[0];
		  getOperationSubsectorGridData();
		  $scope.operationView='Sector';
	  }
	  
	  function splitData(data){
		 var finalArray=new Array();
		 var plannedArray = data.plannedValue.split(',');//planned
		 var achievedArray=data.achievedValue.split(',');//Achieved
		 
		 for(i=0;i<plannedArray.length;i++){
			obj = createTrendObject(i,plannedArray,'Planned');
			finalArray.push(obj);
			if(achievedArray[i]!=undefined && achievedArray[i]!=""){
				obj = createTrendObject(i,achievedArray,'Achieved');
				finalArray.push(obj);
			}
		 }
		 return finalArray;
	  }
	  
	  function createTrendObject(i,arr,source){
		  var obj={};
		  obj={	time:arr[i].split('=')[0],
				value:arr[i].split('=')[1],
				source:source
			}
		  return obj;
	  }
	  
	  $scope.selectProject = function(project){
		  $scope.showLoaderModal=true;
		  $scope.selectedProject = project;
		  if(!$scope.showMap)
			  getIndicator();
		  else{
				$scope.mapSetup($scope.primary_url, function() {
					getChloroplethData();
				});
		  }
		  
	  }
	  
	  $scope.selectOperationProject = function(project){
		  //$scope.showLoaderModal=true;
		  $scope.selectedOperationProject = project;
	  }
	  
	  $rootScope.$on('projectDataPopulated', function(event, data) {
			$scope.allProjects = data
			if($scope.selectedProject == null ||$scope.selectedProject=='')
				$scope.selectedProject = $scope.allProjects[0];
			$scope.mapSetup($scope.primary_url, function() {
				getChloroplethData();
			});
	  });
	  
	  $rootScope.$on('operationProjectDataPopulated', function(event, data) {
		  $scope.allOperationProjects = data;
		  if($scope.selectedOperationProject == null ||$scope.selectedOperationProject=='')
			  $scope.selectedOperationProject = $scope.allOperationProjects[0];
		  
		  getOperationSubsectorGridData();
		 
	  });
	  
	  function getChloroplethData(){
		 if($scope.selectedProject.key != undefined){
			 $http.post("getCholropathData?projectId="+$scope.selectedProject.key+"&timePeriodId="+
					 $scope.selectedTimePeriod.key+"&subSectorKey="+$scope.selectedSubSector.key).
					 success(function(data){
						 $scope.mapData = data;
						 $scope.chloroplethData = data;
						 if(data==[] || data.length==0)
							 $scope.showNoProjectDataModal = true;
						 $scope.showLoaderModal=false;
					 });
		 }
	  }
	  
	  $scope.showView = function(view){
		  $scope.showLoaderModal=true;
		  $rootScope.selectedView = view;
		 if(view=="state"){
			 $scope.primary_url = 'resources/geomaps/India.json';
		 }else if(view=="district"){
			 $scope.primary_url = 'resources/geomaps/odisha_map.json';
		 }
		 cService.getProject();
	  };
	  
	  $scope.selectTab = function(val){
		$scope.isTrendVisible=false;
		$scope.selectedTab=val;
	  };
	  
	  $scope.showOperationTrendChartView = function(grid){
		  $scope.showLoaderModal=true;
		  $scope.selectedOperationIndicator = grid;
		  $http.post("getTrendValue?subgroup=1,2&key="+$scope.selectedOperationSubsectorGrid.indicatroNid+"&timePeriod="
				  +$scope.selectedOperationTimePeriod.key+"&projectId="+$scope.selectedOperationProject.key+
					"&indicator="+$scope.selectedOperationIndicator.indicatroNid).success(function(data){
					 $scope.showLoaderModal=false;
					$scope.indicatorList = [];
					  $scope.unit = data.unit;
					  if(data.plannedValue !="" ){
						  indicatorData = splitData(data)
						  for(var i=0;i<indicatorData.length;i++){
							 str1 = indicatorData[i].time.split('-')[0].split('.')[1]=="01"?'Q4':
								 indicatorData[i].time.split('-')[0].split('.')[1]=="04"?'Q1':
									 indicatorData[i].time.split('-')[0].split('.')[1]=="07"?'Q2':'Q3';
							 
							 indicatorData[i]['timeText']=str1;
						  }
						  
						  $scope.indicatorList = indicatorData;
						  $scope.isTrendVisible=true;
					  }else{
						  $scope.isTrendVisible=false;
						  $scope.showNoDataModal =  true;
					  }
						
		 });
	  };
	  
	  
	  $scope.start();
	  
}]);