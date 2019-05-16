package com.dagu.controller;


import com.dagu.pojo.*;
import com.dagu.service.MailService;
import com.dagu.service.MsgService;
import com.dagu.utils.*;
import com.dagu.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MsgService msgService;


    @RequestMapping(value = "/toLogin_Register", produces = "text/html;charset=UTF-8;")
    public String toLogin_Register(@RequestParam(value = "type", required = false)String type,@RequestParam(value="url", required = false)String url, Model model) {

        System.out.println("toLogin_Register");

        model.addAttribute("type",type);
        model.addAttribute("url",url);
        return "/../../login_register";
    }
    @RequestMapping(value = "/register", produces = "text/html;charset=UTF-8;")
    @ResponseBody
    public String register(@RequestParam("uname") String uname,@RequestParam("email") String email,
                           @RequestParam("password") String password,HttpServletRequest request) {
        String url = request.getHeader("Referer");
        url = url.substring(0,url.lastIndexOf("/")+1);
        User user = new User();
        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();

       try{
           userService.checkUname(uname);
       }catch (Exception e){
           msg.setTips(e.getMessage());
           msg.setStatus(AjaxMessageUtil.FAIL);
           return JSONUtil.getString(msg);
       }
        user.setName(uname);

        try{
            userService.checkEmail(email);
        }catch (Exception e){
            msg.setTips(e.getMessage());
            msg.setStatus(AjaxMessageUtil.FAIL);
            return JSONUtil.getString(msg);
        }
        user.setEmail(email);

       if(password.length()<6 || password.length()>16){
           msg.setStatus(AjaxMessageUtil.FAIL);
           msg.setTips("密码格式错误");
           return JSONUtil.getString(msg);
       }else{
           user.setPassword(password);
       }
       user.setAuthority(Authority.customer);
       String id = UUID.randomUUID().toString();
       try{
           userService.register(url,id,user);
           String path = request.getServletContext().getRealPath("/WEB-INF/../imgs/touxiang.jpg");
           File f= new File(path);
           String newPath = request.getServletContext().getRealPath("/WEB-INF/../head_image/"+user.getName()+".jpg");
           FileUtil.saveFile(f,newPath,true);
       }catch (Exception e){
           msg.setStatus(AjaxMessageUtil.FAIL);
           return  JSONUtil.getString(msg);
       }
       msg.setStatus(AjaxMessageUtil.SUCCESS);

       return  JSONUtil.getString(msg);
    }
    @RequestMapping(value = "/active", produces = "text/html;charset=UTF-8;")
    public void active(@RequestParam("token") String token,HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getRequestURI();
        url = url.substring(0,url.lastIndexOf("/")+1);

        response.setContentType("text/html;charset=UTF-8");
        if (token != null) {
            try {
                userService.active(token);
            }catch (Exception e){
                response.getWriter().write("<script>window.alert('"+e.getMessage()+"')</script>");
                return;
            }
        }else {
            response.getWriter().write("<script>window.alert('URL錯誤')</script>");
            return;
        }
        response.getWriter().write("<script>window.alert('激活成功')</script>");
        response.getWriter().write ("<script>window.location ='"+url+"toLogin_Register?type=login'</script>");
    }
    @RequestMapping(value = "/login", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
    @ResponseBody
    public String Login(@RequestParam("uname") String uname,@RequestParam("password") String password, HttpServletRequest request) {

        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
        User user=null;
        try {
            user = userService.login(uname, password);
        }catch (Exception e){
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips(e.getMessage());
            e.printStackTrace();
            return JSONUtil.getString(msg);
        }

        if(user != null){
            request.getSession().setAttribute("user",user);
            msg.setStatus(AjaxMessageUtil.SUCCESS);
        }else{
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("账号或密码错误");
        }
        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/logout", produces = "text/html;charset=UTF-8",method = RequestMethod.GET)
    public String Logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "../../index";
    }

    @RequestMapping(value = "/checkUname", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
    @ResponseBody
    public String checkUname(@RequestParam("uname") String uname) {
        System.out.println("checkUname");
        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
        try{
            userService.checkUname(uname);
        }catch (Exception e){
            e.printStackTrace();
            msg.setTips(e.getMessage());
            msg.setStatus(AjaxMessageUtil.FAIL);
            return JSONUtil.getString(msg);
        }
        msg.setTips("用戶名可用");
        msg.setStatus(AjaxMessageUtil.SUCCESS);
        return JSONUtil.getString(msg);
    }
    @RequestMapping(value = "/checkEmail", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
    @ResponseBody
    public String checkEmail(@RequestParam("email") String email) {

        System.out.println("checkEmail");
        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();

        try{
            userService.checkEmail(email);
        }catch (Exception e){
            msg.setTips(e.getMessage());
            msg.setStatus(AjaxMessageUtil.FAIL);
            return JSONUtil.getString(msg);
        }
        msg.setTips("邮箱可用");
        msg.setStatus(AjaxMessageUtil.SUCCESS);

        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/checkPhone", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
    @ResponseBody
    public String checkPhone(@RequestParam("phone") String phone) {

        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();

        try{
            userService.checkPhone(phone);
        }catch (Exception e){
            msg.setTips(e.getMessage());
            msg.setStatus(AjaxMessageUtil.FAIL);
            return JSONUtil.getString(msg);
        }
        msg.setTips("号码可用");
        msg.setStatus(AjaxMessageUtil.SUCCESS);

        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/checkPassword", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
    @ResponseBody
    public String checkPassword(@RequestParam("password") String password) {
        System.out.println("checkPassword");
        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();

        if(password.length()<6){
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("密码过短，密码至少应有6位");
        }else if(password.length()>16){
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("密码过长，密码至多有16位");
        }else{
            msg.setStatus(AjaxMessageUtil.SUCCESS);
            msg.setTips("");
        }

        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/toUserMsg", produces = "text/html;charset=UTF-8;")
    public String toUserMsg(@RequestParam(value = "currentpage", required = false) String currentpages, HttpServletRequest request) {

        System.out.println("toUserMsg");
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            request.setAttribute("type","login");
            request.setAttribute("url","toUserMsg");
            return "/../../login_register";
        }
        List<SystemMsg> systemMsgs = msgService.getUnreadSystemMsgById(user.getUid());

        PageUtils pageUtils = new PageUtils();
        int currentpage = 1;
        int totalRows = msgService.getUnreadForumMsgTotalRows(user.getUid());
        pageUtils.setPageRecorders(15);
        int totalPages = (totalRows/pageUtils.getPageRecorders())+(totalRows%pageUtils.getPageRecorders() == 0 ? 0:1);
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
        if(totalPages != 0){
            page = msgService.getUnreadForumMsgPage(pageUtils,user.getUid());
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for(SystemMsg systemMsg :systemMsgs) {
            systemMsg.setTime(df.format(systemMsg.getSendtime()));
        }
        if(page.getList() != null) {
            List<ForumMsg> forumMsgs = (List<ForumMsg>) page.getList();
            for (ForumMsg f : forumMsgs) {
                f.setTime(df.format(f.getSendtime()));
            }
        }
        request.setAttribute("systemMsgs",systemMsgs);
        request.setAttribute("page", page);
        return "user-msg";
    }

    @RequestMapping(value = "/toSetHead", produces = "text/html;charset=UTF-8;")
    public String toSetHead(HttpServletRequest request) {
//        String agent = request.getHeader("user-agent");
//        System.out.println(agent);
//        if (agent.contains("Android")||agent.contains("iPhone")||agent.contains("iPad")) {
//            return "m/set-head";
//        }
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            request.setAttribute("type","login");
            request.setAttribute("url","toSetHead");
            return "/../../login_register";
        }
        return "set-head";
    }

    @RequestMapping(value = "/setHead", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(
            @RequestParam(value = "x") int x,@RequestParam(value = "y") int y,
            @RequestParam(value = "height") int height,@RequestParam(value = "width") int width,
            @RequestParam(value = "imgFile",required = false) MultipartFile imgFile,
            HttpServletRequest request) {
        System.out.println("upload");

        AjaxMessageUtil msg = new AjaxMessageUtil();
        msg.setStatus(AjaxMessageUtil.SUCCESS);
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("登录已失效");
            return JSONUtil.getString(msg);
        }

        if(imgFile == null){
            try {
                String path = request.getServletContext().getRealPath("/WEB-INF/../imgs/test.jpg");
                File f= new File(path);
                FileInputStream fis= new FileInputStream(f);
                imgFile = new MockMultipartFile("test.jpg","test.jpg","image/jpg",fis);
            } catch (Exception e) {
                e.printStackTrace();
                msg.setStatus(AjaxMessageUtil.FAIL);
                msg.setTips("服务器异常");
                return JSONUtil.getString(msg);
            }
        }

        try {
            // 接收图片
            if (!imgFile.isEmpty()) {
                // 保存图片
                String realPath = request.getServletContext().getRealPath("/");
                String resourcePath = "head_image"  + File.separator;
                String fileName = user.getName()+".jpg";
                File file = new File(realPath + resourcePath, fileName);
                FileUtils.copyInputStreamToFile(imgFile.getInputStream(), file);
                Image image = ImageIO.read(file);
                BufferedImage tag = new BufferedImage(385, 240, BufferedImage.TYPE_INT_RGB);
                //绘制改变尺寸后的图
                tag.getGraphics().drawImage(image, 0, 0,385, 240, null);
                //输出流
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(realPath + resourcePath+fileName));
                ImageIO.write(tag, "JPG",out);
                out.close();
                // 实现图片裁剪
                Thumbnails.of(file)
                        .sourceRegion(x, y, width, height)
                        .size(width, height)
                        .keepAspectRatio(false)
                        .toFile(file);
            } else {
                msg.setStatus(AjaxMessageUtil.FAIL);
                msg.setTips("图片不能为空");
                return JSONUtil.getString(msg);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("服务端异常");
            return JSONUtil.getString(msg);
        }

        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/toSetAccountMsg", produces = "text/html;charset=UTF-8;")
    public String toSetAccountMsg(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            request.setAttribute("type","login");
            request.setAttribute("url","toSetAccountMsg");
            return "/../../login_register";
        }
        return "set-accountmsg";
    }

    @RequestMapping(value = "/toSetPersonalMsg", produces = "text/html;charset=UTF-8;")
    public String toSetPersonalMsg(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            request.setAttribute("type","login");
            request.setAttribute("url","toSetPersonalMsg");
            return "/../../login_register";
        }
        UserMsg userMsg = userService.getUserMsgByUid(user.getUid());
        if(userMsg != null) {
            userMsg.setTime(DateUtils.toString(userMsg.getBirthday(), "yyyy-MM-dd"));
        }
        request.setAttribute("userMsg",userMsg);
        return "set-personalmsg";
    }

    @RequestMapping(value = "/setUser", produces = "text/html;charset=UTF-8;", method = RequestMethod.POST)
    @ResponseBody
    public String setUser(@RequestParam(value = "uname",required = false)String uname,@RequestParam(value = "password",required = false)String password,
                          @RequestParam(value = "oldPassword",required = false)String oldPassword,
                          @RequestParam(value = "email",required = false)String email,@RequestParam(value = "phone",required = false)String phone,
                          HttpServletRequest request){
        AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();

        User oldUser = (User) request.getSession().getAttribute("user");
        if(oldUser == null){
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("登录已失效");
            return JSONUtil.getString(msg);
        }
        User user = new User();
        if(uname != null) {
            try {
                userService.checkUname(uname);
            } catch (Exception e) {
                msg.setTips(e.getMessage());
                msg.setStatus(AjaxMessageUtil.FAIL);
                return JSONUtil.getString(msg);
            }
            user.setName(uname);
        }
        if(email != null) {
            try {
                userService.checkEmail(email);
            } catch (Exception e) {
                msg.setTips(e.getMessage());
                msg.setStatus(AjaxMessageUtil.FAIL);
                return JSONUtil.getString(msg);
            }
            user.setEmail(email);
        }
        if(password != null) {
            if(oldPassword == null || !oldPassword.equals(oldUser.getPassword())){
                msg.setTips("原密码有误");
                msg.setStatus(AjaxMessageUtil.FAIL);
                return JSONUtil.getString(msg);
            }
            if (password.length() < 6 || password.length() > 16) {
                msg.setStatus(AjaxMessageUtil.FAIL);
                msg.setTips("密码格式错误");
                return JSONUtil.getString(msg);
            } else {
                user.setPassword(password);
            }
        }
        if(phone != null) {
            try {
                userService.checkPhone(phone);
            } catch (Exception e) {
                msg.setTips(e.getMessage());
                msg.setStatus(AjaxMessageUtil.FAIL);
                return JSONUtil.getString(msg);
            }
            user.setEmail(phone);
        }
        user.setUid(oldUser.getUid());

        boolean b = userService.updateUser(user);
        if(b){
            if(uname != null){
                try {
                    String path = request.getServletContext().getRealPath("/WEB-INF/../head_image/"+oldUser.getName()+".jpg");
                    File f= new File(path);
                    String newPath = request.getServletContext().getRealPath("/WEB-INF/../head_image/"+uname+".jpg");
                    FileUtil.saveFile(f,newPath,true);
                    FileUtil.deleteAllFilesOfDir(f);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            user = userService.getUserById(oldUser.getUid());
            request.getSession().setAttribute("user",user);
            msg.setTips("修改成功");
            msg.setStatus(AjaxMessageUtil.SUCCESS);
        }else {
            msg.setTips("系统错误");
            msg.setStatus(AjaxMessageUtil.FAIL);
        }
        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/setPersonalMsg", produces = "text/html;charset=UTF-8;", method = RequestMethod.POST)
    @ResponseBody
    public String setPersonalMsg(@RequestParam(value = "name",required = false) String name,@RequestParam(value = "sex",required = false) String sex,
                                 @RequestParam(value = "birthday",required = false) String birthday,@RequestParam(value = "education",required = false) String education,
                                 @RequestParam(value = "job",required = false) String job,HttpServletRequest request) {
        AjaxMessageUtil msg = new AjaxMessageUtil();
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            msg.setStatus(AjaxMessageUtil.FAIL);
            msg.setTips("登录已失效");
            return JSONUtil.getString(msg);
        }
        UserMsg userMsg = new UserMsg();
        userMsg.setName(name);
        userMsg.setSex(sex);
        userMsg.setBirthday(DateUtils.getDate(birthday,"yyyy-MM-dd"));
        userMsg.setEducation(education);
        userMsg.setJob(job);
        userMsg.setUid(user.getUid());

       userService.addUserMsg(userMsg);
       msg.setStatus(AjaxMessageUtil.SUCCESS);

        return JSONUtil.getString(msg);
    }

    @RequestMapping(value = "/user/{uanme}", produces = "text/html;charset=UTF-8;", method = RequestMethod.GET)
    public String browseOtherUser(@PathVariable("uanme") String uname){

        return "/../../index";
        //return "/../../user-msg";
    }

}

