package mahogany.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONReader;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import mahogany.entities.Boundaries;
import mahogany.entities.Districts;
import mahogany.entities.StateNames;
import mahogany.repositories.BoundariesRepository;
import mahogany.repositories.DistrictsRepository;
import mahogany.repositories.StateNamesRepository;

@Component
public class usCensusDataUploader {
	@Autowired DistrictsRepository districtsRepo;
	@Autowired StateNamesRepository stateNamesRepo;
	@Autowired BoundariesRepository boundariesRepo;
	
	
	public void uploadJsonToDatabase(ObjectNode fileJsonObject) {
		
		// used to convert district boundaries to jts spatial data
		GeoJSONReader geoJsonReader = new GeoJSONReader();
		
		
		// access the array of district information
		ArrayNode geoJsonFeaturesArray = (ArrayNode)fileJsonObject.get("features");
		Integer congressSession = geoJsonFeaturesArray.get(0).get("properties").get("CDSESSN").asInt();
		Integer year = 1786 + 2*congressSession;
		
		for(int index = 0; index<geoJsonFeaturesArray.size(); ++index) {
			// contains district boundaries
			ObjectNode districtGeometryObject = (ObjectNode)geoJsonFeaturesArray.get(index).get("geometry");
			/* find or creating an entity for the boundaries of the district */
			
			GeometryFactory geoFactory = new GeometryFactory();
			
			MultiPolygon multiPolygon;
			String geometryType = districtGeometryObject.get("type").asText();
			
			if(geometryType.equals("MultiPolygon")) {
				multiPolygon = (MultiPolygon) geoJsonReader.read(districtGeometryObject.toString());
			}
			else {
				Polygon polygon = (Polygon)geoJsonReader.read(districtGeometryObject.toString());
				Polygon[] polygonArray= new Polygon[1];
				polygonArray[0] = polygon;
				multiPolygon = geoFactory.createMultiPolygon(polygonArray);
			}		
			//contains information about the district
			ObjectNode districtPropertiesObject = (ObjectNode)geoJsonFeaturesArray.get(index).get("properties");
			String geoIdString = districtPropertiesObject.get("GEOID").asText();
			Integer districtNumber = parseCongressionalDistrict(geoIdString);
			
			Integer stateCode = districtPropertiesObject.get("STATEFP").asInt();
			String stateName = stateCodeToStateName(stateCode);
			
			System.out.println("State Name: " + stateName + " District Number: " + districtNumber + " Year: " + year);
			
			if(districtNumber != null && stateName != null) {
				StateNames state = stateNamesRepo.findByName(stateName);
				if(state == null) {
					state = new StateNames();
					state.setName(stateName);
					stateNamesRepo.save(state);
				}
				
				Boundaries boundaries = boundariesRepo.findByCoordinates(multiPolygon);
				if(boundaries == null) {
					boundaries = new Boundaries();
					boundaries.setCoordinates(multiPolygon);
					boundariesRepo.save(boundaries);
				}
				Districts district = districtsRepo.findByStateNameAndDistrictNumberAndYear(stateName, districtNumber, year);
				if(district == null) {
					district = new Districts();
					district.setStateName(state);
					district.setDistrictNumber(districtNumber);
					district.setYear(year);
				}
				district.setBoundaries(boundaries);
				districtsRepo.save(district);
			}
			
		}
		
	}
	
	public Integer parseCongressionalDistrict(String geoIdString) {
		String districtCode = geoIdString.substring(2);
		if(districtCode.equals("ZZ") || districtCode.equals("98")) {
			return null;
		}
		else {
			Integer districtNumber= Integer.parseInt(districtCode);
			return districtNumber;
		}
	}

	public String stateCodeToStateName(Integer stateCode) {
		StateNamesEnum stateEnum = StateNamesEnum.getByStateCode(stateCode);
		if(stateEnum == null) {
			return null;
		}
		else {
			return stateEnum.getName();
		}
	}
}
