<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html lang="en">

<jsp:include page="fragments/headtag.jsp" />

<body>

	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
			<div class="container-fluid">
				<section>
					<div class="col-md-12">
						<div class="col-md-12">
							<h2 class="page-header cysdColor" data-html2canvas-ignore="true">About CYSD</h2>
						</div>
						<div class="col-md-8">
							<p class="aboutText">
								CYSD (Centre for Youth and Social Development) is a non-government and 
								non-profit organization established in 1982, working to improve the 
								quality of lives of tribal, rural and urban poor in Odisha, 
								with a primary focus to eradicate extreme poverty and hunger,
								 ensuring social inclusion and justice, good governance and 
								 citizen's right. Helping communities identify and initiate 
								 development measures; providing training and other capacity-building 
								 support to pro-poor organizations and individuals; and carrying out
								  research and advocacy in favour of the under privileged people
								   especially the tribal.
							</p>
						</div>
					</div>
				</section>
			</div>
	</div>
	<jsp:include page="fragments/footer.jsp" />
</body>

</html>
