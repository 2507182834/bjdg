package com.dagu.service;

import com.dagu.pojo.ReportMsg;
import com.dagu.pojo.SystemMsg;
import com.dagu.utils.PageUtils;

import java.util.List;

public interface MsgService {

    /**
     *
     * * @param id 用户id
     * @return 所有系统发给 【指定id用户】 和系统推送给 【所有用户】 的消息
     */
    public List<SystemMsg> getUnreadSystemMsgById(int id);

    /**
     * @param id 用户id
     * @return 用户消息总行数
     */
    public  int getUnreadForumMsgTotalRows(int id);

    /**
     *
     * @param pageUtils
     * @param uid
     * @return
     */
    public PageUtils getUnreadForumMsgPage(PageUtils pageUtils, int uid);

    public Boolean hasReadMsg(String type, int id, int uid);

    public boolean addSystemMsg(String title, String text);

    public Boolean newReportMsg(ReportMsg reportMsg, String url);

    public List<ReportMsg> getAllReportMsg();

    public boolean hasSolveReportMsg(int id);
}
