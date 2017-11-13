package mahogany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;  
import org.apache.commons.fileupload.FileItemFactory;  
import org.apache.commons.fileupload.FileUploadException;  
import org.apache.commons.fileupload.disk.DiskFileItemFactory;  
import org.apache.commons.fileupload.servlet.ServletFileUpload; 
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
public class GerrymanderController {

	@Autowired
	private StateNamesRepository stateNamesRepository;
	
	@Autowired
	private DistrictsRepository districtsRepository;
	
	@Autowired 
	DistrictDetailsRepository districtDetailsRepository;
	
	@RequestMapping("/")
	public String loader(){
		
		//System.out.println("test1");
		
		return "index";
	}
	
	@RequestMapping("/gerrymander")
	public String accessGerrymanderPage() {
		
		return "gerrymander";
	}
	
	
	
	@RequestMapping("/home")
	public String login() {
		return "home";
	}
	
	@RequestMapping("/admin/upload")
	public String uploadFile(@RequestParam("file")MultipartFile[] files) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader();
		
		for(MultipartFile file: files) {
			System.out.println(file.getOriginalFilename());
			
			
			try {
			
				String jsonstring = new String(file.getBytes());
				ObjectNode json = (ObjectNode) reader.readTree(jsonstring);
				//System.out.println(json.toString());
				
				GeoJsonUtils geo = new GeoJsonUtils();
				ArrayList<Districts> districts = convertJsonToDistricts(json);
			//	districtsRepository.save(districts);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "gerrymander";
	}
	
	@RequestMapping("/test")
	public String test() {
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
	
	public ArrayList<Districts> convertJsonToDistricts(ObjectNode json){
		int count=0;
		ArrayList<Districts> districtList = new ArrayList<Districts>();
		ArrayNode featuresArray = (ArrayNode)json.get("features");
		Iterator<JsonNode> features = featuresArray.iterator();
		while(features.hasNext()) {
			ObjectNode properties = (ObjectNode)features.next().get("properties");
			//Districts district = new Districts();
			System.out.println(properties.get("startcong").asInt());
			//district.setCongress(properties.get("startcong").asInt());
			//district.setNumber(properties.get("district").asInt());
			//System.out.println(properties.get("statename").asText());
			//System.out.println(count++);
			String stateName = properties.get("statename").asText();
			int startCong = properties.get("startcong").asInt();
			int endCong = properties.get("endcong").asInt();
			int number = properties.get("district").asInt();
			StateNames stateNames = stateNamesRepository.findFirstByName(stateName);
			if(stateNames == null) {
			
				System.out.println("creating new state");
				stateNames = new StateNames();
				stateNames.setName(stateName);
				stateNamesRepository.save(stateNames);
				
			}
			
			for(int congress = startCong; congress<=endCong; congress = congress +1) {
				DistrictDetails districtDetails = districtDetailsRepository.findByStateNameAndNumberAndCongress(stateName, number, congress);
				if(districtDetails == null) {
					districtDetails = new DistrictDetails();
					districtDetails.setStateId(stateNames.getId());
					districtDetails.setNumber(number);
					districtDetails.setCongress(congress);
					districtDetailsRepository.save(districtDetails);
					//districtDetails.setStateName(stateNames);
				}
				
				//System.out.println("State Name: " + districtDetails.getStateName().getName());
				Districts districts = districtsRepository.findByStateNameAndNumberAndCongress(stateName, number, congress);
				if (districts == null) {
					districts = new Districts();
					districts.setDetailsId(districtDetails.getId());
					districtsRepository.save(districts);
				}
				ObjectNode memberObject = (ObjectNode)properties.get("member");
				ObjectNode electionObject = (ObjectNode)properties.get("election");
				
				ObjectNode memberAndCongressObject = (ObjectNode)memberObject.get(String.valueOf(congress));
				ObjectNode electionAndCongressObject = (ObjectNode)electionObject.get(String.valueOf(congress));
			}
			
			
			//districts.add(district);
			
		}
		return districtList;
	}
	
}
