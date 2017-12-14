package mahogany.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import mahogany.entities.Districts;
import mahogany.entities.Elections;
import mahogany.exceptions.NoDistrictsFoundException;
import mahogany.metrics.EfficiencyGapStateData;
import mahogany.metrics.EfficiencyGapDataBuilder;
import mahogany.metrics.ElectionDataBuilder;
import mahogany.metrics.ElectionStateData;
import mahogany.metrics.MemberDataBuilder;
import mahogany.metrics.MemberStateData;
import mahogany.metrics.MetricOption;
import mahogany.repositories.DistrictsRepository;
import mahogany.repositories.ElectionsRepository;

@Component
public class GerrymanderHelper {

	@Autowired
	UCLADistrictFileUploaderImpl uclaDistrictConverter;
	
	@Autowired
	PrincetonElectionDataUploaderImpl princetonElectionUploader;
	
	@Autowired DistrictsRepository districtsRepo;
	@Autowired ElectionsRepository electionsRepo;
	
	
	@Autowired GeoJsonUtils geoJsonUtils;
	
	
	
	public JsonNode createDistrictBoundariesJsonNode(String state, int year) throws JsonProcessingException, IOException {
		ArrayList<Districts> districtList = (ArrayList<Districts>)districtsRepo.findAllByStateAndYear(state, year);
		
		if (districtList.isEmpty()) {
			throw new NoDistrictsFoundException();
		}
		else {
			JsonNode districtJsonNode;
			districtJsonNode = geoJsonUtils.createDistrictBoundariesJsonNode(districtList);
			return districtJsonNode;
		}
	}
	
	public JsonNode getDistrictDataJsonNode(MetricOption metric, String stateName, Integer year) throws IOException {
		
		
		if(metric == MetricOption.EFFICIENCY_GAP) {
			ArrayList<Elections> electionList = (ArrayList<Elections>)electionsRepo.findAllByStateAndYear(stateName, year);
			if(electionList.isEmpty()) {
				throw new NoDistrictsFoundException();
			}
			EfficiencyGapDataBuilder x = new EfficiencyGapDataBuilder();
			EfficiencyGapStateData metricData = x.generateDataObject(electionList);
			JsonNode metricsJson = geoJsonUtils.generateMetricDataJson(metricData);
			return metricsJson;
		}
		else if(metric == MetricOption.ELECTION_DATA) {
			ArrayList<Elections> electionList = (ArrayList<Elections>)electionsRepo.findAllByStateAndYear(stateName, year);
			if (electionList.isEmpty()) {
				throw new NoDistrictsFoundException();
			}
			
			ElectionDataBuilder x = new ElectionDataBuilder();
			ElectionStateData electionData= x.generateDataObject(electionList);
			JsonNode metricsJson = geoJsonUtils.generateMetricDataJson(electionData);
			return metricsJson;
		}
		else if(metric == MetricOption.MEMBER_DATA) {
			ArrayList<Districts> districtList = (ArrayList<Districts>)districtsRepo.findAllByStateAndYear(stateName, year);
			if(districtList.isEmpty()) {
				throw new NoDistrictsFoundException();
			}
			MemberDataBuilder x = new MemberDataBuilder();
			MemberStateData electionData= x.generateMetricData(districtList);
		
			
			
				JsonNode metricsJson = geoJsonUtils.generateMetricDataJson(electionData);
				return metricsJson;
		}
		else {
		
			return null;
		}
	}


	public List<Integer> getYearList() {
		
		return districtsRepo.findAllYears();
	}
	
	
	
}
