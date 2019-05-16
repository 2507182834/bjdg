package com.dagu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dagu.pojo.User;
import com.dagu.utils.AjaxMessageUtil;
import com.dagu.utils.Authority;
import com.dagu.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.dagu.pojo.Folder;
import com.dagu.service.FolderService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FolderController {
	
	@Autowired
	private FolderService folderService;


	@RequestMapping(value = "/getFolders")
	public String getFolders(HttpServletRequest request, Model model) {
		
		System.out.println("getFolders");
		
		String sffid = request.getParameter("ffid");
		int ffid = 1;
		if(!(sffid == null || sffid.equals(""))) {
			ffid = Integer.parseInt(sffid);
		}
		System.out.println("ffid:"+ffid);
		int authority = 3;
		User user = (User) request.getSession().getAttribute("user");
		if(user != null) {
			authority = user.getAuthority();
		}

		System.out.println(authority);
		Folder folder = folderService.getFolderByFid(ffid);
		if(folder == null || folder.getAuthority()<authority){
			return "folders";
		}
		List<Folder> folders = folderService.getChildFolders(ffid, authority);
		model.addAttribute("folders", JSONUtil.getString(folders));
		model.addAttribute("folder", JSONUtil.getString(folder));
		return "folders";
	}

	@RequestMapping(value = "/newFolder",produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
	@ResponseBody
	public String newFolder(@RequestParam("ffid") int ffid, @RequestParam("foldername") String folderName,
							 @RequestParam("authority") int authority,HttpServletRequest request) {
		System.out.println("newFolder");
		AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			msg.setTips("权限不足");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		if(user.getAuthority()> Authority.employee){
			msg.setTips("权限不足");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}


		if(folderName == null || folderName.equals("")){
			msg.setTips("文件名不能为空");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		System.out.println("ffid:"+ffid);
		String classPath = request.getServletContext().getRealPath("/WEB-INF/");
		Folder folder = new Folder();
		folder.setFfid(ffid);
		folder.setFname(folderName);
		folder.setCreater(user);
		folder.setAuthority(authority);
		try {
			folderService.newFolder(folder,classPath,user);
		} catch (Exception e) {
			msg.setTips(e.getMessage());
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		msg.setStatus(AjaxMessageUtil.SUCCESS);
		return JSONUtil.getString(msg);
	}


	@RequestMapping(value = "/UpdateFolder", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
	@ResponseBody
	public String UpdateFile(@RequestParam("fid")int fid, @RequestParam("fname") String fname, @RequestParam("authority") int authority, HttpServletRequest request){
		System.out.println("UpdateFolder");
		AjaxMessageUtil<String> msg = new AjaxMessageUtil<String>();
		String classPath = request.getServletContext().getRealPath("/WEB-INF/");

		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			msg.setTips("权限不足");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		System.out.println(fid+"----"+authority);

		if(user.getAuthority()> Authority.employee){
			msg.setTips("权限不足");
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}

		Folder folder = new Folder();

		folder.setAuthority(authority);
		folder.setFid(fid);
		folder.setFname(fname);
		try {
			folderService.UpdateFile(folder,classPath);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setTips(e.getMessage());
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		msg.setStatus(AjaxMessageUtil.SUCCESS);
		return JSONUtil.getString(msg);
	}

	@RequestMapping(value = "/DeleteFolder", produces = "text/html;charset=UTF-8;",method = RequestMethod.POST)
	@ResponseBody
	public String DeleteFolder(@RequestParam("fid") int fid, HttpServletRequest request){
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
			folderService.DeleteFolder(fid,classPath);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setTips(e.getMessage());
			msg.setStatus(AjaxMessageUtil.FAIL);
			return JSONUtil.getString(msg);
		}
		msg.setStatus(AjaxMessageUtil.SUCCESS);
		return JSONUtil.getString(msg);
	}

	@RequestMapping(value = "/getUploadProgress", produces = "text/html;charset=UTF-8;",method = RequestMethod.GET)
	@ResponseBody
	public String getUploadProgress(HttpServletRequest request){
		String progress=(String) request.getSession().getAttribute("progress");
		return progress;
	}
}
