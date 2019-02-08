package org.sdrc.cysdrf.web;

import java.util.List;

import org.sdrc.core.Authorize;
import org.sdrc.cysdrf.model.UserDetailModel;
import org.sdrc.cysdrf.model.UserRoleSchemeModel;
import org.sdrc.cysdrf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserRoleManagementController {
	
	@Autowired
	private MessageDigestPasswordEncoder messageDigest;
	
	@Autowired
	private UserService userService;
	
	@Authorize(feature="user_management", permission="Edit")
	@RequestMapping(value="/userManagement")
	public String getUserManagement(){
		 return "userManagement";
	}
	
	@Authorize(feature="role_management", permission="Edit")
	@RequestMapping(value="/roleManagement")
	public String getRoleManagement(){
		 return "roleManagement";
	}
	
	@Authorize(feature="role_management", permission="Edit")
	@RequestMapping(value="/roleMaster")
	public String getRoleMaster(){
		 return "roleMaster";
	}

	@Authorize(feature="user_management", permission="Edit")
	@RequestMapping(value="/createNewUser", method=RequestMethod.POST)
	public String addNewUser(@RequestParam("userName") String userName, @RequestParam("emailId") String emailId, 
			 @RequestParam("contactNum") String contactNum, @RequestParam("password") String password){
		 userService.addNewUser(userName, emailId, contactNum, messageDigest.encodePassword(emailId,password));
		 return "redirect:/userManagement";
	}
	
	@Authorize(feature="user_management", permission="Edit")
	@RequestMapping("/activateDeactivateUser")
	@ResponseBody
	public boolean activeDeactiveUser(@RequestParam("userId") Integer userId, @RequestParam("isLive") boolean isLive){
		return userService.activeDeactiveUser(userId, isLive);
	}
	
	@Authorize(feature="role_management", permission="Edit")
	@RequestMapping(value="/getAllUserRoleSchemes")
	@ResponseBody
	public List<UserRoleSchemeModel> getAllUserRoleSchemes(){
		return userService.findAll();
	}
	
	@Authorize(feature="user_management", permission="Edit")
	@RequestMapping(value="/getAllUser")
	@ResponseBody
	public List<UserDetailModel> getAllUser(){
		return userService.findAllUser();
	}
	
	@Authorize(feature="role_management", permission="Edit")
	@RequestMapping(value="/getUsersOfNoRole")
	@ResponseBody
	public List<UserDetailModel> getUsersOfNoRole(){
		return userService.getUsersNotAttachedToRole();
	}
	
	@Authorize(feature="role_management", permission="Edit")
	@RequestMapping(value="/attachRole", method= RequestMethod.POST)
	@ResponseBody
	public boolean attachRoleToUsers(@RequestParam("roleFeaturePermissionSchemeId") Integer roleFeaturePermissionSchemeId,
			@RequestParam("schemeName") String schemeName, @RequestParam("userId") Integer userId){
		return userService.attachRole(roleFeaturePermissionSchemeId, schemeName, userId);
	}
	
	@Authorize(feature="role_management", permission="Edit")
	@RequestMapping(value="/detachRole")
	@ResponseBody
	public boolean detachRoleFromUser(@RequestParam("userId") Integer userId){
		userService.detachRole(userId);
		return true;
	}

}
