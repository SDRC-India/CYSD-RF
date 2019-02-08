var commonServiceModule=angular.module('commonServiceModule',[])

var commonService = commonServiceModule.service('cService', ['$rootScope','$http',
		function($rootScope,$http) {
			this.getProject = function() {
				if($rootScope.view == 'workspace'){
					var originalData = []
					$http.get('getAllProjects?frameworkType=0').success(function(data){
						for(var i=0;i<data.length;i++){
							indData='';
							var str = data[i].value.split("?");
							indData = {key:data[i].key,
									value:str[0]
							};
							originalData.push(indData);
						}
						for(var i=0;i<originalData.length;i++){
							if(originalData[i].value=='MIS_CYSD'){
								originalData.splice(i, 1);
							}
						}
						$rootScope.$broadcast('projectDataPopulated', originalData);
					});
				}else if($rootScope.view == 'dashboard'){
					
				
					$http.get('cysd/getAllProjects?frameworkType=0').success(function(data){
							var districtProject=[];
							var stateProject=[];
							var indData;
							for(var i=0;i<data.length;i++){
								indData='';
								var str = data[i].value.split("?");
								indData = {key:data[i].key,
										value:str[0]
								};
								if(str[1]!="PS"){
									districtProject.push(indData)
								}
								stateProject.push(indData);
								
							}
							if($rootScope.selectedView=='district')
								$rootScope.$broadcast('projectDataPopulated', districtProject);
							else if($rootScope.selectedView=='state')
								$rootScope.$broadcast('projectDataPopulated', stateProject);
					});
					
					$http.get('cysd/getAllProjects?frameworkType=1').success(function(data){
						var districtProject=[];
						var stateProject=[];
						var indData;
						for(var i=0;i<data.length;i++){
							indData='';
							var str = data[i].value.split("?");
							indData = {key:data[i].key,
									value:str[0]
							};
							if(str[1]!="PS"){
								districtProject.push(indData)
							}
							stateProject.push(indData);
							
						}
						$rootScope.$broadcast('operationProjectDataPopulated', stateProject);
					});
				}
				
				}
		}

]);