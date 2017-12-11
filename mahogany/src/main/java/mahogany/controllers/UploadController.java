package mahogany.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import mahogany.repositories.BoundariesRepository;
import mahogany.utils.FileUploadHelper;
import mahogany.utils.FileSource;

@Controller
public class UploadController {

	@Autowired FileUploadHelper uploadHelper;
	@Autowired BoundariesRepository boundariesRepo ;

	@RequestMapping("/uclaJsonUpload")
	public String uclaBoundaryDataUploadRequest(@RequestParam("file")MultipartFile[] files) {
			uploadHelper.uploadDistrictFiles(files, FileSource.UCLA);
		
		return "redirect:/gerrymander";
	}
	
	@RequestMapping("/princetonElectionUpload")
	public String princetonElectionDataUploadRequest(@RequestParam("file")MultipartFile file) {
		uploadHelper.uploadElectionFile(file, FileSource.PRINCETON);
		return "redirect:/gerrymander";
	}
	
	
	@RequestMapping("censusJsonUpload")
	public String censusJsonUploadRequest(@RequestParam("file")MultipartFile[] files) {
		uploadHelper.uploadDistrictFiles(files, FileSource.US_CENSUS);
		
		
		return "redirect:/gerrymander";
	}
	
	
	public void uploadCongressionalMemberDataRequest() {
		
	}
	
	public void uploadElectionDataRequest() {
		
	}
	
}
