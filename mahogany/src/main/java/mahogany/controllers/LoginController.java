package mahogany.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mahogany.entities.UserDetails;
import mahogany.loginUtils.AuthenticationFailureException;
import mahogany.loginUtils.DuplicateUserNameException;
import mahogany.utils.LoginHelper;

@Controller
//@SessionAttributes({"userName", "roles"})
public class LoginController {
	@Autowired LoginHelper helper;
	
	
	@RequestMapping("/")
	public ModelAndView accessLoginPageRequest(){
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	
	@RequestMapping("/login")
	public ModelAndView loginUserRequest(@RequestParam(name="userName") String userName,
																				@RequestParam(name="password")String password, 
																					HttpSession session,
																					HttpServletRequest request,
																					Model model) {
		//System.out.println(x);
		try {
			UserDetails user = helper.loginUser(userName, password);
			session.setMaxInactiveInterval(60*60);
			session.setAttribute("userName", user.getUserName());
			session.setAttribute("role", user.getRole().getRoleName());
			return new ModelAndView("redirect:/gerrymander");
		}
		catch(AuthenticationFailureException e) {
			model.addAttribute("loginError", e.getMessage());
			return new ModelAndView("index");
		}
	}
	
	@RequestMapping("/logout")
	public ModelAndView logoutUserRequest(HttpSession session) {
		String userName = (String)session.getAttribute("userName");
		if(userName != null) {
			helper.logoutUser(userName);
			session.removeAttribute("userName");
			session.removeAttribute("role");
		}
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/register")
	public String registrationRequest(@RequestParam(name="userName") String userName,
																						@RequestParam(name="password") String password, 
																							HttpServletRequest request,
																								Model model) {
		HttpSession session = request.getSession();
		try {
			UserDetails user = helper.registerNewUser(userName, password);
			session.setMaxInactiveInterval(60*60);
			session.setAttribute("userName", user.getUserName());
			session.setAttribute("role", user.getRole().getRoleName());
			return "redirect:/gerrymander";
		}
		catch(DuplicateUserNameException e) {
			model.addAttribute("loginError", "User Name already exists");
			return "index";
		}
	}
	
	
	
	
}
