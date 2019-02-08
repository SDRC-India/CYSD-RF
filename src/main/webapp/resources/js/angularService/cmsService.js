app.service("GetAllSection", function($http,$q){
	this.getAll = function(){
		var deferred = $q.defer();
		$http.get("../getAllSection")
		.then(function(response){
			var allsections = response.data;
			deferred.resolve(allsections);
		});
		return deferred.promise;
	};
});

app.service("GetAllReport", function($http, $q){
	this.getAll = function(){
		var deferred = $q.defer();
		$http.get("../getAllReports")
		.then(function(response){
			var allReports = response.data;
			deferred.resolve(allReports);
		});
		return deferred.promise;
	};
})
app.service("GetAllTags", function($http, $q){
	this.getAll = function(){
		var deferred = $q.defer();
		$http.get("../getAllTags")
		.then(function(response){
			var allReports = response.data;
			deferred.resolve(allReports);
		});
		return deferred.promise;
	};
})
app.service("GetAllReportType", function($http, $q){
	this.getAll = function(){
		var deferred = $q.defer();
		$http.get("../getAllReportType")
		.then(function(response){
			var allReports = response.data;
			deferred.resolve(allReports);
		});
		return deferred.promise;
	};
});

app.service("GetAllTags", function($http, $q){
	this.getAll = function(){
		var deferred = $q.defer();
		$http.get("../getAllTags")
		.then(function(response){
			var allReports = response.data;
			deferred.resolve(allReports);
		});
		return deferred.promise;
	};
});
app.factory("CategoriesService", function CategoriesService($q, $timeout) {
  	
    var that = {
        find: find
      };
      
   
    function find(search, selectedCategories, categories) {
    	var deferred = $q.defer(),
        result = categories.filter(function(category) {
          return (-1 === selectedCategories.indexOf(category) && -1 !== category.name.indexOf(search));
        });
        
      $timeout(function() {
      	deferred.resolve(result);
      }, 100);
      
     	return deferred.promise;
    }
    
    return that;
  });