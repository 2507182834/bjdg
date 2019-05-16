$(document).ready(function(){

    if(user == undefined || user == null){
        window.location.href = getUrl()+"/toLogin_Register?type=login&url=toUserMsg";
    }

    $(".pagination").children().remove();
    var currentPage = parseInt($("#current-page").val());
    var totalPage = $("#total-page").val();
    var hasPreviousPage = $("#hasPreviousPage").val();
    var hasNextPage = $("#hasNextPage").val();
    var begin = 1;
    var clazz = "";
    var temp = currentPage;
    if(hasPreviousPage == "false"){
        clazz = "disabled";
        temp = currentPage+1;
    }
    var s = "";
    if(totalPage>0) {
        s = s + "<li class=" + clazz + "><a href=" + getUrl() + "/toUserMsg?currentpage=" + (temp - 1) + " aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
    }
    if(currentPage > 3){
        begin = currentPage-2;
    }
    for(var i=0; i<5 && begin <= totalPage; i++){
        clazz = "";
        if(currentPage==begin){
            clazz = "active";
        }
        s = s + "<li class="+clazz+"><a href="+getUrl()+"/toUserMsg?currentpage="+begin+">"+begin+"</a></li>";
        begin++;
    }
    clazz = "";
    temp = currentPage;
    if(hasNextPage == "false"){
        clazz = "disabled";
        temp = currentPage - 1;
    }
    if(totalPage>0) {
        s = s + "<li class=" + clazz + "><a href=" + getUrl() + "/toUserMsg?currentpage=" + (temp + 1) + " aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
    }
    var li = $(s);

    $(".pagination").append(li);

    $("#user-img").css("background"," url(head_image/"+user.name+".jpg)");

    $("#user-img").mouseover(function () {
        $("#user-img-tips").css("display","block");
    })
    $("#user-img").mouseout(function () {
        $("#user-img-tips").css("display","none");
    })
    $("#user-img").click(function () {
        window.location.href = getUrl()+"/toSetHead";
    });
    $("#user-name").click(function () {
        window.location.href = getUrl()+"/toSetAccountMsg";
    });

    $(".unRead-msg").click(function () {
        var type = $(this).next().next().next().val();
        var id = $(this).next().next().next().next().val();
        $.post(
            getUrl()+"/hasReadMsg",
            {
              type: type,
                id: id
            },
            function (data) {
            }
        );
        var msg = $(this).next().next().val();
        $(".draga_content").append(msg);
        $("#edit_draga_box").fadeIn(500);
        $(this).css("color","#999999");
    });

    $("#edit_close_draga").click(function () {
        $("#edit_draga_box").css("display","none");
        $(".draga_content").text("");
    });
});