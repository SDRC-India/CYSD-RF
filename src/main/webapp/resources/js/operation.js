var operationApp =  angular.module("operationApp",['angular-bootstrap-modal','ui.bootstrap','commonServiceModule']);


//angular directive for file upload
operationApp.directive('fileModel', ['$parse', function ($parse) {
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
operationApp.service('fileUpload', ['$http', function ($http) {
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