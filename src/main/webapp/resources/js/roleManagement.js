var url=window.location;
var originalUrl;
var isAttached;
$(document).ready(function(){
	url=window.location;
	isAttached = getQueryVariable("attached");
	originalUrl = url.href.replace(url.search, "");
	if(isAttached){
    	$( "#alert_placeholder").show();
		$('#alert_placeholder').html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>Role attached successfully!</span></div>');
		$( "#alert_placeholder").delay(1000).fadeOut(2000);
		window.history.replaceState('Object', 'Title', originalUrl);
	}
});

function getQueryVariable(variable) {
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    if (pair[0] == variable) {
      return pair[1];
    }
  } 
}

function customReload() {
    oTable_roleManagementTran_params.sAjaxSource = "getAllUserRoleSchemes";
    oTable_roleManagementTran.fnDestroy();
    oTable_roleManagementTran.dataTable(oTable_roleManagementTran_params) ;
}

function attachBtn(data, type, full) {
	if(full.userId == 5)
		return '<div><button class="btn btn-primary" disabled="disabled">Attached</button></div>';
	if(!full.attached){
		return '<div><button class="btn btn-primary" onclick="onAttachClick(\''+full.roleSchemeId+'\',\''+full.schemeName+'\')">Attach</button></div>';
	}else{
		return '<div><button class="btn btn-danger" onclick="onDetachClick(\''+full.userId+'\')">Detach</button></div>';
	}
}

function onAttachClick(roleSchemeId,schemeName){
	location.href = "roleMaster?roleSchemeId="+roleSchemeId+"&schemeName="+schemeName
}

function onDetachClick(userId){
	$.ajax({
		url : 'detachRole?userId='+userId,
		type : "GET",
		success : function(data) {
			customReload();
			if(data==true){
            	$( "#alert_placeholder").show();
        		$('#alert_placeholder').html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>Role detachment successful!</span></div>');
        		$( "#alert_placeholder").delay(2000).fadeOut(2000);
            }else{
            	$( "#alert_placeholder").show();
        		$('#alert_placeholder').html('<div style="text-align: center;" class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><span>Role detachment unsuccessful!</span></div>');
        		$( "#alert_placeholder").delay(2000).fadeOut(2000);
            }
		}
	});
}
