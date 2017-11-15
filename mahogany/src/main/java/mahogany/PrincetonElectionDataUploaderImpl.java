package mahogany;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PrincetonElectionDataUploaderImpl {

	@Autowired DistrictsRepository districtsRepo;
	@Autowired StateNamesRepository stateNamesRepo;
	@Autowired MembersRepository membersRepo;
	@Autowired MemberNamesRepository memberNamesRepo;
	@Autowired PartiesRepository partiesRepo;
	@Autowired ElectionsRepository electionsRepo;
	
	public Workbook convertFileToExcel(MultipartFile file) throws IOException {
		FileInputStream input = (FileInputStream)file.getInputStream();
		Workbook workbook = new HSSFWorkbook(input);
		
		return workbook;
	}
	
	
	public void uploadElectionDataToDatabase(Workbook electionWorkbook) {
		Sheet firstSheet = electionWorkbook.getSheetAt(0);
		
		for(Row row: firstSheet) {
			
			//skip the first row
			if(row.getRowNum() == 1) {
				continue;
			}
			
			String state = row.getCell(0).getStringCellValue();
			
			Integer raceYear = Integer.parseInt(row.getCell(1).getStringCellValue());
			Integer congress = (1972 - 1786) / 2;
			
			
			Integer districtNumber = Integer.parseInt(row.getCell(2).getStringCellValue());
			Integer republicanVotes = Integer.parseInt(row.getCell(4).getStringCellValue());
			Integer democratVotes = Integer.parseInt(row.getCell(6).getStringCellValue());
			Float democratVotePercentage = Float.parseFloat(row.getCell(7).getStringCellValue());
			Float republicanVotePercentage = 1 - democratVotePercentage;
			String winner = row.getCell(8).getStringCellValue();
			if(winner.equals("R")) {
				winner = "Republican";
			}
			else {
				
				winner = "Democrat";
			}
			System.out.println("State: " + state + " Congress: " + congress
					+ " District: " + districtNumber + " D Votes: " + democratVotes 
					+ " R Votes: " + republicanVotes + " Winner: " + winner);
			
			
		}
	
	}
	
}
