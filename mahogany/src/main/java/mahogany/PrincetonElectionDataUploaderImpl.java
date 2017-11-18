package mahogany;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	@Autowired VotesRepository votesRepo;
	
	
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
			
			
			String stateName = stateCell.getStringCellValue();
			
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
			
			
			String winningParty = "";
			if(democratVotePercentage < 0.5) {
				winningParty = "Republican";
			}
			else {
				winningParty = "Democrat";
			}

			//System.out.println("State: " + state + " Congress: " + congress
				//	+ " District: " + districtNumber + " D Votes: " + democratVotes 
					//+ " R Votes: " + republicanVotes + " Winner: " + electionWinner);
			StateNames stateNamesEntity = stateNamesRepo.findByName(stateName);
			if(stateNamesEntity == null) {
				stateNamesEntity = new StateNames();
				stateNamesEntity.setName(stateName);
				stateNamesRepo.save(stateNamesEntity);
			}
			
			Districts districtsEntity = districtsRepo.findByStateNameAndNumberAndCongress(stateName, districtNumber, congress);
			if(districtsEntity == null) {
				districtsEntity = new Districts();
				districtsEntity.setStateName(stateNamesEntity);
				districtsEntity.setCongress(congress);
				districtsEntity.setNumber(districtNumber);
				districtsRepo.save(districtsEntity);
			}
			Parties republicanPartyEntity = partiesRepo.findByName("Republican");
			if(republicanPartyEntity == null) {
				republicanPartyEntity = new Parties();
				republicanPartyEntity.setName("Republican");
				partiesRepo.save(republicanPartyEntity);
			}
			Parties democratPartyEntity = partiesRepo.findByName("Democrat");
			if(democratPartyEntity == null) {
				democratPartyEntity = new Parties();
				democratPartyEntity.setName("Democrat");
				partiesRepo.save(democratPartyEntity);
			}
			
			Elections electionsEntity = electionsRepo.findByDistrict(districtsEntity);
			if(electionsEntity == null) {
				electionsEntity = new Elections();
				electionsEntity.setDistrict(districtsEntity);
			}
			
			if(winningParty.equals("Republican")) {
				electionsEntity.setParty(republicanPartyEntity);
			}
			else {
				electionsEntity.setParty(democratPartyEntity);
			}
			
			Members membersEntity = membersRepo.findFirstByDistrict(districtsEntity);
			if(membersEntity != null) {
				electionsEntity.setWinner(membersEntity);
			
			}
			
			electionsEntity.setDistrict(districtsEntity);
			
			electionsRepo.save(electionsEntity);
			
			Votes democratVotesEntity = votesRepo.findByDistrictAndParty(districtsEntity, democratPartyEntity);
			if(democratVotesEntity == null) {
				democratVotesEntity = new Votes();
				democratVotesEntity.setParty(democratPartyEntity);
				democratVotesEntity.setElection(electionsEntity);
			}
			democratVotesEntity.setVotes(democratVotes);
			democratVotesEntity.setPercentage(democratVotePercentage);
			
			Votes republicanVotesEntity = votesRepo.findByDistrictAndParty(districtsEntity, republicanPartyEntity);
			if(republicanVotesEntity == null) {
				republicanVotesEntity = new Votes();
				republicanVotesEntity.setParty(republicanPartyEntity);
				republicanVotesEntity.setElection(electionsEntity);
			}
			republicanVotesEntity.setVotes(republicanVotes);
			republicanVotesEntity.setPercentage(republicanVotePercentage);
			
			List<Votes> electionVoteList = new ArrayList<Votes>();
			
			electionVoteList.add(republicanVotesEntity);
			electionVoteList.add(democratVotesEntity);
			votesRepo.save(electionVoteList);
			
			
			
			electionsEntity.setVotes(electionVoteList);
			
		}
	
	}
	
}
