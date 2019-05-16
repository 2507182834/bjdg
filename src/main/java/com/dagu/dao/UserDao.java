package com.dagu.dao;

import com.dagu.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {

    public int addUserToTemp(@Param("id")String id,@Param("user") User user);
    public int addUser(@Param("user") User user);

    public int getCountByUanme(@Param("uname") String uname);
    public int getCountByUanmeFromTemp(@Param("uname") String uname);

    public int getCountByEmail(@Param("email") String email);
    public int getCountByEmailFromTemp(@Param("email") String email);


    public User getUserFromTemp(@Param("uname") String uname, @Param("password") String password);
    public User getUser(@Param("uname") String uname, @Param("password") String password);

    public User getUserByIdFromTemp(@Param("id") String token);

    public int delUserFromTemp(@Param("id")String token);

    public User getUserById(@Param("uid") int uid);

    public int getCountByPhone(@Param("phone") String phone);
    public int getCountByPhoneFromTemp(@Param("phone") String phone);

    public void updateUserById(@Param("user") User user);
}
