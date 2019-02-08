var menu = $('#head2');
var origOffsetY;
$(document).ready(function(){
	origOffsetY = menu.offset().top;
	$('html, body').animate({scrollTop:origOffsetY}, 1000);
});
