package com.dagu.service.impl;

import java.io.File;
import java.util.List;

import com.dagu.dao.FileDao;
import com.dagu.pojo.User;
import com.dagu.utils.Authority;
import com.dagu.utils.FileUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


import com.dagu.dao.FolderDao;
import com.dagu.pojo.Folder;
import com.dagu.service.FolderService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FolderServiceImpl implements FolderService{

	@Autowired
	private FolderDao folderDao;
	@Autowired
	private FileDao fileDao;
	@Autowired
	private ApplicationContext ac;


	public Folder getFolderByFid(int fid) {
		return folderDao.getFolderByFid(fid);
	}

	public List<Folder> getChildFolders(int ffid, int authority) {
		
		List<Folder> folders = folderDao.getChildFolders(ffid,authority);
		
		return folders;
	}

	@Transactional
	public boolean newFolder(Folder folder, String classPath, User user) throws RuntimeException {

		Folder father = folderDao.getFolderByFid(folder.getFfid());
		String folderPath = classPath+father.getPath()+"/"+folder.getFname();

		if(father.getAuthority()<folder.getAuthority()){
			throw new RuntimeException("子文件夹的权限不能大于父文件夹");
		}

		folder.setPath(father.getPath()+"/"+folder.getFname());

		if(isFolderExist(folder)){
            throw new RuntimeException("文件夹("+folder.getFname()+")已存在");
		}
		folderDao.addFolder(folder);

		File file = new File(folderPath);
		if(file.exists()){
			file.delete();
			file.mkdirs();
		}else{
			file.mkdirs();
		}
		return true;
	}

	@Transactional
	public boolean isFolderExist(Folder folder) {
		return folderDao.getCountByFnameAndFfid(folder)>0;
	}

	@Transactional
	public boolean UpdateFile(Folder folder,String classPath) throws RuntimeException {

		Folder folder1 = folderDao.getFolderByFid(folder.getFid());
		String oldPath;
		if(folder1 != null) {
			oldPath = folder1.getPath();
			String path = oldPath.substring(0,oldPath.lastIndexOf("/")+1)+folder.getFname();
			folder1.setFname(folder.getFname());
			folder1.setAuthority(folder.getAuthority());
			folder1.setPath(path);

			int count = folderDao.updateFolder(folder1);
			if(count == 0){
				throw new RuntimeException("系统错误，操作失败");
			}
		}else{
			throw new RuntimeException("文件不存在");
		}
		java.io.File file2 = new java.io.File(classPath+oldPath);
		if(file2.exists()) {
			boolean b = file2.renameTo(new java.io.File(classPath + folder1.getPath()));
			if(b) {
				System.out.println("修改成功");
			}else{
				throw new RuntimeException("修改失败");
			}
		}else {
			System.out.println("文件不存在");
			throw new RuntimeException("文件不存在");
		}
		return false;
	}

	@Transactional
	public boolean DeleteFolder(int fid, String classPath) throws RuntimeException {
		Folder folder = folderDao.getFolderByFid(fid);
		String path;
		if(folder != null) {
			path = folder.getPath();

			List<Folder> folders = folderDao.getChildFolders(fid, Authority.admin);
			for (Folder f:folders) {
				((FolderService)AopContext.currentProxy()).DeleteFolder(f.getFid(),classPath);
			}

			fileDao.DeleteFileByFfid(fid);
			int count = folderDao.DeleteFolderByFid(fid);
			if(count == 0){
				throw new RuntimeException("系统错误，操作失败");
			}
		}else{
			throw new RuntimeException("文件不存在");
		}

		java.io.File file = new java.io.File(classPath+path);
		if(file.exists()) {

			boolean b = FileUtil.deleteAllFilesOfDir(file);
			if(b) {
				System.out.println("修改成功");
			}else{
				throw new RuntimeException("修改失败");
			}
		}else {
			System.out.println("文件不存在");
			throw new RuntimeException("文件不存在");
		}
		return false;
	}

}
