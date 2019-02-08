var workspaceApp =  angular.module("cmsApp",['ui.tree']);

workspaceApp.controller('CMSController', [ '$scope', '$window', '$http', '$timeout',
		function($scope, $window, $http,  $timeout) {
	
			var w = angular.element($window);
			$scope.getWindowDimensions = function() {
				return {
					"h" : w.height(),
					"w" : (w.width() * 90 / 100)
				};
			};
			// window get resized.
			w.on("resize", function() {
				$scope.$apply();
			});
			
			$scope.categoryList = [];
			$scope.categories = [];
			$scope.selectedCategory ="SDRC";
			$scope.fileList =[];
			$scope.isFilesVisible=false;

			
			 $scope.treeOptions = {
					    accept: function(sourceNodeScope, destNodesScope, destIndex) {
					      return true;
					    },
				        dropped: function(e) {
				            console.log (e.source.nodeScope);
				        }
					  };
			 
			 
			$scope.start = function() {
					$http.get('cysd/getCategories').success(function(data){
						$scope.categoryList = data;
						$scope.categoryData = formatCategoryDetails($scope.categoryList).children;
					});
			}
			$scope.start();
	
			 $timeout(function(){
					 $scope.collapseAll();
			}, 100);
			
			$scope.remove = function (scope) {
		        scope.remove();
		      };

		    $scope.toggle = function (scope) {
		        scope.toggle();
		    };

		    $scope.moveLastToTheBeginning = function () {
		        var a = $scope.data.pop();
		        $scope.data.splice(0, 0, a);
		    };
		      
		    var getRootNodesScope = function() {
		      	return angular.element(document.getElementById("tree-root")).scope();
		    };

		    $scope.collapseAll = function () {
		    	  var scope = getRootNodesScope();
		      	scope.collapseAll();
		    };

		    $scope.expandAll = function () {
		    	  var scope = getRootNodesScope();
		      	scope.expandAll();
		    };
			
	function formatCategoryDetails(array) {
				var map = {}
				for (var i = 0; i < array.length; i++) {
					var obj = array[i]
					if (obj.parentCategoryId == -1)
						obj.parentCategoryId = null;
					if (!(obj.categoryId in map)) {
						map[obj.categoryId] = obj
						map[obj.categoryId].children = []
					}

					if (typeof map[obj.categoryId].categoryName == 'undefined') {
						map[obj.categoryId].categoryId = String(obj.categoryId);
						map[obj.categoryId].categoryName = obj.categoryName
						map[obj.categoryId].type = obj.type
						map[obj.categoryId].parentCategoryId = String(obj.parentCategoryId);
					}

					var parent = obj.parentCategoryId || '-';
					if (!(parent in map)) {
						map[parent] = {}
						map[parent].children = []
					}

					map[parent].children.push(map[obj.categoryId])
				}
				return map['-']
	}
	
	// gets called when the category element is at granular level 
	$scope.selectCategory = function(category){
		$http.get('cysd/getRepoFiles').success(function(data){
			$scope.fileList = data;
			$scope.isFilesVisible = !$scope.isFilesVisible;
		});
	}
			
	$scope.fileDownload = function(fileName){
		var data = {"fileName" :fileName};
		$.download("cysd/downloadFile",data,'POST');
		
//		$http.get('cysd/downloadFile?fileName='+fileName).success(function(data){
//			console.log("success");
//		});
	}
			
			
} ]);