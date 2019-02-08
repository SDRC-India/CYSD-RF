$( document ).ready(function() {
	$('#newUserDiv').on('shown.bs.collapse', function() {
	    $(".newUser").addClass('fa-minus').removeClass('fa-plus');
	});

	$('#newUserDiv').on('hidden.bs.collapse', function() {
	    $(".newUser").addClass('fa-plus').removeClass('fa-minus');
	});
});

function customReload() {
    oTable_userManagementTran_params.sAjaxSource = "getAllUser";
    oTable_userManagementTran.fnDestroy();
    oTable_userManagementTran.dataTable(oTable_userManagementTran_params) ;
}

function activeBtn(data, type, full) {
	if(full.userId == 17)
		return '<div><button class="btn btn-primary" disabled="disabled">Activated</button></div>';
	if(full.live){
		return '<div><button class="btn btn-danger" onclick="activateDeactivateUser('+full.userId+','+full.live+')">Deactivate</button></div>';
	}else{
		return '<div><button class="btn btn-primary" onclick="activateDeactivateUser('+full.userId+','+full.live+')">Activate</button></div>';
	}
}

function activateDeactivateUser(userid,live){
	$.ajax({
		url : 'activateDeactivateUser?userId='+userid+'&isLive='+!live,
		type : "GET",
		success : function(data) {
			customReload();
			if(data==true){
            	$( "#alert_placeholder").show();
        		$('#alert_placeholder').html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>Activation/Deactivation successful!</span></div>');
        		$( "#alert_placeholder").delay(2000).fadeOut(2000);
            }else{
            	$( "#alert_placeholder").show();
        		$('#alert_placeholder').html('<div style="text-align: center;" class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><span>Activation/Deactivation unsuccessful!!</span></div>');
        		$( "#alert_placeholder").delay(2000).fadeOut(2000);
            }
		}
	});
}

