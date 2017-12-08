package mahogany.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import mahogany.entities.Districts;
import mahogany.entities.Elections;
import mahogany.entities.Members;
import mahogany.entities.Parties;
import mahogany.entities.StateNames;
import mahogany.entities.Votes;
import mahogany.repositories.DistrictsRepository;
import mahogany.repositories.ElectionsRepository;
import mahogany.repositories.MemberNamesRepository;
import mahogany.repositories.MembersRepository;
import mahogany.repositories.PartiesRepository;
import mahogany.repositories.StateNamesRepository;
import mahogany.repositories.VotesRepository;

@Component
public class PrincetonElectionDataUploaderImpl implements ElectionDataUploader{

	@Autowired DistrictsRepository districtsRepo;
	@Autowired StateNamesRepository stateNamesRepo;
	@Autowired MembersRepository membersRepo;
	@Autowired MemberNamesRepository memberNamesRepo;
	@Autowired PartiesRepository partiesRepo;
	@Autowired ElectionsRepository electionsRepo;
	@Autowired VotesRepository votesRepo;
	
	
	public Workbook convertFileToExcelWorkbook(MultipartFile file) throws IOException {
		FileInputStream input = (FileInputStream)file.getInputStream();
		Workbook workbook = new HSSFWorkbook(input);
		
		return workbook;
	}
	
	
	@SuppressWarnings("deprecation")
	public void uploadDataToDatabase(Workbook workbook) {
		Sheet firstSheet = workbook.getSheetAt(0);
		
		for(Row row: firstSheet) {
			
			//The first row is just the header,
			// so skip it
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
			Integer districtNumber = (int)(districtNumberCell.getNumericCellValue());
			Integer republicanVotes;
			Integer democratVotes; 
			Float democratVotesPercentage;
			
			// Some Vote cells have "Unopposed"
			// instead of a vote.
			// There's no way to determine the actual vote count from here,
			// So set the votes to 0 for the time being.

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
			
			// This data only contains information from
			// the two main parties.
			// We cannot determine the republican percentage of the votes
			// because there may have been runners from other parties.
			democratVotesPercentage = (float)democratPercentageCell.getNumericCellValue();
	//		System.out.println(democratVotesPercentage);
			
			String winningParty = "";
			
			
			if(0.5 > democratVotesPercentage) {
				winningParty = "Republican";
			}
			else {
				winningParty = "Democrat";
			}

			// begin the process of storing data into entities,
			// and persisting them into the database.
			
			StateNames stateNamesEntity = stateNamesRepo.findByName(stateName);
			if(stateNamesEntity == null) {
				stateNamesEntity = new StateNames();
				stateNamesEntity.setName(stateName);
				stateNamesRepo.save(stateNamesEntity);
			}
			
			Districts districtsEntity = districtsRepo.findByStateNameAndDistrictNumberAndYear(stateName, districtNumber, raceYear);
			if(districtsEntity == null) {
				districtsEntity = new Districts();
				districtsEntity.setStateName(stateNamesEntity);
				districtsEntity.setYear(raceYear);
				districtsEntity.setDistrictNumber(districtNumber);
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
				electionsEntity.setTotalVotes(0);
				electionsEntity.setVotes(new HashMap<Long, Votes>());
				Members membersEntity = membersRepo.findFirstByDistrict(districtsEntity);
				if(membersEntity != null) {
					electionsEntity.setWinner(membersEntity);
				}
				// Change the data for the current Election
				if(winningParty.equals("Republican")) {
					electionsEntity.setParty(republicanPartyEntity);
				}
				else {
					electionsEntity.setParty(democratPartyEntity);
				}
				electionsRepo.save(electionsEntity);
			}
			
			Votes democratVotesEntity = votesRepo.findByDistrictAndParty(districtsEntity, democratPartyEntity);
			if(democratVotesEntity == null) {
				democratVotesEntity = new Votes();
				democratVotesEntity.setParty(democratPartyEntity);
				democratVotesEntity.setElection(electionsEntity);
				democratVotesEntity.setVotes(democratVotes);
				votesRepo.save(democratVotesEntity);
			}
			//System.out.println(democratVotesEntity.getId());
			democratVotesEntity.setVotes(democratVotes);
			votesRepo.save(democratVotesEntity);
			//System.out.println(democratVotesEntity.getId());
			
			Votes republicanVotesEntity = votesRepo.findByDistrictAndParty(districtsEntity, republicanPartyEntity);
			if(republicanVotesEntity == null) {
				republicanVotesEntity = new Votes();
				republicanVotesEntity.setParty(republicanPartyEntity);
				republicanVotesEntity.setElection(electionsEntity);
				republicanVotesEntity.setVotes(republicanVotes);
				votesRepo.save(republicanVotesEntity);
			}
			//System.out.println(republicanVotesEntity.getId());
			republicanVotesEntity.setVotes(republicanVotes);
			votesRepo.save(republicanVotesEntity);
			//System.out.println(republicanVotesEntity.getId());
			
			
			electionsEntity.getVotes().put(republicanPartyEntity.getId(), republicanVotesEntity);
			electionsEntity.getVotes().put(democratPartyEntity.getId(), democratVotesEntity);
			
			Integer totalVotes = 0;
			for(Entry<Long,Votes> entry: electionsEntity.getVotes().entrySet()) {
			//	System.out.println(entry.getValue().getParty().getName());
				totalVotes += entry.getValue().getVotes();
			}
			
			electionsEntity.setTotalVotes(totalVotes);
			
			if(((float)republicanVotes/totalVotes) > 0.5) {
				electionsEntity.setParty(republicanPartyEntity);
			}
			else if(((float)democratVotes/totalVotes) > 0.5){
				electionsEntity.setParty(democratPartyEntity);
			}
			electionsRepo.save(electionsEntity);
			
		}
	
	}
	
}
