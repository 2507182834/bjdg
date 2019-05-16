package com.dagu.dao;

import com.dagu.pojo.File;
import com.dagu.pojo.ReportMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportMsgDao {

    public int addReportMsg(@Param("reportMsg")ReportMsg reportMsg);
    public List<ReportMsg> getAllReportMsg();

    public int hasSolveReportMsg(@Param("id") int id);
}
