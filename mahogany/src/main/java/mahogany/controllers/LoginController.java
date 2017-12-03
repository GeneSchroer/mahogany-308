package mahogany.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mahogany.loginUtils.LoginStatus;
import mahogany.utils.LoginHelper;

@Controller
public class LoginController {
	@Autowired LoginHelper helper;
	
	
	@RequestMapping("/")
	public String accessLoginPageRequest(){
		return "index";
	}
	
	@RequestMapping("/login")
	public void loginUserRequest() {
		
	}
	
	@RequestMapping("/logout")
	public void logoutUserRequest() {
		
	}
	
	@RequestMapping("/register")
	public String registrationRequest(@RequestParam(name="userName") String userName,
																						@RequestParam(name="password") String password) {
		LoginStatus registrationStatus = helper.registerNewUser(userName, password);
		
		if(registrationStatus == LoginStatus.ALREADY_LOGGED_IN) {
			return "redirect:index";
		}
		else {
			return "redirect:gerrymander";
		}
	}
	
	
	
	
}
