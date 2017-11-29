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

	@RequestMapping("/upload")
	public String uclaBoundaryDataUploadRequest(@RequestParam("file")MultipartFile[] files) {
		uploadHelper.uploadDistrictFiles(files, FileSource.UCLA);
		return "gerrymander";
	}
	
	@RequestMapping("/test")
	public String princetonElectionDataUploadRequest(@RequestParam("file")MultipartFile file) {
		uploadHelper.uploadElectionFile(file, FileSource.PRINCETON);
		return "gerrymander";
	}
	
}
