package org.sdrc.cysdrf.web;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sdrc.cysdrf.model.UserDetailModel;
import org.sdrc.cysdrf.service.UserService;
import org.sdrc.cysdrf.util.Constants;
import org.sdrc.cysdrf.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
//@SessionAttributes("user")
public class AuthenticateController implements AuthenticationProvider {
	
	private final StateManager stateManager;
	
	@Autowired
	private ResourceBundleMessageSource applicationMessageSource;

	@Autowired
	private MessageDigestPasswordEncoder messageDigest;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	public AuthenticateController(StateManager stateManager) {
		this.stateManager = stateManager;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String authorize(
			HttpServletRequest request,
			RedirectAttributes redirectAttributes,
			@RequestParam("userEmail") String userEmail,
			@RequestParam("userPassword") String password,
			Model model) throws IOException {
		
		String referer = request.getHeader(Constants.REFERER);
		referer = referer != null ? referer : "/";
		
		
		if(stateManager.getValue(Constants.USER_PRINCIPAL)!=null)
			return "redirect:/";
		else{
			List<String> errMessgs = new ArrayList<String>();
			try {
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEmail, password);
				token.setDetails(new WebAuthenticationDetails(request));
				Authentication authentication = this.authenticate(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}catch (Exception e) {
				e.printStackTrace();
				SecurityContextHolder.getContext().setAuthentication(null);
				errMessgs.add("Invalid username password");
				redirectAttributes.addFlashAttribute("formError", errMessgs);
				redirectAttributes.addFlashAttribute("className",applicationMessageSource.getMessage("bootstrap.alert.danger",null, null));
				return "redirect:/";
			}
			model.addAttribute("userDetail",((UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL)));
			return Constants.REDIRECT + referer;
		}

	}
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		String encodePassword = messageDigest.encodePassword(authentication.getName(),(String)authentication.getCredentials());
		UserDetailModel userDetailModel = userService.setUserDetail(authentication.getName());

		if (null==userDetailModel  || !userDetailModel.getPassword().equals(encodePassword))
			
			throw new BadCredentialsException("Invalid User!");

		// Save the user to the Session State Manager, for global access.
		stateManager.setValue(Constants.USER_PRINCIPAL, userDetailModel);
		/**
		 * Retrieve current request object 
		 */
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		String ipAddress = getIpAddr(request);
		String userAgent = request.getHeader("User-Agent");
//		userService.createNewTimeperiod();
		try {
			userService.createCurrentTimeperiod();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long loginMetaId = userService.saveUserLoginMeta(ipAddress, userDetailModel.getUserId(), userAgent);
		stateManager.setValue(Constants.LOGIN_META_ID, loginMetaId);
		
		return new UsernamePasswordAuthenticationToken(authentication.getName(),(String)authentication.getCredentials(), null);
	}
	
	private String getIpAddr(HttpServletRequest request) {      
		   String ip = request.getHeader("x-forwarded-for");      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
		       ip = request.getHeader("Proxy-Client-IP");      
		   }      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
		       ip = request.getHeader("WL-Proxy-Client-IP");      
		   }      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
		       ip = request.getRemoteAddr();      
		   }      
		   return ip;      
		}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse resp, RedirectAttributes redirectAttributes)
			throws IOException, ServletException {
		
		HttpSession session=request.getSession(false);
		if(session !=null){
			long userLoginMetaId = (long) stateManager.getValue(Constants.LOGIN_META_ID);
			userService.updateLoggedOutStatus(userLoginMetaId,  new Timestamp(new Date().getTime()));
			stateManager.setValue(Constants.USER_PRINCIPAL, null);
			request.getSession().setAttribute(Constants.USER_PRINCIPAL, null);
			request.getSession().invalidate();
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
					.currentRequestAttributes();
			attr.getRequest().getSession(true)
					.removeAttribute(Constants.USER_PRINCIPAL);
			attr.getRequest().getSession(true).invalidate();
	
			request.logout();
	
			List<String> errMessgs = new ArrayList<>();
			
			errMessgs.add("Successfully logged out!!");
			redirectAttributes.addFlashAttribute("formError", errMessgs);
			redirectAttributes.addFlashAttribute("className",applicationMessageSource.getMessage("bootstrap.alert.success",null, null));
			return "redirect:/";
		}
		else{
			request.getSession().invalidate();
			return "redirect:/";
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
