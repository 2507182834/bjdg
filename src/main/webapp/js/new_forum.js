$(document).ready(function() {

    if(user == undefined || user == null){
        window.location.href = getUrl()+"/toLogin_Register?type=login&url=toNewForum";
    }

    $("#new-forum-topic-input").bind('input propertychange', function() {
        var count = $("#new-forum-topic-input").val().length;
        if(count > 90){
            count = 90;
            var text = $("#new-forum-topic-input").val();
            text = text.substr(0,90);
            $("#new-forum-topic-input").val(text);
        }
        var text = "还可输入 "+(90-count)+" 个字符";
        $("#new-forum-topic-tips").text(text);

    });
    $("#editor-button-publish").click(function () {
        loading();
        var title = $("#new-forum-topic-input").val();
        var text = UE.getEditor('editor').getContent();

        $.post(
            getUrl()+"/newForum" ,
            {
              title: title,
              text: text
            },
            function (data) {
                removeLoading();
                var msg = JSON.parse(data);
                if(msg.status == 0){
                    alert(msg.tips);
                    window.location.href = getUrl()+"/toForum"
                }else{
                    alert(msg.tips)
                }
            }
        );
    });
});