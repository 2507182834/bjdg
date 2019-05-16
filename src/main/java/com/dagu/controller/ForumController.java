package com.dagu.controller;

import com.dagu.pojo.Forum;
import com.dagu.pojo.Remark;
import com.dagu.pojo.User;
import com.dagu.service.ForumService;
import com.dagu.service.UserService;
import com.dagu.utils.AjaxMessageUtil;
import com.dagu.utils.JSONUtil;
import com.dagu.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Controller
public class ForumController {

    @Autowired
    private ForumService forumService;

    @RequestMapping(value = "/toForum", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
    public String toForum(@RequestParam(value = "currentpage",required = false) String currentpages,@RequestParam(value = "orderBy",required = false) String orderBy,
                          @RequestParam(value = "type",required = false) String type,@RequestParam(value = "value",required = false) String value, HttpServletRequest request){
        System.out.println("toForum");

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            request.setAttribute("url", "toForum");
            return "/../../login_register";
        }
        System.out.println(orderBy + "    " + type);
        if (orderBy == null || orderBy.equals("")) {
            orderBy = "hot";
        }
        if (type == null || type.equals("")) {
            type = "desc";
        }

        PageUtils pageUtils = new PageUtils();
        int currentpage = 1;
        int totalRows = forumService.getTotalRowsFromForum(value);
        int totalPages = (totalRows / pageUtils.getPageRecorders()) + (totalRows % pageUtils.getPageRecorders() == 0 ? 0 : 1);
        if (!(currentpages == null || currentpages.equals(""))) {
            currentpage = Integer.parseInt(currentpages);
        }
        if (currentpage < 1) {
            currentpage = 1;
        } else if (currentpage > totalPages) {
            currentpage = totalPages;
        }
        pageUtils.setCurrentPage(currentpage);
        pageUtils.setTotalPages(totalPages);
        pageUtils.setTotalRows(totalRows);

        PageUtils page = forumService.getPage(pageUtils, orderBy, user.getUid(), type, value);

        request.setAttribute("page", page);
        request.setAttribute("type", type);
        request.setAttribute("orderBy", orderBy);
        request.setAttribute("value", value);
        return "forum";
    }

    @RequestMapping("/toNewForum")
    public String toNewForum(){
        System.out.println("toNewForum");
        return "new_forum";
    }

    @RequestMapping(value = "/newForum", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
    @ResponseBody
    public String newForum(@RequestParam("title") String title, @RequestParam("text") String text,
                           HttpServletRequest request){

        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
        String classPath = request.getServletContext().getRealPath("/");
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            msg.setTips("权限不足");
            msg.setStatus(AjaxMessageUtil.FAIL);
            return JSONUtil.getString(msg);
        }
        boolean b = false;

        try{
            b = forumService.NewForum(title,text,classPath,user);
        }catch (Exception e){
            msg.setTips(e.getMessage());
            msg.setStatus(AjaxMessageUtil.FAIL);
        }
        if(b){
            msg.setTips("主题发表成功");
            msg.setStatus(AjaxMessageUtil.SUCCESS);
        }else {
            msg.setTips("服务器错误");
            msg.setStatus(AjaxMessageUtil.FAIL);
        }

        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/getForumMsg", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
    public String getForumMsg(@RequestParam(value = "currentpage",required = false) String currentpages,
                              @RequestParam(value = "floor",required = false) String floor,
                              @RequestParam("fid") int fid, HttpServletRequest request){

        Forum forum = forumService.getForumById(fid);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        forum.setTime(df.format(forum.getCreateTime()));
        request.setAttribute("forum",forum);

        PageUtils pageUtils = new PageUtils();
        int currentpage = 1;
        int totalRows = forumService.getTotalRowsFromRemark(fid);
        pageUtils.setPageRecorders(20);
        int totalPages = (totalRows/pageUtils.getPageRecorders())+(totalRows%pageUtils.getPageRecorders() == 0 ? 0:1);
        System.out.println(totalRows+"    "+totalPages);
        if (!(currentpages == null ||currentpages .equals(""))){
            currentpage = Integer.parseInt(currentpages);
        }
        if(currentpage < 1){
            currentpage = 1;
        }else if(currentpage > totalPages){
            currentpage = totalPages;
        }
        pageUtils.setCurrentPage(currentpage);
        pageUtils.setTotalPages(totalPages);
        pageUtils.setTotalRows(totalRows);
        PageUtils page = pageUtils;
        if (totalPages != 0) {
            page = forumService.getRemarkPage(pageUtils, fid);
        }
        int f = 0;
        if(!(floor == null || floor.equals(""))){
            f = Integer.parseInt(floor);
        }
        request.setAttribute("page",page);
        request.setAttribute("floor",f);
        return "forummsg";
    }

    @RequestMapping(value = "/newRemark", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
    @ResponseBody
    public String newRemark(@RequestParam("fid") int fid,@RequestParam("text") String text,
                           HttpServletRequest request){
        String url = request.getHeader("Referer");
        url = url.substring(0,url.lastIndexOf("/")+1);
        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            msg.setTips("权限不足");
            msg.setStatus(AjaxMessageUtil.FAIL);
            return JSONUtil.getString(msg);
        }
        Remark remark = null;
        try{
            remark = forumService.NewRemark(fid,text,user,url);
        }catch (Exception e){
            msg.setTips(e.getMessage());
            msg.setStatus(AjaxMessageUtil.FAIL);
        }
        if(remark!= null){
            msg.setTips(String.valueOf(remark.getFloor()));
            msg.setStatus(AjaxMessageUtil.SUCCESS);
        }else {
            msg.setTips("服务器错误");
            msg.setStatus(AjaxMessageUtil.FAIL);
        }

        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/getRemarkNum", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
    @ResponseBody
    public String newRemark(@RequestParam("fid") int fid,@RequestParam("floor") int floor, HttpServletRequest request){

        Remark remark = forumService.getRemarkByFidAndFloor(fid,floor);
        return String.valueOf(forumService.getRemarkNumById(fid,remark.getId()));
    }
}
