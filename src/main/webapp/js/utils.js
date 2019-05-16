function loading() {
    var box = $("<div id = 'loadingBox'style='width: 100%; height: 100%; background: rgba(0,0,0,0.1);position: absolute;top: 0; z-index: 999;'></div>");
    $("body").append(box)
}

function removeLoading() {
    $("#loadingBox").remove();
}

/**
 * 获取Session域的值
 * @param value
 */
function getSessionAttribute(value) {
    var result = null;
    $.ajax({
        type:'post',
        url:getUrl()+"/getSessionAttribute",
        data:{value:value,random:Math.random()},
        async:false,//false代表只有在等待ajax执行完毕后才执行后续代码
        success:function (data){
            var msg = JSON.parse(data);
            if(msg.status == 0){
                result = JSON.parse(msg.data);
            }
        }
    });
    return result;
}

/*
* 获取项目url
* */
function getUrl(){
    var curWwwPath = window.document.location.href;
    var pathName =  window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0,pos);
    var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return (localhostPaht + projectName);
}

/*
* 时间格式化工具
* 把Long类型的1527672756454日期还原yyyy-MM-dd 00:00:00格式日期
*/
function datetimeFormat(longTypeDate){
    var dateTypeDate = "";
    var date = new Date();
    date.setTime(longTypeDate);
    dateTypeDate += date.getFullYear();   //年
    dateTypeDate += "-" + getMonth(date); //月
    dateTypeDate += "-" + getDay(date);   //日
    dateTypeDate += " " + getHours(date);   //时
    dateTypeDate += ":" + getMinutes(date);     //分
    dateTypeDate += ":" + getSeconds(date);     //分
    return dateTypeDate;
}
/*
 * 时间格式化工具
 * 把Long类型的1527672756454日期还原yyyy-MM-dd格式日期
 */
function dateFormat(longTypeDate){
    var dateTypeDate = "";
    var date = new Date();
    date.setTime(longTypeDate);
    dateTypeDate += date.getFullYear();   //年
    dateTypeDate += "-" + getMonth(date); //月
    dateTypeDate += "-" + getDay(date);   //日
    return dateTypeDate;
}
//返回 01-12 的月份值
function getMonth(date){
    var month = "";
    month = date.getMonth() + 1; //getMonth()得到的月份是0-11
    if(month<10){
        month = "0" + month;
    }
    return month;
}
//返回01-30的日期
function getDay(date){
    var day = "";
    day = date.getDate();
    if(day<10){
        day = "0" + day;
    }
    return day;
}
//小时
function getHours(date){
    var hours = "";
    hours = date.getHours();
    if(hours<10){
        hours = "0" + hours;
    }
    return hours;
}
//分
function getMinutes(date){
    var minute = "";
    minute = date.getMinutes();
    if(minute<10){
        minute = "0" + minute;
    }
    return minute;
}
//秒
function getSeconds(date){
    var second = "";
    second = date.getSeconds();
    if(second<10){
        second = "0" + second;
    }
    return second;
}

var user = getSessionAttribute("user");

function initHead() {
    $('#Carousel').carousel('cycle');
    console.log("user:"+ user);
    if(user != undefined && user != null){
        $("#login_button").addClass("display_none");
        $("#user_info").removeClass("display_none");
        $("#user_page").text(user.name);
        $("#user_page").append($("<b class=\"caret\"></b>"));
        $("#toIndex").attr("href",getUrl()+"/toIndex");
        $("#toForum").attr("href",getUrl()+"/toForum");
        $("#userMsg").attr("href",getUrl()+"/toUserMsg");
        $("#accountMsg").attr("href",getUrl()+"/toSetAccountMsg");
        $("#logout").attr("href",getUrl()+"/logout");
    }else{
        $("#user_info").addClass("display_none");
        $("#login_button").removeClass("display_none");

        $("#login").attr("href",getUrl()+"/toLogin_Register?type=login");
        $("#register").attr("href",getUrl()+"/toLogin_Register?type=register");
    }
    $("#index").attr("href",getUrl());
    $("#toProduct").attr("href",getUrl()+"/toProduct");
    $("#download_file").attr("href",getUrl()+"/getFolders")
    $("#baijialuntan").attr("href",getUrl()+"/toForum");
    $("#toPatent").attr("href",getUrl()+"/toPatent");
    $("#toQuality").attr("href",getUrl()+"/toQuality");
    $("#toOther").attr("href",getUrl()+"/toOther");
    $("#set-accountmsg").attr("style","cursor:pointer;");
    $("#set-personalmsg").attr("style","cursor:pointer;");
    $("#set-head").attr("style","cursor:pointer;");
    $("#myAuthority").attr("style","cursor:pointer;");

    $("#set-accountmsg").click(function () {
        window.location.href=getUrl()+"/toSetAccountMsg";
    });
    $("#set-personalmsg").click(function () {
        window.location.href=getUrl()+"/toSetPersonalMsg";
    });
    $("#set-head").click(function () {
        window.location.href=getUrl()+"/toSetHead";
    });
    $("#myAuthority").click(function () {
        alert("权限系统未开放");
    });
}