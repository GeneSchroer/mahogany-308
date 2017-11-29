package mahogany.utils;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.MultiPolygon;

import mahogany.entities.Districts;
import mahogany.entities.Elections;
import mahogany.metrics.AbstractStateData;

@Component
public class GeoJsonUtils {
	
	
	
	public JsonNode createDistrictBoundariesJsonNode(List<Districts> districtList) throws JsonProcessingException, IOException {
		ObjectNode districtsJsonNode = new ObjectMapper().createObjectNode();
		
		districtsJsonNode.put("type", "FeatureCollection");
		ArrayNode featuresArray = districtsJsonNode.putArray("features");
		 
		for(Districts district: districtList) {
			ObjectNode featureNode= featuresArray.addObject();
			
			featureNode.put("type", "Feature");
			
			ObjectNode propertiesNode = featureNode.putObject("properties");
			
			propertiesNode.put("id", district.getId());
			propertiesNode.put("districtNumber", district.getDistrictNumber());
			
			Elections election = district.getElection();
			if(election != null) {
				String winningParty = election.getParty().getName();
				propertiesNode.put("winningParty", winningParty);
			}
			
			featureNode.putObject("geometry");
			
			//String coordinatesString = district.getBoundaries().getCoordinatesString();
			
			MultiPolygon coordinates = district.getBoundaries().getCoordinates();
			GeoJSONWriter writer = new GeoJSONWriter();
			String boundaryString = writer.write(coordinates).toString();
			
			
			ObjectMapper jsonNodeMapper = new ObjectMapper();
			ObjectReader jsonNodeReader = jsonNodeMapper.reader();
			ObjectNode boundaryNode = (ObjectNode)jsonNodeReader.readTree(boundaryString);
			featureNode.replace("geometry", boundaryNode);
		}
		//System.out.println(districtsJsonNode.toString());
		
		return (JsonNode) districtsJsonNode;
	}
	
	public JsonNode generateMetricDataJson(AbstractStateData<?> result) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader();
		String metricResultsJsonString = mapper.writeValueAsString(result);
		JsonNode metricResultJson = reader.readTree(metricResultsJsonString);
		return metricResultJson;
	}
	
}