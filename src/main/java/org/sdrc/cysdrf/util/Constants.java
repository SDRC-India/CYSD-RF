package org.sdrc.cysdrf.util;

import org.springframework.stereotype.Component;

@Component
public class Constants {
      public static final  String USER_PRINCIPAL="UserPrincipal"; 
      public static final  String REFERER ="referer"; 
      public static final  String REDIRECT ="redirect:";
      public static final  String USER_PROFILE_OBJECT="userProfileServiceImpl";
      public static final  String ERROR_LIST = "errorList";
      public static final  String LOGIN_META_ID = "loginMetaId";
      
      public static final  String ACCESS_DENIED="accessDenied";
      public static final  String SMTP_HOST_KEY ="smtp.host.key";
      public static final  String SOCKETFACTORY_PORT_KEY ="socketFactory.port.key";
      public static final  String SOCKETFACTORY_CLASS_KEY ="socketFactory.class.key";
      public static final  String SMTP_AUTH_KEY ="smtp.auth.key";
      public static final  String SMTP_PORT_KEY ="smtp.port.key";
      
      public static final  String SMTP_HOST ="smtp.host";
      public static final  String SOCKETFACTORY_PORT ="socketFactory.port";
      public static final  String SOCKETFACTORY_CLASS ="socketFactory.class";
      public static final  String SMTP_AUTH ="smtp.auth";
      public static final  String SMTP_PORT ="smtp.port";
      public static final  String AUTHENTICATION_USERID ="authentication.userid";
      public static final  String AUTHENTICATION_PASSWORD ="authentication.password";
      public static final  String MESSAGE_SETFORM ="message.setFrom";
    
      //CONATACT US
	  public static final String CONTACT_US_MAIL_SEND_FAIL = "contact.us.mail.send.fail";
	  public static final String CONTACT_US_MAIL_SEND_SUCCESS = "contact.us.mail.send.success";
	  public static final String CONTACT_US_MAIL_SUBJECT = "contact.us.mail.subject";
	  public static final String CONTACT_US_MAIL_TOMAILID = "contact.us.mail.tomailid";
	  public static final String CONTACT_US_MAIL_TOUSERNAME = "contact.us.mail.tousername";
	  public static final String CONTACT_US_MAIL_SENDER_MAILID = "contact.us.mail.sender.mailid";
	  
	  //status
	  public static final String TEMPLATE_SUBMISSION_STATUS = "Submitted";
	  public static final String TEMPLATE_APPROVAL_STATUS = "Approved";
	  public static final String TEMPLATE_NOT_SUBMITTED_STATUS = "Not Submitted";
	  
	  //excel path
	  public static final String EXCEL_READ_PATH = "excel.read.filepath";
	  public static final String EXCEL_STORE_PATH = "excel.store.filepath";
	  public static final String EXCEL_CREATE_PROJECT_SHEET = "excel.create.project.sheet";
	  public static final String EXCEL_UNIQUE_KEY_SHEET = "excel.unique.key.sheet";
	  public static final String FACTSHEET_EXCEL_READ_FILEPATH = "factsheet.excel.read.filepath";
	  public static final String EXCEL_UPLOAD_FILEPATH = "excel.upload.filepath";
	  public static final String OPERATIONAL_EXCEL_READ_PATH = "operational.excel.read.filepath";
	  public static final String FACTSHEET_OPERATIONAL_EXCEL_READ_FILEPATH = "factsheet.operational.excel.read.filepath";
	  
	  //Delimeter
	  public static final String DELIMETR_COMMA="delimeter.comma";
	  
	  //upload type
	  public static final String UPLOAD_TYPE_PLANNED = "upload.type.planned";
	  public static final String UPLOAD_TYPE_TARGET = "upload.type.target";
	  public static final String UPLOAD_TYPE_ACHIEVED = "upload.type.achieved";
	  
	  //disable classes
	  public static final String DISABLE_NONE = "blank";
	  public static final String DISABLE_DISABLED = "disabled";
	  
	  //project types
	  public static final String STATE_PROJECT = "PS";
	  public static final String DISTRICT_PROJECT = "PR";
	  
	  //factsheet path
	  public static final String FACTSHEET_FILEPATH="factsheet.filepath";
	  
	  //framework type
	  public static final String FRAMEWORK_TYPE_PROGRAMME = "Programme";
	  public static final String FRAMEWORK_TYPE_OPERATION = "Operations";
} 
