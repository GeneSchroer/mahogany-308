package mahogany.repositories;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import mahogany.App;
@Controller
public class RedistrictController {

	@RequestMapping("/redistrict")
	public String accessRedistrictPage() {
		return "redistrict";
		
	}
	
}
