package com.dagu.controller;


import com.dagu.dao.ForumDao;
import com.dagu.pojo.ReportMsg;
import com.dagu.pojo.User;
import com.dagu.service.ForumService;
import com.dagu.service.MsgService;
import com.dagu.utils.AjaxMessageUtil;
import com.dagu.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private MsgService msgService;
    @Autowired
    private ForumService forumService;

    @RequestMapping(value = "/admin", produces = "text/html;charset=UTF-8;", method = RequestMethod.GET)
    public String toAdmin(HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if(user != null){
            if(user.getAuthority()>0){
                return "/../../index";
            }
        }else{
            request.setAttribute("type","login");
            request.setAttribute("url","admin");
            return "/../../login_register";
        }

       List<ReportMsg> reportMsgs = msgService.getAllReportMsg();
        request.setAttribute("reportMsgs",reportMsgs);
        return "admin";
    }

    @RequestMapping(value = "/publishSystemMsg", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
    @ResponseBody
    public String publishSystemMsg(@RequestParam("text") String text,@RequestParam("title") String title, HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if(user != null){
            if(user.getAuthority()>0){
                return "/../../index";
            }
        }else{
            request.setAttribute("type","login");
            request.setAttribute("url","admin");
            return "/../../login_register";
        }

        boolean b = msgService.addSystemMsg(title,text);
        AjaxMessageUtil msg = new AjaxMessageUtil();
        if(b){
            msg.setStatus(AjaxMessageUtil.SUCCESS);
        }else {
            msg.setStatus(AjaxMessageUtil.FAIL);
        }

        return JSONUtil.getString(msg);
    }


    @RequestMapping(value = "/solveReport", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
    @ResponseBody
    public String solveReport(@RequestParam(value = "target", required = false) String target,@RequestParam(value = "type", required = false) String type,
                              @RequestParam("id") int id,HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if(user != null){
            if(user.getAuthority()>0){
                return "/../../index";
            }
        }else{
            request.setAttribute("type","login");
            request.setAttribute("url","admin");
            return "/../../login_register";
        }

        if(type != null) {
            if (type.equals("主题")) {
                forumService.delForumById(Integer.parseInt(target));
            } else if (type.equals("评论")) {
                forumService.delRemarkById(Integer.parseInt(target));
            }
        }
        msgService.hasSolveReportMsg(id);
        return "true";
    }
}
