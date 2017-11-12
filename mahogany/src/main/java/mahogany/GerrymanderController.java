package mahogany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.node.ObjectNode;

import utils.GeoJsonUtils;

@Controller
public class GerrymanderController {

	@Autowired
	private StateNamesRepository stateNamesRepository;
	
	@Autowired
	private DistrictsRepository districtsRepository;
	
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
				System.out.println(json.toString());
				new GeoJsonUtils().convertJsonToDistricts(json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "gerrymander";
	}
	
	@RequestMapping("/test")
	public String test() {
		Iterable<StateNames> x = stateNamesRepository.findAll();//findFirstByName("Alaska");
		
		for(StateNames state: x) {
			System.out.println(state.getName());
		}
		
		Districts d = districtsRepository.findByStateAndCongress("Alabama", 112);
		//System.out.println(d.getId());

		if(d != null) {
			System.out.println(d.getStateName().getName() + " " + d.getCongress());
		}
		//System.out.println(stateNamesRepository.findFirstByName("Alaska").getName());
		//System.out.println(x.getName() + " " + x.getStateId());
		
		return "gerrymander";
	}
	
	
	
}
