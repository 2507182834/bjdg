$(document).ready(function() {
    var type = "";
    if(user == undefined || user == null){
        window.location.href = getUrl()+"/toLogin_Register?type=login&url=toSetAccountMsg";
    }
    $("#uname-edit-img").click(function () {
        $("#uname-span").css("display","none");
        $("#submit-button").css("display","block");
        $("#uname-input").css("display","inline-block");
        $("#uname-edit-img").attr("src","imgs/close.jpg");
        $("#password-box").css("display","block");
        $("#password-edit-box").css("display","none");
        $("#email-box").css("display","block");
        $("#email-edit-box").css("display","none");
        $("#phone-box").css("display","block");
        $("#phone-edit-box").css("display","none");
        type = "uname";
    });

    $("#uname-input").bind('input propertychange', function() {
        var uname = $(this).val();
        var url = getUrl()+"/checkUname";
        console.log(user.name);
        $.get(
            url,
            {
                uname:uname,
                random:Math.random()
            },
            function(data,status){
                var msg = JSON.parse(data);
                if(msg.status == 0) {
                    $("#uname-edit-img").attr("src", "imgs/gou.png");
                }else{
                    $("#uname-edit-img").attr("src", "imgs/close.jpg");
                }
            }
        );
    });
    $("#password-edit-img").click(function () {
        $("#password-box").css("display","none");
        $("#password-edit-box").css("display","block");
        $("#submit-button").css("display","block");

        $("#uname-span").css("display","inline-block");
        $("#uname-input").css("display","none");;
        $("#uname-edit-img").attr("src","imgs/edit.jpg");
        $("#email-box").css("display","block");
        $("#email-edit-box").css("display","none");
        $("#phone-box").css("display","block");
        $("#phone-edit-box").css("display","none");
        type = "password";
    });

    $("#new-password-input").bind('input propertychange', function() {
        if($(this).val().length <6 || $(this).val().length>16){
            $("#new-password-img").attr("src", "imgs/close.jpg");
        }else{
            $("#new-password-img").attr("src", "imgs/gou.png");
        }
    });
    $("#email-img").click(function () {
        $("#email-box").css("display","none");
        $("#email-edit-box").css("display","block");
        $("#submit-button").css("display","block");

        $("#uname-span").css("display","inline-block");
        $("#uname-input").css("display","none");
        $("#uname-edit-img").attr("src","imgs/edit.jpg");
        $("#password-box").css("display","block");
        $("#password-edit-box").css("display","none");
        $("#phone-box").css("display","block");
        $("#phone-edit-box").css("display","none");
        type = "email";
    });
    $("#new-email-input").bind('input propertychange', function() {
        var email = $(this).val();
        var url = getUrl()+"/checkEmail";
        $.get(
            url,
            {
                email:email,
                random:Math.random()
            },
            function(data,status){
                var msg = JSON.parse(data);
                if(msg.status == 0) {
                    $("#new-email-img").attr("src", "imgs/gou.png");
                }else{
                    $("#new-email-img").attr("src", "imgs/close.jpg");
                }
            }
        );
    });
    $("#verification-img").click(function () {
        $(this).attr("src","getVerificationCode?XX="+Math.random());
    });

    $("#verification-button").click(function () {
        if(!($("#new-email-img").attr('src') == "imgs/gou.png")){
            alert("输入邮箱错误");
            return false;
        }
        var VerificationCode = getSessionAttribute("VerificationCode");

        if($("#verification-input").val().toLowerCase() == VerificationCode.toLowerCase()){
            $.post(
                getUrl()+"/sendVerificationCode",
                {
                    type:"email",
                    account:$("#new-email-input").val()
                },
                function (data) {
                    var msg = JSON.parse(data)
                    if(msg.status == 0){
                        alert("验证码已发送");
                        $("#verification-box").css("display","none");
                        $("#email-verification-box").css("display","block");
                    }else{
                        alert(msg.tips);
                    }
                }
            );
        }else{
            $("#verification-img").click();
            alert("验证码输入错误");
        }

    });




    $("#phone-edit-img").click(function () {
        $("#phone-box").css("display","none");
        $("#phone-edit-box").css("display","block");
        $("#submit-button").css("display","block");
        $("#uname-span").css("display","inline-block");
        $("#uname-input").css("display","none");
        $("#uname-edit-img").attr("src","imgs/edit.jpg");
        $("#password-box").css("display","block");
        $("#password-edit-box").css("display","none");
        $("#email-box").css("display","block");
        $("#email-edit-box").css("display","none");
        type = "phone";
    });
    $("#new-phone-input").bind('input propertychange', function() {
        var phone = $(this).val();
        var url = getUrl()+"/checkPhone";
        $.get(
            url,
            {
                phone:phone,
                random:Math.random()
            },
            function(data,status){
                var msg = JSON.parse(data);
                if(msg.status == 0) {
                    $("#new-phone-img").attr("src", "imgs/gou.png");
                }else{
                    $("#new-phone-img").attr("src", "imgs/close.jpg");
                }
            }
        );
    });
    $("#xx-verification-img").click(function () {
        $(this).attr("src","getVerificationCode?XX="+Math.random());
    });

    $("#xx-verification-button").click(function () {
        if(!($("#new-phone-img").attr('src') == "imgs/gou.png")){
            alert("输入号码错误");
            return false;
        }
        var VerificationCode = getSessionAttribute("VerificationCode");

        if($("#xx-verification-input").val().toLowerCase() == VerificationCode.toLowerCase()){
            $.post(
                getUrl()+"/sendVerificationCode",
                {
                    type:"phone",
                    account:$("#new-phone-input").val()
                },
                function (data) {
                    var msg = JSON.parse(data)
                    if(msg.status == 0){
                        alert("验证码已发送");
                        $("#xx-verification-box").css("display","none");
                        $("#phone-verification-box").css("display","block");
                    }else{
                        alert(msg.tips);
                    }
                }
            );
        }else{
            $("#xx-verification-img").click();
            alert("验证码输入错误");
        }

    });



    $("#submit-button").click(function () {
        switch (type) {
            case "uname":
                if($("#uname-edit-img").attr('src') == "imgs/gou.png") {
                    $.post(
                        getUrl() + "/setUser",
                        {
                            uname: $("#uname-input").val()
                        },
                        function (data) {
                            console.log(data);
                            var msg = JSON.parse(data);
                            if (msg.status == 0) {
                                window.location.href = getUrl() + "/toSetAccountMsg";
                            } else {
                                alert(msg.tips);
                            }
                        }
                    );
                }
                break;
            case "password":
                if($("#new-password-img").attr('src') == "imgs/gou.png"){
                    $.post(
                        getUrl() + "/setUser",
                        {
                            oldPassword: $("#old-password-input").val(),
                            password: $("#new-password-input").val()
                        },
                        function (data) {
                            console.log(data);
                            var msg = JSON.parse(data);
                            if (msg.status == 0) {
                                window.location.href = getUrl() + "/toSetAccountMsg";
                            } else {
                                alert(msg.tips);
                            }
                        }
                    );
                }
                break;
            case "email":
                console.log("email");
                if($("#email-verification-input").val().length!=6){
                    alert("输入验证码错误");
                }
                if($("#new-email-img").attr('src') == "imgs/gou.png"){
                    $.post(
                        getUrl() + "/checkedVerificationCode",
                        {
                            account: $("#new-email-input").val(),
                            verification: $("#email-verification-input").val(),
                            type:"email"
                        },
                        function (data) {
                            console.log(data);
                            var msg = JSON.parse(data);
                            if (msg.status == 0) {
                                alert("邮箱修改成功")
                                window.location.href = getUrl() + "/toSetAccountMsg";
                            } else {
                                alert(msg.tips);
                            }
                        }
                    );
                }
        }
    });
});