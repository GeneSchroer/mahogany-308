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
																	@RequestParam(name="year") int year) {
		JsonNode districtJsonNode = helper.getDistrictBoundaries(state, year);
		
		return districtJsonNode;
	}
	
	@RequestMapping("/electionDataRequest")
	public @ResponseBody JsonNode getElectionDataRequest(@RequestParam("state")String stateName,
															@RequestParam("year")Integer year) {
		JsonNode electionDataJsonNode = helper.getDistrictData(MetricOption.ELECTION_DATA, stateName, year);
		
		return electionDataJsonNode;
	}
	@RequestMapping("/efficiencyGapRequest")
	public @ResponseBody JsonNode getEfficiencyGapRequest(@RequestParam(name="state") String stateName,
																@RequestParam(name="year")Integer year) {
		
		JsonNode metricsJsonNode = helper.getDistrictData(MetricOption.EFFICIENCY_GAP, stateName, year);
		
		return metricsJsonNode;
		
	}
	
	@RequestMapping("/home")
	public String login() {
		return "home";
	}
	
	
	
	
	
	
}
