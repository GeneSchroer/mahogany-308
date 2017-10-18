package controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedistrictingController {

	@RequestMapping("/redistrict")
	public String accessRedistrictingPage() {
		
		return "redistrict";
	}
	
}
