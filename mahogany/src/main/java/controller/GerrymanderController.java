package controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class GerrymanderController {

	@RequestMapping("/")
	public String loader(){
		
		System.out.println("test2");
		
		return "index";
	}
	
	@RequestMapping("/gerrymander")
	public String indexLoader() {
		
		return "gerrymander";
	}
	
}
