package com.dagu.controller;


import com.dagu.pojo.ForumMsg;
import com.dagu.pojo.ReportMsg;
import com.dagu.pojo.SystemMsg;
import com.dagu.pojo.User;
import com.dagu.service.MsgService;
import com.dagu.service.UserService;
import com.dagu.utils.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Controller
public class MsgController {

    @Autowired
    private UserService userService;
    @Autowired
    private MsgService msgService;

    @RequestMapping(value = "/hasReadMsg", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
    @ResponseBody
    public String hasReadMsg(@RequestParam("id") int id,@RequestParam("type") String type,HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            request.setAttribute("type","login");
            request.setAttribute("url","toUserMsg");
            return "/../../login_register";
        }

        System.out.println("hasReadMsg");

        Boolean b = msgService.hasReadMsg(type, id, user.getUid());

        return b.toString();
    }

    @RequestMapping(value = "/newReportMsg", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
    @ResponseBody
    public String newReportMsg(@RequestParam("type") String type,@RequestParam("msg") String msg,@RequestParam("target") int target, HttpServletRequest request) {

        String url = request.getHeader("Referer");
        url = url.substring(0,url.lastIndexOf("/")+1);
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            request.setAttribute("type","login");
            request.setAttribute("url","toUserMsg");
            return "/../../login_register";
        }
        ReportMsg reportMsg = new ReportMsg();
        reportMsg.setMsg(msg);
        reportMsg.setTarget(target);
        reportMsg.setFrom(user.getUid());
        reportMsg.setType(type);
        Boolean b = msgService.newReportMsg(reportMsg, url);

        return b.toString();
    }
}

