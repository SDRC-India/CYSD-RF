
var timePeriod = [];
function getPeriodicity(){
	$.ajax({
		url : '/CYSDRF/getAllTimePeriod',
		type : "GET",
		success : function(data) {
			getFrameworks();
		var year = Object.keys(data);
		timePeriod=[];
		for(var i=0;i<year.length;i++){
			var quarter=[];
			var quarterLength = Object.keys(data[Object.keys(data)[i]]).length;
			var quarterKeys = Object.keys(data[Object.keys(data)[i]]);
			for(var j=0;j<quarterLength;j++){
				quarter.push({
					key:quarterKeys[j],
					value:data[Object.keys(data)[i]][quarterKeys[j]]
				});
			}
			var time = {
					value:year[i],
					quarter:quarter
			};
			timePeriod.push(time);
		}
		$('#timeperiodID').empty();
		var ul = "";
		ul = document.getElementById("timeperiodID");
		for ( var i = 0; i < timePeriod.length; i++) {
			
			tp = timePeriod[i];
			var li =document.createElement('li');
			li.setAttribute("id", "liId"+i);
			li.setAttribute("class", "pointorCursor");
			
			(function(val){
				li.addEventListener("click", function() {
			           getQuarter(val);
			        }, false);})(tp);
			
			var a = document.createElement("a");
	        a.innerHTML = timePeriod[i].value;
	        li.appendChild(a);
	        ul.appendChild(li);
		}
		getQuarter(timePeriod[0]);
		}
	});
}

function getQuarter(selectedTimePeriod){
		var htmlNew = '';
		var id = "#quarterId";
		for ( var i = 0; i < selectedTimePeriod.quarter.length; i++) {
			htmlNew += '<li class="pointorCursor" onclick="displayValue(\''+selectedTimePeriod.quarter[i].value+'\',\''+id+'\',\''+selectedTimePeriod.quarter[i].key+'\')" value="' + selectedTimePeriod.quarter[i].key + '"><a>'
					+ selectedTimePeriod.quarter[i].value + '</a></li>';
		}
		
		$('#quarterID').html(htmlNew);
		displayValue(selectedTimePeriod.value,"#periodicityId",selectedTimePeriod.quarter[0].key);
		$("#quarterId").val(selectedTimePeriod.quarter[0].value);
}

function displayValue(val,id,key) {
	$(id).val(val);
	$('#selectedQuarter').val(key);
}

function downloadExcel(){
	$('#myModal').modal('hide');				 
	$('#loaderModal').modal('show');
	var timePeriodVal=$("#quarterId" ).val();
	var quarter=$("#selectedQuarter").val();
	var frameworkType=$("#frameworkInput").val();
	$.ajax({
		url:'/CYSDRF/getFactsheet?quarter='+quarter+'&timePeriodVal='+timePeriodVal+'&frameworkType='+frameworkType,
		type:"GET",
			success : function(data) {
				
				var fileName = {"fileName" :data};
				$.download("/CYSDRF/cysd/downloadFile",fileName,'POST'); 
				 $('#loaderModal').modal('hide');
			}
		
	});
}


function getFrameworks() {
	var fr = "";
	$.ajax({
				url : '/CYSDRF/cysd/getAllFactsheetFramework',
				type : "GET",
				success : function(data) {
					fr = data;
					var htmlNew = '';
					var id = "#frameworkInput";
					for (var i = 0; i < fr.length; i++) {
						htmlNew += '<li class="pointorCursor" onclick="frameWorkdisplayValue(\''
								+ fr[i].value
								+ '\',\''
								+ id
								+ '\',\''
								+ fr[i].key
								+ '\')" value="'
								+ fr[i].key
								+ '"><a>' + fr[i].value + '</a></li>';
					}
					$('#frameworkId').html(htmlNew);
					$("#frameworkInput").val(fr[0].value);
				}
			});
}

function frameWorkdisplayValue(val,id,key) {
	$(id).val(val);
}

//download a file
$.download = function(url, data, method){
	//url and data options required
	if( url && data ){ 
		//data can be string of parameters or array/object
		data = typeof data == 'string' ? data : jQuery.param(data);
		//split params into form inputs
		var inputs = '';
		jQuery.each(data.split('&'), function(){ 
			var pair = this.split('=');
			inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />'; 
		});
		//send request
		jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
		.appendTo('body').submit().remove();
	};
};
