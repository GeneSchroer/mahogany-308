package mahogany;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mahogany.metrics.DistrictData;
import mahogany.metrics.EfficiencyGapResults;
import mahogany.metrics.EfficiencyGapTest;
import mahogany.metrics.MetricOption;
import mahogany.metrics.TestResult;

@Component
public class GerrymanderHelper {

	@Autowired
	UCLADistrictFileUploaderImpl uclaDistrictConverter;
	
	@Autowired
	PrincetonElectionDataUploaderImpl princetonElectionUploader;
	
	@Autowired DistrictsRepository districtsRepo;
	
	@Autowired GeoJsonUtils geoJsonUtils;
	
	
	
	public JsonNode getDistrictBoundaries(String state, int congress) {
		ArrayList<Districts> districtList = (ArrayList<Districts>)districtsRepo.findAllByStateAndCongress(state, congress);
		
		
		
		JsonNode districtJsonNode;
		try {
			districtJsonNode = geoJsonUtils.createDistrictBoundariesJson(districtList);
			return districtJsonNode;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		//return districtJsonNode;
	}
	
	
	public JsonNode buildDistrictMetrics(MetricOption metric, String state, Integer congress) {
		ArrayList<Districts> districtList = (ArrayList<Districts>)districtsRepo.findAllByStateAndCongress(state, congress);
		
		if(metric == MetricOption.EFFICIENCY_GAP) {
			EfficiencyGapTest x = new EfficiencyGapTest();
			EfficiencyGapResults metricResults = x.generateTestResults(districtList);
		
			
			
			try {
				JsonNode metricsJson = geoJsonUtils.generateMetricResultsJson(metricResults);
				return metricsJson;
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public void uploadDistrictFiles(MultipartFile[] files, Source source){
		
		if(source == Source.UCLA) {
			for(MultipartFile file: files) {
				try {
					ObjectNode fileJsonObject = (ObjectNode)uclaDistrictConverter.convertFileToJsonNode(file);
					uclaDistrictConverter.uploadJsonToDatabase(fileJsonObject);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void uploadElectionFile(MultipartFile file, Source source) {
		if(source == Source.PRINCETON) {
			try {
				Workbook electionWorkbook = princetonElectionUploader.convertFileToExcel(file);
				princetonElectionUploader.uploadElectionDataToDatabase(electionWorkbook);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
