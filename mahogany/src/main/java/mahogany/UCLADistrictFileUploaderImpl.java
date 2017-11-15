package mahogany;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mahogany.Districts;

@Component
public class UCLADistrictFileUploaderImpl {

	@Autowired DistrictsRepository districtsRepo;
	@Autowired StateNamesRepository stateNamesRepo;
	@Autowired MembersRepository membersRepo;
	@Autowired MemberNamesRepository memberNamesRepo;
	@Autowired PartiesRepository partiesRepo;
	
	public JsonNode convertFileToJsonNode(MultipartFile file) throws IOException {
		
		/* used to convert the file from a jsonStrig to a JsonNode object*/
		ObjectMapper jsonNodeMapper = new ObjectMapper();
		ObjectReader jsonNodeReader = jsonNodeMapper.reader();
		
		System.out.println("Converting file: " + file.getOriginalFilename() );
		
		/* this line can throw an IOException*/
		String fileAsJsonString = new String(file.getBytes());
		ObjectNode fileAsJsonNode = (ObjectNode)jsonNodeReader.readTree(fileAsJsonString);
		
		return fileAsJsonNode;
	}
	
	public ArrayList<Districts> uploadJsonToDatabase(ObjectNode fileJsonNode) throws IOException{
		
		ArrayList<Districts> districtList = new ArrayList<Districts>();
		System.out.println("Going through list of districts");
		ArrayNode districts = (ArrayNode)fileJsonNode.get("features");
		
		
		for(int index=0; index<districts.size(); ++index) {
			ObjectNode districtObject = (ObjectNode) districts.get(index).get("properties");
			//System.out.println(districtObject.toString());
			String stateName = districtObject.get("statename").asText();
			
			StateNames stateNamesEntity = stateNamesRepo.findByName(stateName);
			if (stateNamesEntity == null) {
				stateNamesEntity = new StateNames();
				stateNamesEntity.setName(stateName);
				stateNamesRepo.save(stateNamesEntity);
			}
			
			int startCongress = districtObject.get("startcong").asInt();
			int endCongress = districtObject.get("endcong").asInt();
			int districtNumber = districtObject.get("district").asInt();
			
			System.out.println("Reading district " + districtNumber 
								+ " for state " + stateName
								+ " from " + startCongress + " to " + endCongress + ".");
			
			// Data for all members of congress during this time period
			ObjectNode sessionListObject= (ObjectNode)districtObject.get("member");
			
			for(int currentCongress = startCongress; currentCongress <= endCongress; ++currentCongress) {
				System.out.println("Creating Districts Entity for session " + currentCongress);
				
				
				Districts districtsEntity = districtsRepo.findByStateEntityAndNumberAndCongress(stateNamesEntity, districtNumber, currentCongress);
				if(districtsEntity == null) {	
					districtsEntity = new Districts();
					districtsEntity.setCongress(currentCongress);
					districtsEntity.setNumber(districtNumber);
					districtsEntity.setStateName(stateNamesEntity);
					//districtsEntity.setStateId(stateNamesEntity.getId());
					districtsRepo.save(districtsEntity);
				}
				System.out.println("Getting congressional members for district " + districtNumber + ", session " + currentCongress);
				// list of all congressmen from this district during this session of congress
				Iterator<JsonNode> memberList = sessionListObject.get(String.valueOf(currentCongress)).iterator();
				
				while(memberList.hasNext()) {
					ObjectNode memberData = (ObjectNode)memberList.next();
					String party = memberData.get("party").asText();
					// member name is lastname, firstname mi
					String rawMemberName = memberData.get("name").asText("N/A");
					String memberName = formatName(rawMemberName);
					
					System.out.println("Member Details - Name: " + memberName + ", Party: " + party);
					
					Parties partiesEntity = partiesRepo.findByName(party);
					if (partiesEntity == null) {		
						partiesEntity = new Parties();
						partiesEntity.setName(party);
						partiesRepo.save(partiesEntity);
					}
					
					MemberNames memberNamesEntity = memberNamesRepo.findByName(memberName);
					if(memberNamesEntity == null) {
						memberNamesEntity =  new MemberNames();
						memberNamesEntity.setName(memberName);
						memberNamesRepo.save(memberNamesEntity);
					}
					Members membersEntity = membersRepo.findByNameIdAndPartyId(memberNamesEntity.getId(), partiesEntity.getId());
					if(membersEntity == null) {
						membersEntity = new Members();
						membersEntity.setPartyId(partiesEntity.getId());
						membersEntity.setNameId(memberNamesEntity.getId());
						membersEntity.setDistrictId(districtsEntity.getId());
						membersRepo.save(membersEntity);
					}
				}
				districtList.add(districtsEntity);
			}
			
		}
		
		return districtList;
	}
	public String formatName(String rawName) {
		String formattedName="";
		
		int firstNameIndex = rawName.indexOf(", ");
		String firstName = rawName.substring(firstNameIndex+2);
		System.out.println("First Name: " + firstName);
		
		String lastName = rawName.substring(0, firstNameIndex);
		System.out.println("Last Name: " + lastName);
		
		formattedName = firstName + " " + lastName;
	
		return formattedName;
		
	}
}
