package com.dagu.service.impl;

import com.dagu.dao.*;
import com.dagu.pojo.*;
import com.dagu.service.MsgService;
import com.dagu.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MsgServiceImpl implements MsgService {

    @Autowired
    private SystemMsgDao systemMsgDao;
    @Autowired
    private ForumMsgDao forumMsgDao;
    @Autowired
    private ReportMsgDao reportMsgDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ForumDao forumDao;

    @Override
    public List<SystemMsg> getUnreadSystemMsgById(int id) {
        List<SystemMsg> list = systemMsgDao.getUnreadSystemMsgById(id);
        for (SystemMsg s:list) {
            String readnames = s.getReadnames();
            s.setIsread(false);
            if(!(readnames==null || readnames.equals(""))){
                String[] names = readnames.split(",");
                for (String name: names) {
                    if((id+"").equals(name)){
                        s.setIsread(true);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public int getUnreadForumMsgTotalRows(int id) {
        return forumMsgDao.getUnreadForumMsgTotalRows(id);
    }

    @Override
    public PageUtils getUnreadForumMsgPage(PageUtils pageUtils, int uid) {

        pageUtils.setPageStartRow((pageUtils.getCurrentPage()-1)*pageUtils.getPageRecorders());
        pageUtils.setPageEndRow(pageUtils.getCurrentPage()*pageUtils.getPageRecorders()-1);

        System.out.println(pageUtils.getCurrentPage() +"---"+ pageUtils.getPageStartRow());
        List<ForumMsg> list = forumMsgDao.getUnreadForumMsgsByid(pageUtils.getPageStartRow(),pageUtils.getPageRecorders(), uid);

        pageUtils.setList(list);


        pageUtils.setHasNextPage(pageUtils.getTotalPages()>pageUtils.getCurrentPage());
        pageUtils.setHasPreviousPage(pageUtils.getCurrentPage()>1);

        return pageUtils;
    }

    @Override
    public Boolean hasReadMsg(String type, int id, int uid) {
        int i = 0;
        if (type.equals("forummsg")){
            i = forumMsgDao.updateIsreadById(id);
        }else{
            i = systemMsgDao.updateIsreadById(id, uid+",");
        }
        return i==1;
    }

    @Override
    public boolean addSystemMsg(String title, String text) {


        return systemMsgDao.addSystemMsg(title,text)>0;
    }

    @Override
    public Boolean newReportMsg(ReportMsg reportMsg, String url) {

        User user = userDao.getUserById(reportMsg.getFrom());
        String a = "";
        if(!reportMsg.getType().equals("主题")){
            Remark remark = forumDao.getRemarkById(reportMsg.getTarget());
            User user1 = userDao.getUserById(remark.getAuthor());
            int c = forumDao.getRemarkNumById(remark.getFid(),remark.getId());
            int currentPage = c/20+1;
            int floor = remark.getFloor();
            a = "<a style='color: #0094ff' href='"+url+"getForumMsg?fid="+remark.getFid()+"&currentpage="+currentPage+"&floor="+floor+"'>"+user1.getName()+"的评论</a>";
        }else{
            Forum forum = forumDao.getForumById(reportMsg.getTarget());
            a = "<a style='color: #0094ff' href='"+url+"getForumMsg?fid="+reportMsg.getTarget()+"'>"+forum.getAuthorName()+"发表的主题《"+forum.getTitle()+"》</a>";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        reportMsg.setTime(df.format(new Date()));
        String msg = "<p>来自"+user.getName()+"的举报：</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; " +
                "我认为，"+a+"存在违规行为,具体涉及<span style='color: #0094ff'>"+reportMsg.getMsg()+"</span>,敬请审查筛选。<br/></p><p><br/></p>" +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp" +
                ";&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+reportMsg.getTime()+"<br/></p>";
        reportMsg.setMsg(msg);
        int i = reportMsgDao.addReportMsg(reportMsg);
        return i==1;
    }

    @Override
    public List<ReportMsg> getAllReportMsg() {
        List<ReportMsg> list = reportMsgDao.getAllReportMsg();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for(ReportMsg reportMsg : list) {
            reportMsg.setTime(df.format(reportMsg.getReporttime()));
        }
        return list;
    }

    @Override
    public boolean hasSolveReportMsg(int id) {

        int i = reportMsgDao.hasSolveReportMsg(id);
        return i == 1;
    }
}
