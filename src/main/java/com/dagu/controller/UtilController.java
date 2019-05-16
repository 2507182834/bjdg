package com.dagu.controller;

import com.dagu.pojo.MailModel;
import com.dagu.pojo.User;
import com.dagu.pojo.Verification;
import com.dagu.service.MailService;
import com.dagu.service.UserService;
import com.dagu.service.UtilService;
import com.dagu.utils.AjaxMessageUtil;
import com.dagu.utils.JSONUtil;
import com.dagu.utils.VerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

@Controller
public class UtilController {

    @Autowired
    private UtilService utilService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/getSessionAttribute", produces = "text/html;charset=UTF-8;", method = RequestMethod.POST)
    @ResponseBody
    public String getSessionAttribute(@RequestParam("value") String value, HttpServletRequest request) {
        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();

        if(value != null && !value.equals("")){
           Object o = request.getSession().getAttribute(value);
           if(o != null) {
               msg.setData(JSONUtil.getString(o));
               msg.setStatus(AjaxMessageUtil.SUCCESS);
           }else{
               msg.setStatus(AjaxMessageUtil.FAIL);
               msg.setTips("The Session Attribute return NULL");
           }
        }else {
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("The Request Param can not be NULL");
        }
        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/getVerificationCode", produces = "text/html;charset=UTF-8;", method = RequestMethod.GET)
    public void getVerificationCode(HttpServletRequest request, HttpServletResponse response){
        VerificationUtil verificationUtil = new VerificationUtil();

        BufferedImage image = verificationUtil.getImage();
        request.getSession().setAttribute("VerificationCode",verificationUtil.getText());

        try {
            VerificationUtil.outPut(image,response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/sendVerificationCode", produces = "text/html;charset=UTF-8;", method = RequestMethod.POST)
    @ResponseBody
    public String sendVerificationCode(@RequestParam("type") String type, @RequestParam("account") String account,
                                       HttpServletRequest request){
        AjaxMessageUtil msg = new AjaxMessageUtil();
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("登录已失效");
            return JSONUtil.getString(msg);
        }
        String Verification = "";
        for(int i=0; i<6; i++){
            Verification +=(int)(Math.random()*10);
        }
        Verification verification = new Verification();
        switch (type){
            case "email":
                MailModel mailModel = new MailModel();
                mailModel.setFromAddress("2507182834@qq.com");
                mailModel.setToAddresses(account);
                mailModel.setSubject("百佳大谷-----邮箱验证码");
                StringBuffer sb = new StringBuffer();
                sb.append("您好！<br/>");
                sb.append("<br/><br/>    尊敬的用户，您的【验证码】为:"+Verification+"<br/>");
                sb.append("该验证码有效事件为十分钟，请尽快使用。<br/>" +
                        "<br/>" +
                        "致敬！<br/>" +
                        "<br/>" +
                        "百佳大谷团队");
                mailModel.setContent(sb.toString());
                try{
                    mailService.sendAttachMail(mailModel);
                }catch(Exception e){
                    throw new RuntimeException(e.getMessage());
                }
                verification.setTarget(user.getUid());
                verification.setType("email");
                verification.setTips(account);
                break;
            case "phone":
                msg.setStatus(AjaxMessageUtil.FAIL);
                msg.setTips("系统错误");
                return JSONUtil.getString(msg);
//                break;
        }

        utilService.delVerification(verification);
        verification.setVerification(Verification);
        int i = utilService.addVerification(verification);
        if(i==1){
            msg.setStatus(AjaxMessageUtil.SUCCESS);
        }else{
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("系统错误");
        }
        return JSONUtil.getString(msg);
    }


    @RequestMapping(value = "/checkedVerificationCode", produces = "text/html;charset=UTF-8;", method = RequestMethod.POST)
    @ResponseBody

    public String checkedVerificationCode(@RequestParam("account") String account, @RequestParam("verification") String verification,
                                          @RequestParam("type") String type,HttpServletRequest request){
        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("登录已失效");
            return JSONUtil.getString(msg);
        }

        Verification v = new Verification();
        v.setTarget(user.getUid());
        v.setVerification(verification);
        v.setType(type);
        v.setTips(account);

       v = utilService.getVerification(v);

       if(v != null){
           Date d = new Date();
           System.out.println(v.getSendtime());
           System.out.println(d);
           System.out.println(d.getTime()-v.getSendtime().getTime());
           if((d.getTime()-v.getSendtime().getTime())<10*60*1000){
               user = userService.getUserById(user.getUid());
               user.setEmail(account);
               userService.updateUser(user);
               utilService.delVerification(v);
               request.getSession().setAttribute("user",JSONUtil.getString(user));
               request.getSession().setAttribute("userProject",user);
           }else{
               utilService.delOvertimeVerification(new Date(d.getTime()-10*60*1000));
               msg.setTips("验证码已过期");
               msg.setStatus(AjaxMessageUtil.FAIL);
           }
       }else{
           msg.setTips("输入验证码错误");
           msg.setStatus(AjaxMessageUtil.FAIL);
       }

        return JSONUtil.getString(msg);
    }
}
