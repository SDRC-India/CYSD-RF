<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<footer class="bottomfooter">
		<div class="container-fluid">
		
		<div class="row">
			<div class="col-md-6 text-left" style="margin-top : 5px;">
				<span class="footerfont">Version: v1.0</span>
			</div>
			
			<div class="col-md-6 text-right">
				<span class="footerfont">Powered by </span><a href="http://sdrc.co.in/"
					target="_blank" class="text_deco_none"><span
					class="poweredbysdrc">SDRC</span></a>
			</div>
		</div>
			
		</div>

	</footer>

<!-- Keep your java scripts down below -->
<spring:url value="/resources/js/core.js" var="coreJS" />
<script src="${coreJS}"></script>





