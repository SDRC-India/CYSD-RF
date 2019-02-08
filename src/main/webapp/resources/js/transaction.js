
function customReload() {
    oTable_templateTran_params.sAjaxSource = "getUserTXNFiles";
    oTable_templateTran.fnDestroy();
    oTable_templateTran.dataTable(oTable_templateTran_params) ;
}

function downloadTargetFile(data, type, full) {
	 return '<div id="div1_'+full.index+'"> ' 
		+ '<span id="span1_'+full.index+'">'+full.targetStatus+'</span> </br>'
		+ '<span id="span1_'+full.index+'">'+full.targetLastStatusDate+'</span> </br>'
		+'<a href="#" onclick="getFile(\''+full.targetFileName+'\');" class="btn btn-primary btn-sm margin-top-10" '+full.isTargetAvailable+'><i class="fa fa-lg fa-cloud-download"></i>&nbsp;&nbsp;Download</a>'+
		'</div>';
}

function downloadAchiveFile(data, type, full) {
		 return '<div id="div1_'+full.index+'"> '
		 + '<span id="span1_'+full.index+'">'+full.achieveStatus+'</span> </br>'
		 + '<span id="span1_'+full.index+'">'+full.achieveLastStatusDate+'</span> </br>'
		 +'<a href="#" onclick="getFile(\''+full.achieveFileName+'\');" class="btn btn-primary btn-sm margin-top-10" '+full.isAchieveAvailable+'><i class="fa fa-lg fa-cloud-download"></i>&nbsp;&nbsp;Download</a>'+

			'</div>';
}

function getFile(file) {
			var fileName = {"fileName" :file};
			$.download("downloadFile",fileName,'POST'); 
}