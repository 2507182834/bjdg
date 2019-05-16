package com.dagu.service;

import com.dagu.pojo.Forum;
import com.dagu.pojo.Remark;
import com.dagu.pojo.User;
import com.dagu.utils.PageUtils;

public interface ForumService {

    public boolean NewForum(String title, String text, String classPath, User user);

    public PageUtils getPage(PageUtils pageUtils, String orderBy,int author, String type, String value);

    public int getTotalRowsFromForum(String value);
    public int getTotalRowsFromRemark(int fid);

    public Forum getForumById(int fid);

    public PageUtils getRemarkPage(PageUtils pageUtils, int fid);

    public Remark NewRemark(int fid, String text, User user,String url);

    public int getRemarkNumById(int fid, int id);

    public Remark getRemarkByFidAndFloor(int fid, int floor);

    public boolean delForumById(int fid);

    public boolean delRemarkById(int id);
}
