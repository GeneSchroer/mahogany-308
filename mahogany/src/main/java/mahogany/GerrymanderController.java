package mahogany;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class GerrymanderController {

	@RequestMapping("/")
	public String loader(){
		
		//System.out.println("test1");
		
		return "index";
	}
	
	@RequestMapping("/gerrymander")
	public String indexLoader() {
		
		return "gerrymander";
	}
	
	@RequestMapping("/home")
	public String login() {
		return "home";
	}
	
}
