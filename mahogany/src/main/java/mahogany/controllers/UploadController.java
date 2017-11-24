package mahogany.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mahogany.utils.GerrymanderHelper;
import mahogany.utils.Source;

@Controller
public class UploadController {

	@Autowired GerrymanderHelper helper;
	
	
	@RequestMapping("/upload")
	public String uclaDistrictDataUploadRequest(@RequestParam("file")MultipartFile[] files) {
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
