package mahogany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;

import mahogany.metrics.MetricOption;

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
	
	@RequestMapping("/efficiencyGapRequest")
	public @ResponseBody JsonNode getDistrictMetricsRequest(@RequestParam(name="state") String state,
																@RequestParam(name="congress")Integer congress) {
		
		JsonNode metricsJsonNode = helper.buildDistrictMetrics(MetricOption.EFFICIENCY_GAP, state, congress);
		
		return metricsJsonNode;
		
	}
	
	@RequestMapping("/home")
	public String login() {
		return "home";
	}
	
	@RequestMapping("/upload")
	public String uclaDistrictUploadRequest(@RequestParam("file")MultipartFile[] files) {
		//helper.setDistrictsRepository(districtsRepository);

		helper.uploadDistrictFiles(files, Source.UCLA);
		
		
		return "gerrymander";
	}
	
	@RequestMapping("/test")
	public String princetonElectionDataUploadRequest(@RequestParam("file")MultipartFile file) {
		
		
		
		helper.uploadElectionFile(file, Source.PRINCETON);
		
		return "gerrymander";
	}
	
	
}
