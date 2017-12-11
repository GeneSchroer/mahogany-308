package mahogany.utils;

import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONReader;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import mahogany.entities.Boundaries;

@Component
public class usCensusDataUploader {

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
			String districtNumberString = districtPropertiesObject.get("NAMELSAD").asText();
			Integer districtNumber = parseCongressionalDistrict(districtNumberString);
			
			Integer stateCode = districtPropertiesObject.get("STATEFP").asInt();
			String stateName = stateCodeToStateName(stateCode);
			
			System.out.println("State Name: " + stateName + " District Number: " + districtNumber + " Year: " + year);
		
		}
		
	}
	
	public Integer parseCongressionalDistrict(String districtString) {
		if(districtString.contains("(at Large)")) {
			return 1;
		}
		else {
			String districtNumberString = districtString.replace("Congressional District ", "");
			return Integer.parseInt(districtNumberString);
		}
	}

	public String stateCodeToStateName(Integer stateCode) {
		return StateNamesEnum.getByStateCode(stateCode).getName();
	}
}
