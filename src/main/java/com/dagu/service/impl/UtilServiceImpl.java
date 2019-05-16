package com.dagu.service.impl;

import com.dagu.dao.VerificationDao;
import com.dagu.pojo.Verification;
import com.dagu.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UtilServiceImpl implements UtilService {

	@Autowired
	private VerificationDao verificationDao;

	@Override
	public int addVerification(Verification verification) {

		return verificationDao.AddVerification(verification);
	}

	@Override
	public Verification getVerification(Verification v) {
		return verificationDao.selVerification(v);
	}

	@Override
	public int delVerification(Verification v) {
		return verificationDao.delVerification(v);
	}

	@Override
	public int delOvertimeVerification(Date time) {

		return verificationDao.delOvertimeVerification(time);
	}
}
