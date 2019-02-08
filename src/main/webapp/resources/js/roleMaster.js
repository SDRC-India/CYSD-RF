var roleSchemeId='';
var roleSchemeName='';

$(document).ready(function(){
	roleSchemeId = getQueryVariable("roleSchemeId");
	roleSchemeName = getQueryVariable("schemeName");
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
    oTable_userRoleTable_params.sAjaxSource = "getUsersOfNoRole";
    oTable_userRoleTable.fnDestroy();
    oTable_userRoleTable.dataTable(oTable_userRoleTable_params) ;
}

function assignRoleBtn(data, type, full) {
	return '<div><button class="btn btn-primary" onclick="assignRole('+full.userId+')">Assign Role</button></div>';
}

function assignRole(userid){
	$.ajax({
		url : 'attachRole?userId='+userid+'&roleFeaturePermissionSchemeId='+roleSchemeId
		+'&schemeName='+roleSchemeName,
		type : "POST",
		success : function(data) {
			if(data==true){
				location.href = "roleManagement?attached="+true;
            }else{
            	$( "#alert_placeholder").show();
        		$('#alert_placeholder').html('<div style="text-align: center;" class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><span>Role attachment failed!</span></div>');
        		$( "#alert_placeholder").delay(2000).fadeOut(2000);
            }
		}
	});
}
