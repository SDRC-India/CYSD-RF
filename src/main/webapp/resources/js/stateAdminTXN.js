var txnApp =  angular.module("txnApp",['ui.bootstrap']);
var tp;
var globaluserId='';
var globalareacode='';
var globaltxnTemplateId='';
var globaltimePeriodId='';
var globalframeworkId='';
function customReload() {
    oTable_adminTemplateTran_params.sAjaxSource = "getAllUserTxnForms?timePeriodId="+tp;
    oTable_adminTemplateTran.fnDestroy();
    oTable_adminTemplateTran.dataTable(oTable_adminTemplateTran_params) ;
}

function downloadTargetFile(data, type, full) {
	return '<div id="div1_'
			+ full.userId
			+ '"> '
			+ '<span id="span1_'
			+ full.index
			+ '">'
			+ full.targetStatus
			+ '</span> </br>'
			+ '<span id="span1_'
			+ full.index
			+ '">'
			+ full.targetLastStatusDate
			+ '</span> </br>'
			+ '<a href="#" onclick="getTargetFile(\''
			+ full.userId
			+ '\',\''
			+ full.userName
			+ '\',\''
			+ full.timePeriodId
			+ '\',\''
			+ full.frameworkId
			+ '\',\''
			+ full.targetFileName
			+ '\',\''
			+ full.frameworkType
			+ '\');" class="btn btn-primary btn-sm" '
			+ full.isTargetAvailable
			+ '><i class="fa fa-lg fa-cloud-download"></i>&nbsp;&nbsp;Download</a>'
			+

			'</div>';
}

function downloadAchiveFile(data, type, full) {
	return '<div id="div2_'
			+ full.userId
			+ '"> '
			+ '<span id="span1_'
			+ full.index
			+ '">'
			+ full.achieveStatus
			+ '</span> </br>'
			+ '<span id="span1_'
			+ full.index
			+ '">'
			+ full.achieveLastStatusDate
			+ '</span> </br>'
			+ '<a href="#" onclick="getFile(\''
			+ full.achieveFileName
			+ '\');" class="btn btn-primary btn-sm" '
			+ full.isAchieveAvailable
			+ '><i class="fa fa-lg fa-cloud-download"></i>&nbsp;&nbsp;Download</a>'
			+

			'</div>';
}

function uploadAchiveFile(data, type, full) {
	
	return '<form id="fileForm_'+full.userId+'"><label class="uploadDandelionWarningLabel noDisplay" id="warningLabel_'+full.userId+
	'">Please upload a valid .xls file</label><div onclick="onUploadBtnClick(\''+full.userId+'\')" id="div3_'+full.userId+'" class="fileUpload btn btn-primary btn-sm"'+ full.isEnableChooseFile+'>'+
	'<span ><i class="fa fa-upload"></i>&nbsp;&nbsp;Upload</span><input id="fileId_'+full.userId+'" name="file_'+full.userId+
	'" type="file" class="upload"  onchange="showModal(\''+full.userId+'\',\''+full.areaCode+'\',\''
	+full.txnTemplateId+'\',\''+full.timePeriodId+'\',\''+full.frameworkId+'\');" '+full.isEnableChooseFile
	+'/></div></form>';
}


function approveFile(data, type, full) {
	 return '<div id="div4_'+full.userId+'"> ' +
	 
	 '<a href="#" id="approve_'+full.userId+'" onclick="approveAchieveFile(\''+full.userId+'\',\''+full.txnTemplateId+'\',\''+full.timePeriodId+'\',\''+full.frameworkId+'\');" class="btn btn-primary btn-sm" '+full.disabled+'><i class="icon-ok"></i>&nbsp;&nbsp;Approve</a>'+

		'</div>';
	 
}
function getFile(file) {
			var fileName = {"fileName" :file};
			$.download("downloadFile",fileName,'POST'); 
}
function getTargetFile(userId, userName, timePeriodId, frameworkId, targetFileName, frameworkType){
	$.ajax({
		url : 'getTargetFile?userName='+userName+'&userId='+userId+'&timePeriodId='+timePeriodId
		+'&frameworkType='+frameworkId+'&targetFileName='+targetFileName+'&frameworkTypeName='+frameworkType,
		type : "POST",
		success : function(result) {
			var fileName = {"fileName" :result};
			$.download("downloadFile",fileName,'POST'); 
		}
	});
}
function approveAchieveFile(userId, txnTemplateId, timePeriodId, frameworkId){
	$.ajax({
		url : 'adminApprove?userId='+userId+'&txnTemplateId='+txnTemplateId+'&timePeriodId='+timePeriodId+'&frameworkId='+frameworkId,
		type : "POST",
		success : function(result) {
			if(result==true){
                          	$( "#alert_placeholder").show();
                      		$( '#alert_placeholder').html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>File aproved successfully!</span></div>');
                      		$( "#alert_placeholder").delay(3000).fadeOut(3000);
				customReload();
			}else{
				$( "#alert_placeholder").show();
          		$( '#alert_placeholder').html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>File aproval failed!</span></div>');
          		$( "#alert_placeholder").delay(3000).fadeOut(3000);
          		customReload();
			}
		}
	});
}

function saveFile(userId, areaCode, txnTemplateId, timePeriodId, frameworkId){
	$.ajax({
		url : 'adminUploadAchieve?userId='+userId+'&txnTemplateId='+txnTemplateId+'&timePeriodId='+timePeriodId+'&frameworkId='+frameworkId,
		type : "POST",
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false,
		data : new FormData(document.getElementById("fileForm_"+userId)),
		success : function(result) {
				if(result.split("_")[1]=="true"){
	                          	$( "#alert_placeholder").show();
	                      		$( '#alert_placeholder').html('<div style="text-align: center;" class="alert alert-success"><a class="close" data-dismiss="alert">×</a><span>File uploaded and aproved successfully!</span></div>');
	                      		$( "#alert_placeholder").delay(3000).fadeOut(3000);
	                      		customReload();
				}else{
     				$( "#alert_placeholder").show();
                 	$('#alert_placeholder').html('<div style="text-align: center;" class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><span>File upload failed ! '+result.split("_")[0]+'</span></div>');
                 	$( "#alert_placeholder").delay(3000).fadeOut(3000);
                 	customReload();
     			}
		}
	});
	
}

function disableButton(status){
	return status == 'Approved' ? 'disabled' : '';
}

function onUploadBtnClick(userId){
	$('#fileId_'+userId).val('');
}

function showModal(userId,areaCode,txnTemplateId,timePeriodId,frameworkId,isEnableChooseFile){
//	console.log($('#fileId_'+userId).val())
	var filePath = $('#fileId_'+userId).val();
	var fileName= filePath.substr(filePath.lastIndexOf("\\")+1);
	var ext = filePath.substr(filePath.lastIndexOf('.')+1)
	if(ext == "xls"){
		$('#warningLabel_'+userId).addClass('noDisplay');
		globaluserId = userId;
		globalareacode = areaCode;
		globaltxnTemplateId=txnTemplateId;
		globaltimePeriodId=timePeriodId;
		globalframeworkId=frameworkId;
		$("#modalLabel").text('Are you sure you want to upload the file '+ fileName+' ?')
		$('#uploadModal').modal({backdrop: 'static', keyboard: false}) ;
		$('#uploadModal').modal('show');
	}else{
		$('#warningLabel_'+userId).removeClass('noDisplay');
	}
}

function hideModal(){
	$('#uploadModal').modal('hide');
}

function onYesClick(){
	hideModal();
	saveFile(globaluserId, globalareacode, globaltxnTemplateId, globaltimePeriodId, globalframeworkId);
}

function onNoClick(){
	hideModal();
	$('#fileId_'+globaluserId).val('');
}
