package mahogany;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class GerrymanderHelper {

	@Autowired
	UCLADistrictFileUploaderImpl uclaDistrictConverter;
	
	@Autowired
	PrincetonElectionDataUploaderImpl princetonElectionUploader;
	
	@Autowired DistrictsRepository districtsRepo;
	
	@Autowired GeoJsonUtils geoJsonUtils;
	
	public JsonNode getDistrictBoundaries(String name, Integer congress) {
		ArrayList<Districts> districtList = (ArrayList<Districts>)districtsRepo.findAllByStateAndCongress(name, congress);
		
		JsonNode districtJson = geoJsonUtils.createDistrictBoundariesJson(districtList);
		
		return districtJson;
	}
	
	public void uploadFiles(MultipartFile[] files){
		
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
	
	public void uploadElectionFile(MultipartFile file) {
		try {
			Workbook electionWorkbook = princetonElectionUploader.convertFileToExcel(file);
			princetonElectionUploader.uploadElectionDataToDatabase(electionWorkbook);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
