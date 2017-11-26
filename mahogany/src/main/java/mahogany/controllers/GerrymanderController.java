package mahogany.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.JsonNode;

import mahogany.metrics.MetricOption;
import mahogany.utils.GerrymanderHelper;

@Controller
public class GerrymanderController {

	
	@Autowired
	GerrymanderHelper helper; 	
	@RequestMapping("/")
	public String loader(){
		return "index";
	}
	
	@RequestMapping("/gerrymander")
	public String accessGerrymanderPageRequest() {
		
		return "gerrymander";
	}
	
	@RequestMapping("/gerrymander2")
	public String accessGerrymanderPage2Request() {
		
		return "gerrymander2";
	}
	
	
	@RequestMapping("/districtBoundariesRequest")
	public @ResponseBody JsonNode getDistrictBoundariesRequest(@RequestParam(name="state")String state, 
																	@RequestParam(name="year") int year) {
		JsonNode districtJsonNode = helper.createDistrictBoundariesJsonNode(state, year);
		
		return districtJsonNode;
	}
	
	@RequestMapping("/electionDataRequest")
	public @ResponseBody JsonNode getElectionDataRequest(@RequestParam("state")String stateName,
															@RequestParam("year")Integer year) {
		JsonNode electionDataJsonNode = helper.getDistrictDataJsonNode(MetricOption.ELECTION_DATA, stateName, year);
		
		return electionDataJsonNode;
	}
	@RequestMapping("/efficiencyGapRequest")
	public @ResponseBody JsonNode getEfficiencyGapRequest(@RequestParam(name="state") String stateName,
																@RequestParam(name="year")Integer year) {
		
		JsonNode metricsJsonNode = helper.getDistrictDataJsonNode(MetricOption.EFFICIENCY_GAP, stateName, year);
		
		return metricsJsonNode;
		
	}
	
	@RequestMapping("/home")
	public String login() {
		return "home";
	}
	
	
	@RequestMapping("/getYears")
	public @ResponseBody List<Integer> getYearListRequest(){
		List<Integer> yearList = helper.getYearList();
		
		return yearList;
	}
	
	
	
}
