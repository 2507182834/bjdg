package com.dagu.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Folder {
	private int fid;
	private int ffid;
	private String fname;
	private Date lastTime;
	private Date createTime;
	private User creater ;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public User getCreater() {
		return creater;
	}
	public void setCreater(User creater) {
		this.creater = creater;
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
		return "Folder [fid=" + fid + ", ffid=" + ffid + ", fname=" + fname + ", lastTime=" + lastTime + ", createTime="
				+ createTime + ", creater=" + creater +"]";
	}
	
	
}
