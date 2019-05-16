package com.dagu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dagu.pojo.File;
import com.dagu.pojo.Folder;
import com.dagu.pojo.User;


public interface FolderService {

	public Folder getFolderByFid(int fid);

	public List<Folder> getChildFolders(int ffid, int authority);

	public boolean newFolder(Folder folder, String classPath, User user) throws RuntimeException;

	public boolean isFolderExist(Folder folder);

    public boolean UpdateFile(Folder file, String classPath) throws RuntimeException;

    public boolean DeleteFolder(int fid, String classpath) throws RuntimeException;
}
