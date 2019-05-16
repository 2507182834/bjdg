package com.dagu.dao;

import com.dagu.pojo.File;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileDao {

    public File getFileByFid(@Param("fid") int fid);

    public List<File> getFiles(@Param("ffid")int ffid,@Param("authority") int authority);

    public int addFile(@Param("file") File file);

    public int getCountByFnameAndFfid(@Param("file") File file);

    public int  updateFile(@Param("file") File file);

    public int DeleteFileByFid(@Param("fid")int fid);

    public int DeleteFileByFfid(@Param("ffid")int ffid);
}
