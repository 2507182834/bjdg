
$(document).ready(function() {

    if (user == undefined || user == null) {
        window.location.href = getUrl() + "/toLogin_Register?type=login&url=admin";
    } else if (user.authority > 0){
        window.location.href = getUrl() + "/toIndex";
    }
    

    $("#publish-system-msg").click(function () {
        var title = $("#title-input").val();
        var text = UE.getEditor('editor').getContent();
        if(title == null || title == "") {
            alert("主题不能为空");
            return false;
        }
        if(text.length>2000){
            alert("消息内容过长 "+text);
            return false;
        }
        $.get(
            getUrl()+"/publishSystemMsg",
            {
                title:title,
                text:text
            },
            function (data) {
                var msg = JSON.parse(data);
                if(msg.status==0){
                    alert("发布成功");
                    $("#title-input").val("");
                    UE.getEditor('editor').setContent("");
                }else{
                    alert("系统错误");
                }
            }
        );
    });

    $("#title-input").bind('input propertychange', function() {
        var count = $("#title-input").val().length;
        if(count > 50){
            count = 50;
            var text = $("#title-input").val();
            text = text.substring(0,50);
            $("#title-input").val(text);
        }
        var text = "还可输入 "+(50-count)+" 个字符";
        $("#title-tips").text(text);

    });
    var target = "";
    var type = "";
    var id = "";
    $(".unRead-msg").click(function () {
        $(".draga_content").append($(this).next().next().val());
        type = $(this).next().next().next().val();
        target = $(this).next().next().next().next().val();
        id = $(this).next().next().next().next().next().val();
        $("#edit_draga_box").fadeIn(500);
        $(this).css("color","#999999");
    });

    $("#edit_close_draga").click(function () {
        $("#edit_draga_box").fadeOut(500);
        $(".draga_content").text("");
    });

    $("#edit_submit").click(function () {
        $.post(
            getUrl()+"/solveReport",
            {
                target:target,
                type:type,
                id:id,
                random:Math.random()
            },
            function (data) {
                if(data == "true"){
                    alert("删除成功");
                    $("#edit_close_draga").click();
                }
            }
        );
    });
    $("#edit_delete").click(function () {
        $.post(
            getUrl()+"/solveReport",
            {
                target:target,
                id:id,
                random:Math.random()
            },
            function (data) {
                if(data == "true"){
                    alert("处理完成");
                    $("#edit_close_draga").click();
                }
            }
        );
    });
});

window.onload = function () {
    UE.getEditor('editor').setHeight(170);
}

