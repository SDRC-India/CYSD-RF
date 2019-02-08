<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.ResourceBundle"%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CYSD</title>
<%
	ResourceBundle resource = ResourceBundle.getBundle("spring/app");
%>
<spring:url value="/webjars/bootstrap/3.1.1/css/bootstrap.min.css"
	var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />

<spring:url value="/webjars/font-awesome/4.0.3/css/font-awesome.min.css"
	var="fontawesomeCss" />
<link href="${fontawesomeCss}" rel="stylesheet" />

<spring:url value="/resources/css/jquery.multiselect.css"
	var="multiselectcss" />
<link href="${multiselectcss}" rel="stylesheet" />

<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>

<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
	var="bootstrapjs" />
	
<script src="${bootstrapjs}"></script>

<spring:url value="/resources/css/angular-tooltips.css"
	var="tooltipcss" />
<link href="${tooltipcss}" rel="stylesheet" />

<spring:url value="/resources/images/favicon.ico"
	var="favicon" />
<link rel="icon"   type="image/png"   href="${favicon}">

<spring:url value="/resources/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet" />

<script>
var rootURL = '<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>' ;
</script>

</head>