$(document).ready(function(){

    initHead();

    $(".dropdown").mouseover(function (){
        $(this).find("ul").css("display","block");
    });

    $(".dropdown").mouseout(function () {
        $(this).find("ul").css("display","none");
    });

    $(".dropdown-menu").mouseout(function () {
        $(this).css("display","none")
    });

    $(".user-dropdown").mouseover(function (){
        $(this).css("background","#FFF");
        $(this).find("li").find("a").css("color","#000");
        $(this).find("li").find("ul").css("display","block");
    });

    $(".user-dropdown").mouseout(function () {
        $(this).css("background","#000");
        $(this).find("li").find("a").css("color","#FFF");
        $(this).find("li").find("ul").css("display","none");
    });

    $(".dropdown-menu").mouseout(function () {
        $(".user-dropdown").css("background","#000");
        $(".user-dropdown").find("li").find("a").css("color","#FFF");
        $(this).css("display","none")
    });
});