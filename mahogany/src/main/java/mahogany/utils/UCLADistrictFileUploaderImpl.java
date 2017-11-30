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
		
		// part of the geojson file containing the district properties,
		// including boundaries.
		ArrayNode featuresArray = (ArrayNode)fileJsonNode.get("features");
		
		GeoJSONReader geoJsonReader = new GeoJSONReader();

		// iterate through the array and persist data as entities.
		for(int index=0; index<featuresArray.size(); ++index) {
			// All the non-boundary properties of the district
			ObjectNode districtPropertiesObject = (ObjectNode) featuresArray.get(index).get("properties");
			
			ObjectNode districtGeometryObject = (ObjectNode) featuresArray.get(index).get("geometry");
			
			/* find or creating an entity for the boundaries of the district */
			com.vividsolutions.jts.geom.MultiPolygon multiPolygon = (MultiPolygon) geoJsonReader.read(districtGeometryObject.toString());
			Boundaries boundariesEntity = boundariesRepo.findByCoordinates(multiPolygon);
			if(boundariesEntity == null) {
				System.out.println("Creating new Boundaries entity");
				System.out.println(multiPolygon.toString());
				boundariesEntity = new Boundaries();
				boundariesEntity.setCoordinates(multiPolygon);
				boundariesRepo.save(boundariesEntity);
			}
	
			// find or create an entity for the name of the state.
			String stateName = districtPropertiesObject.get("statename").asText();
			StateNames stateNamesEntity = stateNamesRepo.findByName(stateName);
			if (stateNamesEntity == null) {
				stateNamesEntity = new StateNames();
				stateNamesEntity.setName(stateName);
				stateNamesRepo.save(stateNamesEntity);
			}
			

			// The sessions of congress this data concerns,
			// as well as the district number
			int startCongress = districtPropertiesObject.get("startcong").asInt();
			int endCongress = districtPropertiesObject.get("endcong").asInt();
			int districtNumber = districtPropertiesObject.get("district").asInt();

			
			/* get the year of this congress and the congressional representatives */

			
			// Data for all members of congress during this time period
			ObjectNode memberListObject= (ObjectNode)districtPropertiesObject.get("member");
			
			for(int currentCongress = startCongress; currentCongress <= endCongress; ++currentCongress) {
				
				// The first session of congress was in 1786,
				// And each session of congress lasts two years,
				// So it's easy to find was year each session starts.
				Integer raceYear = 1786 + (2 * currentCongress);
				
				// get or create the chosen district
				Districts districtsEntity = districtsRepo.findByStateEntityAndDistrictNumberAndYear(stateNamesEntity, districtNumber, raceYear);
				if(districtsEntity == null) {	
					districtsEntity = new Districts();
					districtsEntity.setYear(raceYear);
					districtsEntity.setDistrictNumber(districtNumber);
					districtsEntity.setStateName(stateNamesEntity);
				}

				// Update the district boundaries.
				districtsEntity.setBoundaries(boundariesEntity);
				districtsRepo.save(districtsEntity);
				
				// list of all congressmen from this district during this session of congress
				Iterator<JsonNode> memberIterator = memberListObject.get(String.valueOf(currentCongress)).iterator();
				List<Members> memberList = new ArrayList<Members>();
				
				while(memberIterator.hasNext()) {
					ObjectNode memberData = (ObjectNode)memberIterator.next();
					String party = memberData.get("party").asText();
					
					// member name initially formatted as lastname, firstname mi
					// so we need to convert it.
					String rawMemberName = memberData.get("name").asText("N/A");
					String memberName = formatName(rawMemberName);
					
					// get or create a Parties entity to persist
					Parties partiesEntity = partiesRepo.findByName(party);
					if (partiesEntity == null) {		
						partiesEntity = new Parties();
						partiesEntity.setName(party);
						partiesRepo.save(partiesEntity);
					}
					
					// get or create a MemberNames entity to persist.
					MemberNames memberNamesEntity = memberNamesRepo.findByName(memberName);
					if(memberNamesEntity == null) {
						memberNamesEntity =  new MemberNames();
						memberNamesEntity.setName(memberName);
						memberNamesRepo.save(memberNamesEntity);
					}
					
					// get or create a Members entity to persist.
					Members membersEntity = membersRepo.findByMemberNameAndPartyAndDistrict(memberNamesEntity, partiesEntity, districtsEntity);
					if(membersEntity == null) {
						membersEntity = new Members();
						membersEntity.setDistrict(districtsEntity);
						membersEntity.setMemberName(memberNamesEntity);
						membersEntity.setParty(partiesEntity);
						membersRepo.save(membersEntity);
					}
					
					// get or create an Elections entity to persist
					Elections electionsEntity = electionsRepo.findByDistrict(districtsEntity);
					if(electionsEntity != null) {
						electionsEntity.setWinner(membersEntity);
						electionsEntity.setDistrict(districtsEntity);
						electionsRepo.save(electionsEntity);
					}
					memberList.add(membersEntity);
				}
				
				districtsEntity.setMembers(memberList);
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
