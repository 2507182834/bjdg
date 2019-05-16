package com.dagu.service.impl;

import com.dagu.dao.FolderDao;
import com.dagu.pojo.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dagu.dao.FileDao;
import com.dagu.pojo.File;
import com.dagu.service.FileService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService{

	@Autowired
	private FileDao fileDao;
	@Autowired
	private FolderDao folderDao;

	public File getFileByFid(int fid) {
		return fileDao.getFileByFid(fid);
	}

	public List<File> getFiles(int ffid, int authority) {
		return fileDao.getFiles(ffid, authority);
	}


	@Transactional
	public boolean saveFile(File file, CommonsMultipartFile f, String classPath) throws RuntimeException, IOException {
		Folder father = folderDao.getFolderByFid(file.getFfid());
		String filePath = classPath+father.getPath()+"/"+file.getFname();

		if(father.getAuthority()<file.getAuthority()){
			throw new RuntimeException("文件的权限不能大于父文件夹");
		}

		file.setPath(father.getPath()+"/"+file.getFname());


        if(isFileExist(file)){
            throw new RuntimeException("文件("+file.getFname()+")已存在");
        }

		fileDao.addFile(file);

		java.io.File file1 = new java.io.File(filePath);
		if(file1.exists()){
		    file1.delete();
			file1.createNewFile();
		}else{
			file1.createNewFile();
        }

		f.transferTo(file1);

		return false;
	}

	@Transactional
	public boolean UpdateFile(File file,String classPath) throws RuntimeException {

		File file1 = fileDao.getFileByFid(file.getFid());
		String oldPath;
		if(file1 != null) {
			oldPath = file1.getPath();
			String path = oldPath.substring(0,oldPath.lastIndexOf("/")+1)+file.getFname();
			file1.setFname(file.getFname());
			file1.setAuthority(file.getAuthority());
			file1.setPath(path);
			int count = fileDao.updateFile(file1);
			if(count == 0){
				throw new RuntimeException("系统错误，操作失败");
			}
		}else{
			throw new RuntimeException("文件不存在");
		}
		java.io.File file2 = new java.io.File(classPath+oldPath);
		if(file2.exists()) {
			boolean b = file2.renameTo(new java.io.File(classPath + file1.getPath()));
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
	public boolean DeleteFile(int fid, String classPath) throws RuntimeException {
		File file1 = fileDao.getFileByFid(fid);
		String path;
		if(file1 != null) {
			path = file1.getPath();
			int count = fileDao.DeleteFileByFid(fid);
			if(count == 0){
				throw new RuntimeException("系统错误，操作失败");
			}
		}else{
			throw new RuntimeException("文件不存在");
		}

		java.io.File file = new java.io.File(classPath+path);
		if(file.exists()) {
			boolean b = file.delete();
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
    public boolean isFileExist(File file) {
        return fileDao.getCountByFnameAndFfid(file)>0;
    }

}
