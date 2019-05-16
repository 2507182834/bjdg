package com.dagu.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtil {

    /**
     *
     * @param file 要保存的文件
     * @param path 保存文件的目标路径(包括文件名)
     * @param overwrite 是否可以覆盖
     * @return
     */
    public static boolean saveFile(File file,String path,boolean overwrite) throws IOException {
        File targetFile = new File(path);

        if(!targetFile.exists()){
            String parentPath = targetFile.getParent();
            parentPath = parentPath.replace("\\", "/");
            File parent = new File(parentPath);
            if(!parent.exists()) {
                parent.mkdirs();
            }
            targetFile.createNewFile();
        }else if(overwrite){
            targetFile.delete();
            targetFile.createNewFile();
        }else{
            System.out.println("文件已存在，本方法不支持覆盖。");
            return false;
        }
        int bufferSize = 131072;
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(targetFile);
        FileChannel fileChannel = fis.getChannel();
        ByteBuffer buff = ByteBuffer.allocateDirect(786432);
        byte[] byteArr = new byte[bufferSize];
        int nRead, nGet;

        try {
            while ((nRead = fileChannel.read(buff)) != -1) {
                if (nRead == 0) {
                    continue;
                }
                buff.position(0);
                buff.limit(nRead);
                while (buff.hasRemaining()) {
                    nGet = Math.min(buff.remaining(), bufferSize);
                    // read bytes from disk
                    buff.get(byteArr, 0, nGet);
                    // write bytes to output
                    fos.write(byteArr);
                }
                buff.clear();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            buff.clear();
            fileChannel.close();
            fis.close();
            fos.close();
        }
        return true;
    }

    /**
     * 功能：删除指定的文件或文件夹
     * @param path 要删除的文件或文件夹
     * @return
     */

    public static boolean deleteAllFilesOfDir(File path) throws RuntimeException {
        System.out.println("删除："+path.getPath());
        if (!path.exists())
            throw new RuntimeException("指定文件不存在");
        if (path.isFile()) {
            path.delete();
            return true;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
        return true;
    }

    /**
     *
     * @param path  要创建的文件夹路劲
     * @param multi 是否多级创建
     * @return
     */
    public static boolean createFolder(String path, boolean multi) throws RuntimeException {
        File targetFile = new File(path);

        if(targetFile.exists()){
            throw (new RuntimeException("文件已经存在"));
        }else{
            if(multi) {
                targetFile.mkdirs();
            }else{
                targetFile.mkdir();
            }
        }
        if(targetFile.exists()){
            return true;
        }
        return false;
    }
}
