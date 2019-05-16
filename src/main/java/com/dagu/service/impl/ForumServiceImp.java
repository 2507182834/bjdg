package com.dagu.service.impl;

import com.dagu.dao.ForumDao;
import com.dagu.dao.ForumMsgDao;
import com.dagu.dao.UserDao;
import com.dagu.pojo.Forum;
import com.dagu.pojo.ForumMsg;
import com.dagu.pojo.Remark;
import com.dagu.pojo.User;
import com.dagu.service.ForumService;
import com.dagu.utils.FileUtil;
import com.dagu.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ForumServiceImp implements ForumService {

    @Autowired
    private ForumDao forumDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ForumMsgDao forumMsgDao;
    @Override
    @Transactional
    public boolean NewForum(String title, String text, String classPath, User user) {
        classPath = classPath.replace("\\","/");
        String regex = "src=\"/bjdg/UEditor/jsp/upload/(?:image|video)/[0-9]{8}/[0-9]{19}.\\w+" ;
        Pattern pattern = Pattern.compile(regex) ;
        Matcher matcher = pattern.matcher(text) ;
        while(matcher.find()){
            String os = matcher.group(0);
            String oPath = classPath+os.substring(os.lastIndexOf("/UEditor/")+1);
            File oFile = new File(oPath);
            System.out.println(oPath);
            try {
                String nPath = classPath+os.substring(os.lastIndexOf("/upload/")+1);
                System.out.println(nPath);
                FileUtil.saveFile(oFile,nPath,true);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
            String ns = os.replace("/UEditor/jsp/", "/");
            text = text.replace(os,ns);
        }
        System.out.println(classPath+"UEditor/jsp/upload");
        try {
            FileUtil.deleteAllFilesOfDir(new File(classPath+"UEditor/jsp/upload"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if(!e.getMessage().equals("指定文件不存在"))
                throw new RuntimeException(e);
        }
        Forum forum = new Forum();
        forum.setTitle(title);
        forum.setContent(text);
        forum.setAuthor(user.getUid());
        int i = forumDao.addForum(forum);
        return i>0;
    }

    @Override
    @Transactional
    public Remark NewRemark(int fid, String text, User user,String url) {
        Remark remark = new Remark();

        remark.setFid(fid);
        remark.setContent(text);
        int max = forumDao.getMaxRemarkFloor(fid);
        remark.setFloor(max+1);
        remark.setAuthor(user.getUid());

        int i = forumDao.addRemark(remark);
        remark = forumDao.getRemarkByFidAndFloor(fid,max+1);
        int c = forumDao.getRemarkNumById(remark.getFid(),remark.getId());
        int currentPage = c/20+1;
        int floor = remark.getFloor();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Forum forum = forumDao.getForumById(fid);

        ForumMsg forumMsg = new ForumMsg();
        forumMsg.setTitle("主题新评论通知");
        forumMsg.setMsg("<p style=\"white-space: normal; text-align: left;\">尊敬的"+forum.getAuthorName()+":</p><p style=\"white-space: normal;\">" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+user.getName()+"在"+df.format(new Date())+"评论了您发表的主题"+"<a style='color: #0094ff' href='"+url
                +"getForumMsg?fid="+fid+"'>《"+forum.getTitle()+
                "》</a>"+"，点击<a style=\"color:#0094ff\" href=\""+url+"getForumMsg?fid="+fid+"&currentpage="+currentPage+"&floor="+floor+"\">查看</a>前往查看详情。</p>" +
                "<p style=\"white-space: normal; text-align: right;\">百佳大谷团队</p>" +
                "<p style=\"white-space: normal; text-align: right;\">"+df.format(new Date())+"</p><p><br/></p>");
        forumMsg.setTo(forum.getAuthor());

        forumMsgDao.addForumMsg(forumMsg);
        if(i>0) {
            return remark;
        }else{
           return null;
        }
    }

    @Override
    public int getRemarkNumById(int fid, int id) {
        return forumDao.getRemarkNumById(fid,id);
    }

    @Override
    public Remark getRemarkByFidAndFloor(int fid, int floor) {
        return forumDao.getRemarkByFidAndFloor(fid,floor);
    }

    @Transactional
    @Override
    public boolean delForumById(int fid) {

        forumDao.delRemarkByFid(fid);
       int i = forumDao.delForumById(fid);
        return i>0;
    }

    @Override
    public boolean delRemarkById(int id) {

        int i = forumDao.delRemarkById(id);
        return i>0;
    }

    @Override
    public PageUtils getPage(PageUtils pageUtils,String orderBy, int author,String type, String value){

        pageUtils.setPageStartRow((pageUtils.getCurrentPage()-1)*pageUtils.getPageRecorders());
        if(pageUtils.getPageStartRow()<0){
            pageUtils.setPageStartRow(0);
        }
        pageUtils.setPageEndRow(pageUtils.getCurrentPage()*pageUtils.getPageRecorders()-1);
        if(orderBy.equals("hot")){
            List<Forum> list = forumDao.getForumsOrderByHot(pageUtils.getPageStartRow(),pageUtils.getPageRecorders(), type, value);
            pageUtils.setList(list);
        }else if(orderBy.equals("createtime")){
            List<Forum> list = forumDao.getForumsOrderByCreateTime(pageUtils.getPageStartRow(),pageUtils.getPageRecorders(), type, value);
            pageUtils.setList(list);
        }else{
            int totalRows = forumDao.getTotalRowsByAuthorFromForum(author, value);
            int totalPages = (totalRows/pageUtils.getPageRecorders())+(totalRows%pageUtils.getPageRecorders() == 0 ? 0:1);
            List<Forum> list = forumDao.getForumsByAuthor(pageUtils.getPageStartRow(),pageUtils.getPageRecorders(),author, type, value);
            pageUtils.setTotalPages(totalPages);
            pageUtils.setTotalRows(totalRows);
            pageUtils.setList(list);
        }


        pageUtils.setHasNextPage(pageUtils.getTotalPages()>pageUtils.getCurrentPage());
        pageUtils.setHasPreviousPage(pageUtils.getCurrentPage()>1);

        return pageUtils;
    }

    @Override
    public PageUtils getRemarkPage(PageUtils pageUtils,int fid) {

        pageUtils.setPageStartRow((pageUtils.getCurrentPage()-1)*pageUtils.getPageRecorders());
        pageUtils.setPageEndRow(pageUtils.getCurrentPage()*pageUtils.getPageRecorders()-1);
        List<Remark> list = forumDao.getRemarksByFid(pageUtils.getPageStartRow(),pageUtils.getPageRecorders(), fid);

        pageUtils.setList(list);


        pageUtils.setHasNextPage(pageUtils.getTotalPages()>pageUtils.getCurrentPage());
        pageUtils.setHasPreviousPage(pageUtils.getCurrentPage()>1);

        return pageUtils;
    }

    @Override
    public int getTotalRowsFromForum(String value) {
        return forumDao.getTotalRowsFromForum(value);
    }

    @Override
    public int getTotalRowsFromRemark(int fid) {
        return forumDao.getTotalRowsFromRemark(fid);
    }
    @Override
    public Forum getForumById(int fid) {
        Forum forum= forumDao.getForumById(fid);
        return forum;
    }
}
