package mahogany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class GerrymanderController {

	//@Autowired
	//private StateNamesRepository stateNamesRepository;
	
	//@Autowired
	//private DistrictsRepository districtsRepository;
	
	@Autowired
	GerrymanderHelper helper; //= new GerrymanderHelper();
	
	@RequestMapping("/")
	public String loader(){
		
		//System.out.println("test1");
		
		return "index";
	}
	
	@RequestMapping("/gerrymander")
	public String accessGerrymanderPage() {
		
		return "gerrymander";
	}
	
	@RequestMapping("/districtBoundariesRequest")
	public @ResponseBody JsonNode getDistrictBoundariesRequest(@RequestParam(name="state")String state, 
											@RequestParam(name="congress") int congress) {
		JsonNode districtJsonNode = helper.getDistrictBoundaries(state, congress);
		
		return districtJsonNode;
	}
	
	@RequestMapping("/home")
	public String login() {
		return "home";
	}
	
	@RequestMapping("/admin/upload")
	public String uploadFile(@RequestParam("file")MultipartFile[] files) {
		//helper.setDistrictsRepository(districtsRepository);

		helper.uploadFiles(files);
		
		
		return "gerrymander";
	}
	
	@RequestMapping("/test")
	public String test(@RequestParam("file")MultipartFile file) {
		
		helper.uploadElectionFile(file);
		//Iterable<StateNames> x = stateNamesRepository.findAll();//findFirstByName("Alaska");
		
	//	for(StateNames state: x) {
	///		System.out.println(state.getName());
	//	}
		
		//Districts d = districtsRepository.findByStateAndCongress("Alabama", 112);
		//System.out.println(d.getId());

		
		//System.out.println(stateNamesRepository.findFirstByName("Alaska").getName());
		//System.out.println(x.getName() + " " + x.getStateId());
		
		return "gerrymander";
	}
	
	
}
