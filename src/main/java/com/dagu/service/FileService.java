package com.dagu.service;

import org.springframework.stereotype.Service;

import com.dagu.pojo.File;
import com.dagu.pojo.User;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;


public interface FileService {

	public File getFileByFid(int fid);

	public List<File> getFiles(int ffid,int authority);

    public boolean saveFile(File file, CommonsMultipartFile f, String classPath) throws Exception;

    public boolean UpdateFile(File file, String classPath) throws Exception;

    public boolean DeleteFile(int fid, String classPath) throws Exception;
}
