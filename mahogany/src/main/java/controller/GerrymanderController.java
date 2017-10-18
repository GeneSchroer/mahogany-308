package controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class GerrymanderController {

	@RequestMapping(name="/gerrymander")
	public String indexLoader() {
		
		return "gerrymander";
	}
	
}
