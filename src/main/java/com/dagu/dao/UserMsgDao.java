package com.dagu.dao;

import com.dagu.pojo.UserMsg;
import org.apache.ibatis.annotations.Param;

public interface UserMsgDao {

    public int addUserMsg(@Param("userMsg") UserMsg userMsg);

    public UserMsg getUserMsgByUid(@Param("uid") int uid);

    public void updateUserMsg(@Param("userMsg") UserMsg userMsg);
}
