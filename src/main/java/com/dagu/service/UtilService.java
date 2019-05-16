package com.dagu.service;


import com.dagu.pojo.Verification;

import java.util.Date;

public interface UtilService {

    public int addVerification(Verification verification);

    public Verification getVerification(Verification v);

    public int delVerification(Verification v);

    public int delOvertimeVerification(Date time);
}
