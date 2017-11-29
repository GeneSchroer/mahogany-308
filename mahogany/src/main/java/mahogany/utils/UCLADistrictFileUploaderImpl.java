package mahogany.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.wololo.jts2geojson.GeoJSONReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.MultiPolygon;

import mahogany.entities.Boundaries;
import mahogany.entities.Boundaries;
import mahogany.entities.Districts;
import mahogany.entities.Elections;
import mahogany.entities.MemberNames;
import mahogany.entities.Members;
import mahogany.entities.Parties;
import mahogany.entities.StateNames;
import mahogany.repositories.BoundariesRepository;
import mahogany.repositories.BoundariesRepository;
import mahogany.repositories.DistrictsRepository;
import mahogany.repositories.ElectionsRepository;
import mahogany.repositories.MemberNamesRepository;
import mahogany.repositories.MembersRepository;
import mahogany.repositories.PartiesRepository;
import mahogany.repositories.StateNamesRepository;

@Component
public class UCLADistrictFileUploaderImpl {

	@Autowired DistrictsRepository districtsRepo;
	@Autowired StateNamesRepository stateNamesRepo;
	@Autowired MembersRepository membersRepo;
	@Autowired MemberNamesRepository memberNamesRepo;
	@Autowired PartiesRepository partiesRepo;
	@Autowired BoundariesRepository boundariesRepo;
	@Autowired ElectionsRepository electionsRepo;
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
		GeoJSONReader geoJsonReader = new GeoJSONReader();

		
		for(int index=0; index<districts.size(); ++index) {
			ObjectNode districtPropertiesObject = (ObjectNode) districts.get(index).get("properties");
			ObjectNode districtGeometryObject = (ObjectNode) districts.get(index).get("geometry");
			
			MultiPolygon multiPolygon = (MultiPolygon) geoJsonReader.read(districtGeometryObject.toString());

			
			
			Boundaries boundariesEntity = boundariesRepo.findByCoordinates(multiPolygon);
			if(boundariesEntity == null) {
				System.out.println("Creating new Boundaries entity");
				boundariesEntity = new Boundaries();
				boundariesEntity.setCoordinates(multiPolygon);
				boundariesRepo.save(boundariesEntity);
			}
			//System.out.println(districtObject.toString());
			String stateName = districtPropertiesObject.get("statename").asText();
			
			StateNames stateNamesEntity = stateNamesRepo.findByName(stateName);
			if (stateNamesEntity == null) {
				stateNamesEntity = new StateNames();
				stateNamesEntity.setName(stateName);
				stateNamesRepo.save(stateNamesEntity);
			}
			
			int startCongress = districtPropertiesObject.get("startcong").asInt();
			int endCongress = districtPropertiesObject.get("endcong").asInt();
			int districtNumber = districtPropertiesObject.get("district").asInt();
			
			System.out.println("Reading district " + districtNumber 
								+ " for state " + stateName
								+ " from " + startCongress + " to " + endCongress + ".");
			
			// Data for all members of congress during this time period
			ObjectNode sessionListObject= (ObjectNode)districtPropertiesObject.get("member");
			
			for(int currentCongress = startCongress; currentCongress <= endCongress; ++currentCongress) {
				System.out.println("Creating Districts Entity for session " + currentCongress);
				
				
				Integer raceYear = 1786 + (2 * currentCongress);
				
				Districts districtsEntity = districtsRepo.findByStateEntityAndDistrictNumberAndYear(stateNamesEntity, districtNumber, raceYear);
				if(districtsEntity == null) {	
					districtsEntity = new Districts();
					districtsEntity.setYear(raceYear);
					districtsEntity.setDistrictNumber(districtNumber);
					districtsEntity.setStateName(stateNamesEntity);
				}
				System.out.println(districtsEntity.getMembers().toString());
				districtsEntity.setBoundaries(boundariesEntity);
				districtsRepo.save(districtsEntity);
				System.out.println("Getting congressional members for district " + districtNumber + ", session " + currentCongress);
				// list of all congressmen from this district during this session of congress
				Iterator<JsonNode> memberIterator = sessionListObject.get(String.valueOf(currentCongress)).iterator();
				List<Members> memberList = new ArrayList<Members>();
				
				while(memberIterator.hasNext()) {
					ObjectNode memberData = (ObjectNode)memberIterator.next();
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
					Members membersEntity = membersRepo.findByMemberNameAndPartyAndDistrict(memberNamesEntity, partiesEntity, districtsEntity);
					if(membersEntity == null) {
						membersEntity = new Members();
						membersEntity.setDistrict(districtsEntity);
						membersEntity.setMemberName(memberNamesEntity);
						membersEntity.setParty(partiesEntity);
						membersRepo.save(membersEntity);
					}
					Elections electionsEntity = electionsRepo.findByDistrict(districtsEntity);
					if(electionsEntity != null) {
						electionsEntity.setWinner(membersEntity);
						electionsEntity.setDistrict(districtsEntity);
						electionsRepo.save(electionsEntity);
					}
					memberList.add(membersEntity);
				}
				districtsEntity.setMembers(memberList);
				System.out.println(districtsEntity.getMembers().toString());
				districtsRepo.save(districtsEntity);
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
