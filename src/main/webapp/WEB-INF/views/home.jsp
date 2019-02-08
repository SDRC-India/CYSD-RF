<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>

<html lang="en">

<jsp:include page="fragments/headtag.jsp" />

<body>

	<div id="wrapper">
		<jsp:include page="fragments/bodyHeader.jsp"></jsp:include>
		<div class="container">
			<!-- 				<h1>Hi this is home</h1> -->
			<!-- 				<a href='./getUtData'><button>Hi this is home</button></a> -->
			<div class="row">
				<serror:Error id="msgBox" errorList="${formError}"
					cssInfClass="${className}">
				</serror:Error>
			</div>
			<section id="banner">
				<div id="carousel-banner-slide" class="carousel slide"
					data-ride="carousel">

					<div class="carousel-inner">
						<div class="item active">
							<img alt="cotton_connect_bg" src="resources/images/banner1.jpg"
								class="img-responsive" width="100%">
						</div>

						<div class="item">
							<img alt="cotton_connect_bg" src="resources/images/banner2.png"
								class="img-responsive" width="100%">
						</div>
						<div class="item">
							<img alt="cotton_connect_bg" src="resources/images/banner3.jpg"
								class="img-responsive" width="100%">
						</div>
					</div>

					<!-- Controls -->
					<a class="left carousel-control" href="#carousel-banner-slide"
						data-slide="prev"> <span
						class="glyphicon glyphicon-chevron-left"></span>
					</a> <a class="right carousel-control" href="#carousel-banner-slide"
						data-slide="next"> <span
						class="glyphicon glyphicon-chevron-right"></span>
					</a>
				</div>
			</section>
			<div class="content">
				<!-- 				<section id="cc_connect_site_menu" > -->
				<!-- 					<div class="container-fluid"> -->
				<!-- 						<h3> -->
				<!-- 							<i class="fa fa-arrow-circle-o-right fa-lg"></i>Cotton Connect Site Menu -->
				<!-- 						</h3> -->
				<!-- 						<div class="row"> -->
				<!-- 							<div class="col-md-3 text-center"> -->
				<!-- 								<a href="about"> -->
				<!-- 									<div class="site_menu"> -->
				<!-- 										<i class="fa fa-info-circle fa-3x"></i> -->
				<!-- 									</div> -->
				<!-- 								</a> <a href="about"><h4>About Cotton Connect</h4></a> -->
				<!-- 							</div> -->
				<!-- 							<div class="col-md-3 text-center"> -->
				<!-- 								<a href="resource"> -->
				<!-- 									<div class="site_menu"> -->
				<!-- 										<i class="fa fa-files-o fa-3x"></i> -->
				<!-- 									</div> -->
				<!-- 								</a><a href="resource"> -->


				<!-- 									<h4>Resources</h4> -->
				<!-- 								</a> -->

				<!-- 							</div> -->
				<!-- 							<div class="col-md-3 text-center"> -->
				<!-- 								<a href="sops"> -->
				<!-- 									<div class="site_menu"> -->
				<!-- 										<i class="fa fa-bars fa-3x "></i> -->
				<!-- 									</div> -->
				<!-- 								</a> <a href="sops"> -->
				<!-- 									<h4>SOPs</h4> -->
				<!-- 								</a> -->
				<!-- 							</div> -->
				<!-- 							<div class="col-md-3 text-center"> -->
				<!-- 								<a href="tools"> -->
				<!-- 									<div class="site_menu"> -->
				<!-- 										<i class="fa fa-gears fa-3x"></i> -->
				<!-- 									</div> -->
				<!-- 								</a> <a href="tools"> -->
				<!-- 									<h4>Tools</h4> -->
				<!-- 								</a> -->
				<!-- 							</div> -->
				<!-- 						</div> -->
				<!-- 					</div> -->
				<!-- 				</section> -->
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#msgBox").delay(2000).fadeOut(2000);
		})
	</script>
	<jsp:include page="fragments/footer.jsp" />
</body>

</html>
