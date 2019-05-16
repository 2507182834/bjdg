package com.dagu.dao;

import com.dagu.pojo.ForumMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumMsgDao {

    public int getUnreadForumMsgTotalRows(@Param("id") int id);

    public List<ForumMsg> getUnreadForumMsgsByid(@Param("pageStartRow") int pageStartRow,@Param("pageRecorders") int pageRecorders,@Param("id") int uid);

    public int updateIsreadById(@Param("id") int id);

    public void addForumMsg(@Param("forumMsg") ForumMsg forumMsg);
}
