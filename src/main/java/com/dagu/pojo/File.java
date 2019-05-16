package com.dagu.pojo;

import java.util.Date;

public class File {
	
	private int fid;
	private int ffid;
	private String fname;
	private Date lastTime;
	private int uploadMan;
	private String path;
	private int authority;
	
	
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public int getFfid() {
		return ffid;
	}
	public void setFfid(int ffid) {
		this.ffid = ffid;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public int getUploadMan() {
		return uploadMan;
	}
	public void setUploadMan(int uploadMan) {
		this.uploadMan = uploadMan;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "Flie [fid=" + fid + ", ffid=" + ffid + ", fname=" + fname + ", lasttime=" + lastTime
				+ ", uploadMan=" + uploadMan + ", path=" + path + "]";
	}


}
