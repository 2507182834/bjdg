package com.dagu.dao;

import com.dagu.pojo.Forum;
import com.dagu.pojo.Remark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumDao {

    public int addForum(@Param("forum") Forum forum);

    public List<Forum> getForumsOrderByHot(@Param("pageStartRow")int pageStartRow, @Param("pageRecorders")int pageRecorders, @Param("type") String type,@Param("value") String value);
    public List<Forum> getForumsOrderByCreateTime(@Param("pageStartRow")int pageStartRow, @Param("pageRecorders")int pageRecorders, @Param("type") String type,@Param("value") String value);
    public int getTotalRowsFromForum(@Param("value") String value);
    public int getTotalRowsFromRemark(@Param("fid") int fid);

    public Forum getForumById(@Param("fid")int fid);

    public List<Remark> getRemarksByFid(@Param("pageStartRow")int pageStartRow, @Param("pageRecorders")int pageRecorders,@Param("fid") int fid);

    public int getMaxRemarkFloor(@Param("fid") int fid);

    public int addRemark(@Param("remark") Remark remark);

    public List<Forum> getForumsByAuthor(@Param("pageStartRow")int pageStartRow, @Param("pageRecorders")int pageRecorders, @Param("author") int author, @Param("type") String type, @Param("value") String value);

    public int getTotalRowsByAuthorFromForum(@Param("author") int author,@Param("value") String value);

    public Remark getRemarkById(@Param("id") int id);

    public int getRemarkNumById(@Param("fid") int fid,@Param("id") int id);

    public Remark getRemarkByFidAndFloor(@Param("fid")int fid, @Param("floor")int floor);

    public int delForumById(@Param("fid")int fid);

    public int delRemarkByFid(@Param("fid")int fid);

    public int delRemarkById(@Param("id")int id);
}
