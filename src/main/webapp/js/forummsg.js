$(document).ready(function(){
    var fid = $("#fid").val();
    var floor = $("#floor").val();

    if(floor>0){
        var floorid = "#floor"+floor;
        $('body,html').animate({scrollTop: $(floorid).offset().top}, 500);
        $(floorid).parent().parent().animate({backgroundColor:'#bbbbbb'},500);
        $(floorid).parent().parent().animate({backgroundColor:'#FFFFFF'},500);
    }

    $("#remark").click(function () {
        UE.getEditor('editor').execCommand('insertHtml', " ");
    });
    $(".remark-short").click(function () {
        var value = "<p style='color: #999999;' class='remark-floor-number'>回复：“" +$(this).parent().siblings("span").text()+"”</p>";
        UE.getEditor('editor').execCommand('insertHtml', value);
    });

    var content = $("#content-input").val();
    $("#content").append($(content));
    $("#user-img").css("background"," url(head_image/"+$("#authorName").val()+".jpg)");

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
        temp = currentPage + 1;
    }
    var s = "";
    if(totalPage>0) {
        s = "<li class=" + clazz + "><a href=" + getUrl() + "/getForumMsg?fid=" + fid + "&currentpage=" + (temp - 1) + " aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
    }
    if(currentPage > 3){
        begin = currentPage-2;
    }
    console.log("totalPage:"+totalPage);
    for(var i=0; i<5 && begin <= totalPage; i++){
        clazz = "";
        if(currentPage==begin){
            clazz = "active";
        }
        s = s + "<li class="+clazz+"><a href="+getUrl()+"/getForumMsg?fid="+fid+"&currentpage="+begin+">"+begin+"</a></li>";
        begin++;
    }
    clazz = "";
    temp = currentPage;
    if(hasNextPage == "false"){
        clazz = "disabled";
        temp = currentPage - 1;
    }

    if(totalPage > 0) {
        s = s + "<li class=" + clazz + "><a href=" + getUrl() + "/getForumMsg?fid=" + fid + "&currentpage=" + (temp + 1) + " aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
    }

    var li = $(s);

    $(".pagination").append(li);
    
    
    $("#editor-button-publish").click(function () {
        var text = UE.getEditor('editor').getContent();
        if(text == ""){
            alert("评论的内容不能为空");
            return false;
        }
        $.post(
            getUrl()+"/newRemark" ,
            {
                fid : fid,
                text: text
            },
            function (data) {
                removeLoading();
                var msg = JSON.parse(data);
                if(msg.status == 0){
                    window.location.href = getUrl()+"/getForumMsg?fid="+fid+"&currentpage="+($("#totalPages").val()+1)+"&floor="+msg.tips;
                }else{
                    alert(msg.tips)
                }
            }
        );
    });

    $(".remark-floor-number").click(function () {
        var id = $(this).text();
        var floor = id.substring(id.lastIndexOf("“")+2,id.lastIndexOf("”"));
        $.post(
            getUrl()+"/getRemarkNum",
            {
                fid:fid,
                floor: floor
            },
            function (data) {
                var currentpage = (parseInt(data/20))+1;
               window.location.href = getUrl()+"/getForumMsg?currentpage="+currentpage+"&fid="+fid+"&floor="+floor;
            }
        );
    });

    var type = "";
    var tid = 0;
    $("#report").click(function () {
        type = "主题";
        tid = fid;
        $("#edit_draga_box").css("display","block");
    });
    $(".remark-report").click(function () {
        type="评论";
        tid = $(this).parent().siblings("input").val();
        $("#edit_draga_box").css("display","block");
    });
    $("#edit_close_draga").click(function () {
        $("#edit_draga_box").css("display","none");
    });
    $("#edit_submit").click(function () {

        var reportReason = "";

        for(var i=0; i<$("#edit_draga_box p").length; i++){
           var v = $("#edit_draga_box p").eq(i).children("input").val();
           var c = $("#edit_draga_box p").eq(i).children("input").is(":checked");
           if(c){
                reportReason += (v + "、");
           }
        }
        if(reportReason != ""){
            reportReason = reportReason.substring(0, reportReason.length-1);
        }
        $.post(
            getUrl()+"/newReportMsg",
            {
                type:type,
                msg:reportReason,
                target:tid,
            },
            function (data) {
                var msg = JSON.parse(data);
                if(msg == true){
                    $("#edit_close_draga").click();
                    for(var i=0; i<$("#edit_draga_box p").length; i++){
                        $("#edit_draga_box p").eq(i).children("input").prop({checked:false})
                    }
                    alert("举报成功");
                }else{
                    window.location.href = getUrl()+"/toLogin_Register?type=login&url=getForumMsg?fid="+fid;
                }
            }
        );
    });
    $("#edit_delete").click(function () {
        $("#edit_close_draga").click();
    });

});