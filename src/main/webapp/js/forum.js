$(document).ready(function() {
    var page;

    if(user == undefined || user == null){
        window.location.href = getUrl()+"/toLogin_Register?type=login&url=toForum";
    }

    $(".pagination").children().remove();
    var currentPage = parseInt($("#current-page").val());
    var totalPage = $("#total-page").val();
    var hasPreviousPage = $("#hasPreviousPage").val();
    var hasNextPage = $("#hasNextPage").val();
    var orderBy = $("#orderBy").val();
    var type = $("#type").val();
    var val = $("#search-input").val();
    var begin = 1;
    var clazz = "";
    var temp = currentPage;
    if(hasPreviousPage == "false"){
        clazz = "disabled";
        temp = currentPage + 1;
    }
    var s = "";
    if(totalPage>0) {
        s = s + "<li class="+clazz+"><a href="+getUrl()+"/toForum?currentpage="+(temp-1)+"&orderBy="+orderBy+"&type="+type+"&value="+val+" aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
    }

    if(currentPage > 3){
        begin = currentPage-2;
    }
    for(var i=0; i<5 && begin <= totalPage; i++){
        clazz = "";
        if(currentPage==begin){
            clazz = "active";
        }
       s = s + "<li class="+clazz+"><a href="+getUrl()+"/toForum?currentpage="+begin+"&orderBy="+orderBy+"&type="+type+"&value="+val+">"+begin+"</a></li>";
       begin++;
    }
    clazz = "";
    temp = currentPage;
    if(hasNextPage == "false"){
        clazz = "disabled";
        temp = currentPage - 1;
    }
    if(totalPage>0) {
        s = s + "<li class=" + clazz + "><a href=" + getUrl() + "/toForum?currentpage=" + (temp + 1) + "&orderBy=" + orderBy + "&type=" + type + "&value=" + val + " aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
    }
    var li = $(s);

    $(".pagination").append(li);

    $("#new-forum").click(function () {
        window.location.href = getUrl()+"/toNewForum";
    });

    $(".forum-title").click(function () {
        var fid= $(this).siblings("input").val();

        window.location.href=getUrl()+"/getForumMsg?fid="+fid;
        console.log(fid);
    });

    $("#orderByHot").click(function () {
        if(type == "asc"){
            type = "desc";
        }else{
            type = "asc";
        }
        console.log("hot");
        window.location.href=getUrl()+"/toForum?orderBy=hot&type="+type;
    });
    $("#orderByCreateTime").click(function () {
        if(type == "asc"){
            type = "desc";
        }else{
            type = "asc";
        }
        window.location.href=getUrl()+"/toForum?orderBy=createtime&type="+type;
    });

    $("#aboutMe").click(function () {
        if(type == "asc"){
            type = "desc";
        }else{
            type = "asc";
        }
        console.log("hot");
        window.location.href=getUrl()+"/toForum?orderBy=aboutme&type="+type;
    });
    $("#orderByCreateTime").removeClass("forum-active");
    $("#orderByHot").removeClass("forum-active");
    $("#aboutMe").removeClass("forum-active");
    switch (orderBy) {
        case "hot":
            $("#orderByHot").addClass("forum-active");
            break;
        case "createtime":
            $("#orderByCreateTime").addClass("forum-active");
            break;
        case "aboutme":
            $("#aboutMe").addClass("forum-active");
    }

    $("#search").click(function () {
        val = $("#search-input").val();
        console.log(val);
        window.location.href=getUrl()+"/toForum?currentpage=1&orderBy="+orderBy+"&type="+type+"&value="+val;
    });
});