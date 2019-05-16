package com.dagu.controller;

import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dagu.service.FileService;
import com.dagu.utils.AjaxMessageUtil;
import com.dagu.utils.Authority;
import com.dagu.utils.JSONUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.dagu.pojo.User;

@Controller
public class FileController {

	private Logger log = Logger.getLogger(this.getClass().getName());
	@Autowired
	private FileService fileService;

	@RequestMapping(value = "/toIndex")
	public String toindex(Model model) {

		System.out.println("toindex");
		model.addAttribute("msg", "111111");
		return "/../../index";
	}
	@RequestMapping(value = "/*index*")
	public String index(Model model) {

		System.out.println("toindex");
		model.addAttribute("msg", "111111");
		return "/../../index";
	}

	@RequestMapping(value = "/upLoadFile", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
	@ResponseBody
	public String upload(@RequestParam("ffid") int ffid,@RequestParam("authority") int authority,
						 @RequestParam("file") CommonsMultipartFile[] files, HttpServletRequest request){
		System.out.println("upload");
		AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
		User user = (User) request.getSession().getAttribute("user");
		if(user == null || user.getAuthority()> Authority.employee){
			msg.setTips("权限不足");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}

		System.out.println(ffid+"----"+user.getAuthority());


		String classPath = request.getServletContext().getRealPath("/WEB-INF/");

		for (CommonsMultipartFile f : files){
			System.out.println(f.getOriginalFilename());
			com.dagu.pojo.File file = new com.dagu.pojo.File();

			file.setFfid(ffid);
			file.setFname(f.getOriginalFilename());
			file.setUploadMan(user.getUid());
			file.setAuthority(authority);
			try {
				fileService.saveFile(file,f,classPath);
			} catch (Exception e) {
				msg.setTips(e.getMessage());
				msg.setStatus(AjaxMessageUtil.FAIL);
				return JSONUtil.getString(msg);
			}
		}
		msg.setStatus(AjaxMessageUtil.SUCCESS);
		return JSONUtil.getString(msg);
	}

	@RequestMapping(value = "/download", produces = "text/html;charset=UTF-8;")
	public String down(@Param("fid") int fid, HttpServletRequest request, HttpServletResponse response) throws Exception {

		int authority = 3;

		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			request.setAttribute("url","getFolders");
			return "/../../login_register";
		}else{
			authority = user.getAuthority();
		}
		com.dagu.pojo.File file = fileService.getFileByFid(fid);
		if (file != null && file.getAuthority()<authority){
			response.setHeader("Content-type","text/html;charset=utf-8");
			response.getWriter().println("<h1>用户权限不足<h1>");
			return null;
		}
		String path = request.getServletContext().getRealPath("/WEB-INF/")+file.getPath();
		File file1 = new File(path);
//		// 获取输入流
//		InputStream bis = new BufferedInputStream(new FileInputStream(file1));
//		// 假如以中文名下载的话转码，免得文件名中文乱码
//		String fileName = URLEncoder.encode(file.getFname(), "UTF-8");
//		// 设置文件下载头
//		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
//		// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
//		response.setContentType("multipart/form-data");
//		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//		int len = 0;
//		while ((len = bis.read()) != -1) {
//			out.write(len);
//			out.flush();
//		}
//		out.close();
//		bis.close();
		if (file1.exists()) {
			String userAgent = request.getHeader("User-Agent");
			String formFileName = file.getFname();

			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				formFileName = java.net.URLEncoder.encode(formFileName, "UTF-8");
			} else {
				// 非IE浏览器的处理：
				formFileName = new String(formFileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", formFileName));
			response.setContentType("multipart/form-data");
			response.setCharacterEncoding("UTF-8");
			response.setContentLength((int) file1.length());

			//NIO 实现
			int bufferSize = 131072;
			FileInputStream fileInputStream = new FileInputStream(file1);
			FileChannel fileChannel = fileInputStream.getChannel();
			// 6x128 KB = 768KB byte buffer
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
						response.getOutputStream().write(byteArr);
					}
					buff.clear();

				}
			} catch (IOException e) {
				response.setHeader("Content-type","text/html;charset=utf-8");
				response.getWriter().println("<h1>"+e.getMessage()+"<h1>");
				return null;
			} finally {
				buff.clear();
				fileChannel.close();
				fileInputStream.close();
			}

		}else{
			response.setHeader("Content-type","text/html;charset=utf-8");
			response.getWriter().println("<h1>文件不存在<h1>");
			return null;
	}
		log.warn("[Download]   用戶："+user.getName()+"下載了文件--->"+file.getFname());
		return null;
}

	@RequestMapping(value = "/getFiles", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
	@ResponseBody
	public String getFiles(@RequestParam("ffid") int ffid,HttpServletRequest request) {

		int authority = 3;

		User user = (User) request.getSession().getAttribute("user");
		if(user != null){
			authority = user.getAuthority();
		}

		System.out.println("getFiles:ffid="+ffid);

		return JSONUtil.getString(fileService.getFiles(ffid, authority));
	}

	@RequestMapping(value = "/UpdateFile", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
	@ResponseBody
	public String UpdateFile(@RequestParam("fid")int fid,@RequestParam("fname") String fname, @RequestParam("authority") int authority,HttpServletRequest request){
		System.out.println("UpdateFile");
		AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
		String classPath = request.getServletContext().getRealPath("/WEB-INF/");

		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			msg.setTips("权限不足1");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		System.out.println(fid+"----"+authority);

		if(user.getAuthority()> Authority.employee){
			msg.setTips("权限不足2");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}

		com.dagu.pojo.File file = new com.dagu.pojo.File();
		file.setAuthority(authority);
		file.setFid(fid);
		file.setFname(fname);
		try {
			fileService.UpdateFile(file,classPath);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setTips(e.getMessage());
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		msg.setStatus(AjaxMessageUtil.SUCCESS);
		return JSONUtil.getString(msg);
	}

	@RequestMapping(value = "/DeleteFile", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
	@ResponseBody
	public String DeleteFile(@RequestParam("fid") int fid, HttpServletRequest request){
		System.out.println("DeleteFile");
		AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
		String classPath = request.getServletContext().getRealPath("/WEB-INF/");

		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			msg.setTips("权限不足");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		System.out.println(fid);

		if(user.getAuthority()> Authority.employee){
			msg.setTips("权限不足");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}

		try {
			fileService.DeleteFile(fid,classPath);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setTips(e.getMessage());
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		msg.setStatus(AjaxMessageUtil.SUCCESS);
		return JSONUtil.getString(msg);
	}
}
