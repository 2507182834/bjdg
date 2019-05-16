package com.dagu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dagu.pojo.Folder;

public interface FolderDao {

	public Folder getFolderByFid(@Param("fid") int fid);

	public List<Folder> getChildFolders(@Param("ffid")int ffid, @Param("authority") int authority);

	public int addFolder(@Param("folder")Folder folder);

	public int getCountByFnameAndFfid(@Param("folder")Folder folder);

	public int delFolderByFnameAndFfid(@Param("folder")Folder folder);

    public int updateFolder(@Param("folder") Folder folder);

	public int DeleteFolderByFid(@Param("fid") int fid);
}
