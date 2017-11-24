package mahogany.utils;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class FileUploadHelper {

	@Autowired
	UCLADistrictFileUploaderImpl uclaDistrictConverter;
	
	@Autowired
	PrincetonElectionDataUploaderImpl princetonElectionUploader;
	
	
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
