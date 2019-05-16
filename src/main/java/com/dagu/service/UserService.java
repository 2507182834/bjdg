package com.dagu.service;

import com.dagu.pojo.User;
import com.dagu.pojo.UserMsg;

public interface UserService {

    public boolean addUserMsg(UserMsg userMsg);

    public boolean register(String url, String id, User user);

    public boolean checkUname(String uname);

    public boolean checkEmail(String email);

    public User login(String uname,String password);

    public Boolean active(String token);

    public boolean updateUser(User user);

    boolean checkPhone(String phone);

    public User getUserById(int uid);

    public UserMsg getUserMsgByUid(int uid);
}
