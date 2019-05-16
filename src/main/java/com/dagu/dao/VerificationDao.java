package com.dagu.dao;

import com.dagu.pojo.Verification;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface VerificationDao {

    public int AddVerification(@Param("verification")Verification verification);

    public Verification selVerification(@Param("verification") Verification verification);

    public int delVerification(@Param("verification") Verification ve);

    public int delOvertimeVerification(@Param("time")Date time);
}
