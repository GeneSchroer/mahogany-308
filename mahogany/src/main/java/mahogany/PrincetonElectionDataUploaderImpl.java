package mahogany;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
			if(row.getRowNum() == 0) {
				continue;
			}
			Cell stateCell = row.getCell(0);
			Cell raceYearCell = row.getCell(1);
			Cell districtNumberCell = row.getCell(2);
			Cell republicanVotesCell = row.getCell(3);
			Cell democratVotesCell = row.getCell(5);
			Cell democratPercentageCell = row.getCell(7);
			
			
			String state = stateCell.getStringCellValue();
			
			Integer raceYear = (int) (raceYearCell.getNumericCellValue());
			Integer congress = (raceYear- 1786) / 2;
			
			Integer districtNumber = (int)(districtNumberCell.getNumericCellValue());
			
			Integer republicanVotes;
			Integer democratVotes; 
			
			if(republicanVotesCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				republicanVotes = (int)(republicanVotesCell.getNumericCellValue());
			}
			else {
				republicanVotes = 0;
			}
			
			if(democratVotesCell.getCellType() == Cell.CELL_TYPE_NUMERIC){
				democratVotes = (int)(democratVotesCell.getNumericCellValue());
			}
			else {
				democratVotes = 0;
			}
				
			Float democratVotePercentage = (float)(democratPercentageCell.getNumericCellValue());
			Float republicanVotePercentage = 1 - democratVotePercentage;
			
			
			String electionWinner = "";
			if(democratVotePercentage < 0.5) {
				electionWinner = "Republican";
			}
			else {
				electionWinner = "Democrat";
			}

			System.out.println("State: " + state + " Congress: " + congress
					+ " District: " + districtNumber + " D Votes: " + democratVotes 
					+ " R Votes: " + republicanVotes + " Winner: " + electionWinner);
			
			
		}
	
	}
	
}
