package mahogany.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import mahogany.loginUtils.LoginStatus;
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
																@RequestParam(name="password")String password, HttpSession session) {
		ModelAndView mav;
		LoginStatus userStatus = helper.loginUser(userName, password);
		if(userStatus == LoginStatus.ALREADY_LOGGED_IN) {
			mav = new ModelAndView("gerrymander");
			session.setAttribute("userName", userName);
			System.out.println(session.getId());
			System.out.println(session.getAttribute("userName").toString());
			//mav.addObject("userName", userName);
		}
		else if(userStatus == LoginStatus.NO_SUCH_USER) {
			mav = new ModelAndView("index");
		}
		else {
			mav = new ModelAndView("gerrymander");
			//mav.addObject("userName", userName);
			session.setAttribute("userName", userName);

		}
		return mav;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logoutUserRequest(HttpSession session) {
		String userName = (String)session.getAttribute("userName");
				
		
		helper.logoutUser(userName);
		
		session.removeAttribute("userName");
		return new ModelAndView("index");
	}
	
	@RequestMapping("/register")
	public String registrationRequest(@RequestParam(name="userName") String userName,
																				@RequestParam(name="password") String password, 
																					Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		LoginStatus registrationStatus = helper.registerNewUser(userName, password);
		
		if(registrationStatus == LoginStatus.USER_ALREADY_EXISTS) {
			return "";
		}
		else {
			session.setAttribute("userName", userName);
			model.addAttribute("userName", userName);
			return "gerrymander";
		}
	}
	
	
	
	
}
