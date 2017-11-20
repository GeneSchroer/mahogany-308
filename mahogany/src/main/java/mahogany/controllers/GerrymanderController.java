package mahogany.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;

import mahogany.metrics.MetricOption;
import mahogany.utils.GerrymanderHelper;
import mahogany.utils.Source;

@Controller
public class GerrymanderController {

	
	@Autowired
	GerrymanderHelper helper; 	
	@RequestMapping("/")
	public String loader(){
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
	
	@RequestMapping("/electionDataRequest")
	public @ResponseBody JsonNode getElectionDataRequest(@RequestParam("state")String stateName,
															@RequestParam("congress")Integer congress) {
		JsonNode electionDataJsonNode = helper.buildDistrictMetrics(MetricOption.ELECTION_DATA, stateName, congress);
		
		return electionDataJsonNode;
	}
	@RequestMapping("/efficiencyGapRequest")
	public @ResponseBody JsonNode getDistrictMetricsRequest(@RequestParam(name="state") String stateName,
																@RequestParam(name="congress")Integer congress) {
		
		JsonNode metricsJsonNode = helper.buildDistrictMetrics(MetricOption.EFFICIENCY_GAP, stateName, congress);
		
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
