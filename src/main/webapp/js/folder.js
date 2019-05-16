
function edit(data){
    console.log(data);
    $("#edit_name_msg").css("color","green");
    $("#edit_name_msg").text("");
    var type;
    if(data.createTime ==  undefined){
        type = "file";
    }else{
        type = "folder";
    }
    $("#edit_name_input").val(data.fname);
    $("#edit_authority").val(data.authority);
    $("#edit_draga_box").css("display","block");
    $("#edit_delete").css("background","#ff4646");
    var edit_file_id = $("<input value=\""+data.fid+"\" type=\"hidden\" id=\"edit_file_id\"/>");
    var edit_file_type = $("<input value=\""+type+"\" type=\"hidden\" id=\"edit_file_type\"/>");
    $("#edit_file_id").remove();
    $("#edit_draga_box").append(edit_file_id);
    $("#edit_file_type").remove();
    $("#edit_draga_box").append(edit_file_type);
}

function progressFunction(evt) {
    console.log(evt);
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
       $("#pdBar").css("width",completePercent);
       $("#pdNum").text(completePercent);
    }
}


$(document).ready(function(){
    initHead();

    console.log($("#preFolder").val());
    console.log($("#folders").val());
    var preFolder = JSON.parse($("#preFolder").val());
    var folders = JSON.parse($("#folders").val());//接收到的数据转化为JQuery对象，由JQuery为我们处理
    var files;
    if(preFolder != undefined && preFolder.ffid != 0) {
        var folder_line = $("<div class='folder_line'></div>");
        var folder_name = "<div class='folder_name'><img src='imgs/dir.jpg' class='folder_icon'><a href=" + (getUrl() + "/getFolders?ffid=" + preFolder.ffid) + "> .. </a></div>";
        folder_line.append(folder_name);
        $("#folder_box").append(folder_line);
    }
    $.each(folders, function(index, objVal) { //遍历对象数组，index是数组的索引号，objVal是遍历的一个对象。
        folder_line = $("<div class='folder_line'></div>");
        folder_name = "<div class='folder_name'><img src='imgs/dir.jpg' class='folder_icon'><a href="+(getUrl()+"/getFolders?ffid="+objVal.fid)+">"+objVal.fname+"</a></div>";
        var folder_lasttime = "<div class='folder_lasttime'>"+datetimeFormat(objVal.lastTime)+"</div>"
        folder_line.append(folder_name);
        folder_line.append(folder_lasttime);
        $("#folder_box").append(folder_line);
    });

    var url = getUrl()+"/getFiles";
    $.get(
        url,
        {
            ffid:preFolder.fid
        },
        function (data,status) {
            console.log(data);
            files = JSON.parse(data);
            $.each(files, function(index, objVal) { //遍历对象数组，index是数组的索引号，objVal是遍历的一个对象。
                folder_line = $("<div class='folder_line'></div>");
                folder_name = "<div class='folder_name'><img src='imgs/file.jpg' class='file_icon'><a href="+(getUrl()+"/download?fid="+objVal.fid)+">"+objVal.fname+"</a></div>";
                var folder_lasttime = "<div class='folder_lasttime'>"+datetimeFormat(objVal.lastTime)+"</div>"
                folder_line.append(folder_name);
                folder_line.append(folder_lasttime);
                $("#folder_box").append(folder_line);
            });
        }
    );
    console.log(user);
    if(user != null && user.authority<2) {
        $("#file_handle").css("display","block");
    }
    $("#new_folder_button").click(function () {
        $("#folder_name_input").val("");
        $("#folder_name_msg").text("");
        $("#draga_box1").css("display","block");
    });

    $("#file_input_esc1").click(function () {
        $("#folder_name_input").val("");
        $("#draga_box1").css("display","none");
    });

    $("#close_draga1").click(function () {
        $("#folder_name_input").val("");
        $("#draga_box1").css("display","none");
    });
    
    $("#folder_name_submit").click(function () {
        console.log("folder_name_submit");
        var url = getUrl()+"/newFolder";
        var foldername = $("#folder_name_input").val();
        var ffid = preFolder.fid;
        var authority = $("#authority_new").val();
        $.get(
            url,
            {
                ffid:ffid,
                foldername:foldername,
                authority:authority
            },
            function (data, status) {
                var msg = JSON.parse(data);
                console.log(msg.status+"-----"+msg.tips);
                if(msg.status == 0){
                    window.location.href = getUrl()+"/getFolders?ffid="+ffid;
                }else{
                    $("#folder_name_msg").css("color","red");
                    $("#folder_name_msg").text(msg.tips);

                }
            }
        )
    });

    $("#upload_button").click(function () {
        $("#draga_box2").css("display","block");
    });

    $("#file_input_esc2").click(function () {
        $("#file_input").val("");
        $("#draga_box2").css("display","none");
    });

    $("#close_draga2").click(function () {
        $("#file_input").val("");
        $("#draga_box2").css("display","none");
    });
    
    $("#file_input_submit2").click(function () {
        var url = getUrl()+"/upLoadFile";
        var ffid = preFolder.fid;
        var authority = $("#authority_file").val();
        var options = {
            type:'post',                     //post提交
            url:url,
            dataType:"json",                //json格式
            data:{'ffid':ffid,'authority':authority},        //如果需要提交附加参数，视情况添加
            clearForm: true,                //成功提交后，清除所有表单元素的值
            resetForm: true,                //成功提交后，重置所有表单元素的值
            cache:false,
            async:true,                    //同步返回
            xhr:function(){
                myXhr = $.ajaxSettings.xhr();
                if(progressFunction && myXhr.upload) { //检查进度函数和upload属性是否存在
                    //绑定progress事件的回调函数
                    $("#upload-content").css("display","none");
                    $("#pdBar").css("width","0%");
                    $("#pdNum").text("0%");
                    $("#pgID").css("display","block");
                    myXhr.upload.addEventListener("progress",progressFunction, false);
                }
                return myXhr; //xhr对象返回给jQuery使用
            },
            success:function(data){
                console.log(data);
                if(data.status == 0){
                    window.location.href = getUrl()+"/getFolders?ffid="+ffid;
                }else{
                    $("#upload-content").css("display","block");
                    $("#pgID").css("display","none");
                    $("#file_msg").css("color","red");
                    $("#file_msg").css("margin-left","0px");
                    $("#file_msg").text(data.tips);

                }
            },
            error:function(XmlHttpRequest,textStatus,errorThrown){
                alert('操作失败');
                window.location.href = getUrl()+"/getFolders?ffid="+ffid;
            }
        };
        $('#infoLogoForm').ajaxSubmit(options);
    });

    $("#file_input").change(function () {
        $("#file_msg").css("color","green");
        $("#file_msg").css("margin-left","0px");
        $("#file_msg").text("");
    });
    
    $("#edit_button").click(function () {

        console.log($("#edit_button").text());
        if($("#edit_button").text() == "编辑文件") {
            $("#new_folder_button").attr('disabled', "true");
            $("#upload_button").attr('disabled', "true");
            $("#edit_button").text("完成");
            $("#folder_box").children().remove();
            var title = $("<div class=\"folder_line\"><div class=\"folder_name\">文件名</div><div class=\"folder_lasttime\">修改日期</div></div>");
            $("#folder_box").append(title);
            $.each(folders, function (index, objVal) { //遍历对象数组，index是数组的索引号，objVal是遍历的一个对象。
                folder_line = $("<div class='folder_line'></div>");
                folder_name = "<div class='folder_name'><img src='imgs/dir.jpg' class='folder_icon'><span class='edit_folder_name' onclick='edit("+JSON.stringify(objVal)+")'id='folder"+objVal.fid+"'>" + objVal.fname + "</span></div>";
                var folder_lasttime = "<div class='folder_lasttime'>" + datetimeFormat(objVal.lastTime) + "</div>"
                folder_line.append(folder_name);
                folder_line.append(folder_lasttime);
                $("#folder_box").append(folder_line);
            });

            $.each(files, function(index, objVal) { //遍历对象数组，index是数组的索引号，objVal是遍历的一个对象。
                folder_line = $("<div class='folder_line'></div>");
                folder_name = "<div class='folder_name'><img src='imgs/file.jpg' class='file_icon'><span class='edit_folder_name' onclick='edit("+JSON.stringify(objVal)+")' id='file"+objVal.fid+"'>" + objVal.fname + "</span></div>";
                var folder_lasttime = "<div class='folder_lasttime'>"+datetimeFormat(objVal.lastTime)+"</div>"
                folder_line.append(folder_name);
                folder_line.append(folder_lasttime);
                $("#folder_box").append(folder_line);
            });
        }else{
            window.location.href = getUrl()+"/getFolders?ffid="+preFolder.fid;
        }
    });

    $("#edit_close_draga").click(function () {
        $("#edit_draga_box").css("display","none");
    });
    $("#edit_submit").click(function () {
        var url = getUrl();
        var fid = $("#edit_file_id").val();
        var type = $("#edit_file_type").val();
        var authority = $("#edit_authority").val();
        var fname = $("#edit_name_input").val();

        if(type=="file"){
            url = url + "/UpdateFile";
        }else  if(type == "folder"){
            url = url + "/UpdateFolder";
        }else{
            return false;
        }
        $.post(
            url,
            {
                fid:fid,
                fname:fname,
                authority:authority
            },
            function (data) {
                console.log(data);
                var msg = JSON.parse(data);
                if(msg.status == 0){
                    console.log("SUCCESS");
                    $("#edit_close_draga").click();
                    $("#"+type+fid).text(fname);
                }else{
                    $("#edit_name_msg").css("color","red");
                    $("#edit_name_msg").text(msg.tips);
                }
            }
        );
    });
    $("#edit_delete").click(function () {
        var url = getUrl();
        var fid = $("#edit_file_id").val();
        var type = $("#edit_file_type").val();

        if(type=="file"){
            url = url + "/DeleteFile";
        }else  if(type == "folder"){
            url = url + "/DeleteFolder";
        }else{
            return false;
        }
        $.post(
            url,
            {
                fid:fid
            },
            function (data) {
                console.log(data);
                var msg = JSON.parse(data);
                if(msg.status == 0){
                    console.log("SUCCESS");
                    $("#edit_close_draga").click();
                    $("#"+type+fid).parent().parent().remove();
                }else{
                    $("#edit_name_msg").css("color","red");
                    $("#edit_name_msg").text(msg.tips);
                }
            }
        );
    });
});
