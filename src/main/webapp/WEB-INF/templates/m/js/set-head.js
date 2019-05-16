$(function() {
    $('#imgFile').change(function(event) {
        // 根据这个 <input> 获取文件的 HTML5 js对象
        var files = event.target.files, file;
        if (files && files.length > 0) {
            // 获取目前上传的文件
            file = files[0];
            // 获取window的 URL工具
            var URL = window.URL || window.webkitURL;
            // 通过 file生成目标 url
            var imgURL = URL.createObjectURL(file);
            // 用这个URL产生一个 <img> 将其显示出来
            $('#cropimg1').attr('src', imgURL);
            $('#cropimg2').attr('src', imgURL);
            $('#cropimg3').attr('src', imgURL);
            $('#cropimg4').attr('src', imgURL);
            $('#imgFile').attr('src', imgURL);
        }
    });
});

function saveCropImage() {
    // 需要获取裁剪之后，裁剪框的宽度和高度，以及裁剪框相对于裁剪图片的坐标位置
    var mainBox = document.getElementById("mainBox");
    // 裁剪框的宽度
    var width = mainBox.clientWidth;
    // 裁剪框的高度
    var height = mainBox.clientHeight;
    // 相对于裁剪图片x左边
    var x = mainBox.offsetLeft;
    // 相对于裁剪图片y左边
    var y = mainBox.offsetTop;

    console.log(width);
    console.log(height);
    console.log(x);
    console.log(y);

    var options = {
        type: 'post',                     //post提交
        url: getUrl() + '/setHead',
        dataType: "json",                //json格式
        data: {
            'width': width, 'height': height,
            'x': x, 'y': y
        },        //如果需要提交附加参数，视情况添加
        clearForm: false,                //成功提交后，清除所有表单元素的值
        resetForm: false,                //成功提交后，重置所有表单元素的值
        cache: false,
        async: false,                    //同步返回
        success: function (data) {
            console.log(data);
            if(data.tips == "登录已失效"){
                window.location.href = getUrl()+"/toLogin_Register?type=login&url=toSetHead";
            }else{
                window.location.href = getUrl()+"/toUserMsg";
            }
        },
        error: function (XmlHttpRequest, textStatus, errorThrown) {
            console.log('操作失败');
            console.log(errorThrown);
        }
    };
    $('#uploadForm').ajaxSubmit(options);

    // AjaxFileUpload提交 或者 jQuery提交表单
};

function isimg(src) {
    var ext = ['.jpg', '.jpeg', '.png', '.gif', '.bmp'];
    var s = src.toLowerCase();
    var bool = false;
    for (var i = 0; i < ext.length; i++) {
        if (s.indexOf(ext[i]) > 0) {
            bool = true;
            break;
        }
    }
    return bool;
};