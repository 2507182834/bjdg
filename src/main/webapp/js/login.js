
function resolveMsg(data,status,label){
    console.log(data);
    var msg = JSON.parse(data);
    console.log(msg.status);
    if(msg.status == 0){
        label.text(msg.tips);
        label.css("color","green");
        return true;
    }else{
        label.text(msg.tips);
        label.css("color","red");
        return false;
    }
}


$(document).ready(function(){
        var unameflag = false;
        var emailflag = false;
        var passwordflag = false;
        var ensure_passwordflag = false;

        function enableRegisterButton() {
            console.log(unameflag+"--"+emailflag+"--"+passwordflag+"--"+ensure_passwordflag);
            if(unameflag == true && emailflag == true && passwordflag == true && ensure_passwordflag==true) {
                $('#register_submit').removeAttr("disabled");
            }else{
                $('#register_submit').attr('disabled',"true");
            }
        }

        $("#login").click(function () {
            var url = $("#url").val();
            if(url == undefined || url == "")  {
                window.location.href = getUrl() + "/toLogin_Register?type=login";
            }else{
                window.location.href = getUrl() + "/toLogin_Register?type=login&url="+url;
            }
        });

        $("#register").click(function () {
            window.location.href = getUrl()+"/toLogin_Register?type=register";
        });

        if($("#type").val() == "register"){
            $("#login_box").addClass("display_none");
            $("#register_box").removeClass("display_none");
            $()
        }else{
            $("#register_box").addClass("display_none");
            $("#login_box").removeClass("display_none");
        }

        
        $("#login_submit").click(function () {
            var url = getUrl()+"/login";
            var uname = $("#login_uname").val();
            var password = $("#login_password").val();

            $.get(
                url,
                {
                    uname:uname,
                    password:password
                },
                function (data, status) {
                    console.log(data)
                    var msg = JSON.parse(data);
                    if(msg.status == 0){
                        if($("#url").val() == undefined || $("#url").val() == "") {
                            window.location.href = getUrl() + "/toIndex";
                        }else{
                            window.location.href = getUrl() + "/"+$("#url").val();
                        }
                    }else{
                        $("#login_password_msg").text(msg.tips);
                        $("#login_password_msg").css("color","red");
                    }
                }
            )
        });

        $("#register_submit").click(function () {
            $('#register_submit').attr('disabled',"true");
            var url = getUrl()+"/register";
            var uname = $("#register_uname").val();
            var email = $("#register_email").val();
            var password = $("#register_password").val();

            $.get(
                url,
                {
                    uname:uname,
                    email:email,
                    password:password
                },
                function (data, status) {
                    console.log(data)
                    var msg = JSON.parse(data);
                    if(msg.status == 0){
                        alert("注册成功,激活邮件已經发往注冊邮箱，赶快去激活吧！");
                        window.location.href = getUrl()+"/toLogin_Register?type=login";
                    }else{
                        alert(msg.tips);
                        window.location.href = getUrl()+"/toLogin_Register?type=register";
                    }
                }
            )
        });

        $('#register_submit').attr('disabled',"true");


        $("#register_uname").blur(function(){
            var uname = $("#register_uname").val();
            var url = getUrl()+"/checkUname";
            console.log(uname);
            $.get(
                url,
                {
                    uname:uname,
                    random:Math.random()
                },
                function(data,status){
                    unameflag = resolveMsg(data,status,$("#uname_msg"));
                    enableRegisterButton();
                }
            );
        });

    $("#register_email").blur(function(){
        var email = $("#register_email").val();
        var url = getUrl()+"/checkEmail";
        console.log(email);
        $.get(
            url,
            {
                email:email,
                random:Math.random()
            },
            function(data,status){
                emailflag = resolveMsg(data,status,$("#email_msg"));
                enableRegisterButton();
            });
    });

    $("#register_password").blur(function(){
        var password = $("#register_password").val();
        var url = getUrl()+"/checkPassword";
        console.log(password);
        $.get(
            url,
            {
                password:password,
                random:Math.random()
            },
            function(data,status){
               passwordflag = resolveMsg(data,status,$("#password_msg"));
                var password1 = $("#register_password").val();
                var password2 = $("#register_ensure_password").val();
                if(password1 == password2){
                    $("#ensure_password_msg").text("");
                    $("#ensure_password_msg").css("color","green");
                    ensure_passwordflag = true;
                }else{
                    $("#ensure_password_msg").text("两次输入密码不一致");
                    $("#ensure_password_msg").css("color","red");
                    ensure_passwordflag = false;
                }

                enableRegisterButton();
            });
    });
    $("#register_ensure_password").blur(function(){
        var password1 = $("#register_password").val();
        var password2 = $("#register_ensure_password").val();
        if(password1 == password2){
            $("#ensure_password_msg").text("");
            $("#ensure_password_msg").css("color","green");
            ensure_passwordflag = true;
        }else{
            $("#ensure_password_msg").text("两次输入密码不一致");
            $("#ensure_password_msg").css("color","red");
            ensure_passwordflag = false;
        }
        enableRegisterButton();
    });
    });