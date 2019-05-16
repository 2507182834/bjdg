package com.dagu.service.impl;

import com.dagu.dao.UserDao;
import com.dagu.dao.UserMsgDao;
import com.dagu.pojo.MailModel;
import com.dagu.pojo.User;
import com.dagu.pojo.UserMsg;
import com.dagu.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
public class UserServiceImpl implements com.dagu.service.UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserMsgDao userMsgDao;

    @Override
    public boolean addUserMsg(UserMsg userMsg) {
        if(userMsgDao.getUserMsgByUid(userMsg.getUid()) != null){
            userMsgDao.updateUserMsg(userMsg);
        }else{
            userMsgDao.addUserMsg(userMsg);
        }
        return true;
    }

    @Transactional
    public boolean register(String url,String id,User user) {

        userDao.addUserToTemp(id,user);

        MailModel mailModel = new MailModel();
        mailModel.setFromAddress("2507182834@qq.com");
        mailModel.setToAddresses(user.getEmail());
        mailModel.setSubject("百佳大谷-----用戶注冊激活郵件");
        StringBuffer sb = new StringBuffer();
        sb.append("您好！<br/>");
        sb.append("<br/><br/>    感谢您注册帐户。为激活您的帐户，请单击下列链接。<br/>");
        sb.append(url+"active?token="+id+"<br/>");
        sb.append("如果单击链接没有反应，请将链接复制到浏览器窗口中，或直接输入链接。<br/>" +
                "<br/>" +
                "致敬！<br/>" +
                "<br/>" +
                "百佳大谷团队");
        mailModel.setContent(sb.toString());
        try{
            mailService.sendAttachMail(mailModel);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    public User login(String uname, String password) {
        if(userDao.getUserFromTemp(uname, password) != null){
            throw new RuntimeException("账户邮箱还没有激活，请激活再登陆。");
        }
        return userDao.getUser(uname,password);
    }

    @Transactional
    public Boolean active(String token) {

        User u = userDao.getUserByIdFromTemp(token);

        if(u!=null){
            userDao.addUser(u);
            userDao.delUserFromTemp(token);
        }else{
                throw new RuntimeException("鏈接已過期");
        }


        return true;
    }

    @Override
    public boolean updateUser(User user) {

        userDao.updateUserById(user);
        return true;
    }

    public boolean checkUname(String uname) {

        String regex = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";
        if(uname.length()<4||uname.length()>16){
            throw new RuntimeException("请输入一个4-16个字符的用户名");
        }else if (!uname.matches(regex)){
            throw new RuntimeException("请检查用户名是否含有特殊符号");
        }
        if(userDao.getCountByUanme(uname) > 0 || userDao.getCountByUanmeFromTemp(uname) > 0){
            throw new RuntimeException("用戶名已存在");
        }

        return true;
    }

    public boolean checkEmail(String email) {

        String regex = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";
        if(!email.matches(regex)){
            throw new RuntimeException("请检查邮箱格式是否正确");
        }
        if(userDao.getCountByEmail(email) > 0 || userDao.getCountByEmailFromTemp(email) > 0){
            throw new RuntimeException("邮箱已注冊");
        }

        return true;
    }

    @Override
    public boolean checkPhone(String phone) {

        String regex = "^[1][3,4,5,7,8][0-9]{9}$";
        if(!phone.matches(regex)){
            throw new RuntimeException("请检查号码格式是否正确");
        }
        if(userDao.getCountByPhone(phone) > 0 || userDao.getCountByPhoneFromTemp(phone) > 0){
            throw new RuntimeException("号码已注冊");
        }

        return true;
    }

    @Override
    public User getUserById(int uid) {
        return userDao.getUserById(uid);
    }

    @Override
    public UserMsg getUserMsgByUid(int uid) {
        return userMsgDao.getUserMsgByUid(uid);
    }


}
