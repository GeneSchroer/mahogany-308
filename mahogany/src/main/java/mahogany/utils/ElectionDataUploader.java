package mahogany.utils;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

public interface ElectionDataUploader {

	Workbook convertFileToExcelWorkbook(MultipartFile file) throws IOException;
	void uploadDataToDatabase(Workbook workbook);
}
