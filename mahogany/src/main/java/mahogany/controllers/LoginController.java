package mahogany.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import mahogany.utils.LoginHelper;

@Controller
public class LoginController {
	@Autowired LoginHelper helper;
	
	
	@RequestMapping("/register")
	public String processRegistrationRequest() {
		
		
		return "gerrymander";
	}
	
	
}
