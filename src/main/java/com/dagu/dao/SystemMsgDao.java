package com.dagu.dao;
import com.dagu.pojo.SystemMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemMsgDao {

    /**
     *
     * @param id 用户id
     * @return 所有系统发给 【指定id用户】 和系统推送给 【所有用户】 的消息
     */
    public List<SystemMsg> getUnreadSystemMsgById(@Param("id") int id);

    public int updateIsreadById(@Param("id") int id, @Param("uid") String uid);

    public int addSystemMsg(@Param("title") String title, @Param("text") String text);

}
