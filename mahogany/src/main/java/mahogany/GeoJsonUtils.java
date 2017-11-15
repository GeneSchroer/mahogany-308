package mahogany;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class GeoJsonUtils {
	
	@Autowired
	private StateNamesRepository stateNamesRepository;
	
	
	public JsonNode createDistrictBoundariesJson(List<Districts> districtList) {
		ObjectNode districtsJson = new ObjectMapper().createObjectNode();
		
		districtsJson.put("type", "FeaturesCollection");
		ArrayNode featuresArray = districtsJson.putArray("features");
		
		for(Districts district: districtList) {
			
		}
		
		return (JsonNode) districtsJson;
	}
	
	
	
	
	public Integer getStateCode(String stateName) {
		return 	stateName.equals(StateNamesEnum.Alabama.getName()) ? StateNamesEnum.Alabama.getCode()
				: stateName.equals(StateNamesEnum.Alaska.getName()) ? StateNamesEnum.Alaska.getCode()	
				: stateName.equals(StateNamesEnum.Arizona.getName()) ? StateNamesEnum.Arizona.getCode()
				: stateName.equals(StateNamesEnum.Arkansas.getName()) ? StateNamesEnum.Arkansas.getCode() 
				: stateName.equals(StateNamesEnum.California.getName()) ? StateNamesEnum.California.getCode()
				: stateName.equals(StateNamesEnum.Colorado.getName()) ? StateNamesEnum.Colorado.getCode()				
				: stateName.equals(StateNamesEnum.Connecticut.getName()) ? StateNamesEnum.Connecticut.getCode()		
				: stateName.equals(StateNamesEnum.Delaware.getName()) ? StateNamesEnum.Delaware.getCode()
				: stateName.equals(StateNamesEnum.District_Of_Columbia.getName()) ? StateNamesEnum.District_Of_Columbia.getCode()
				: stateName.equals(StateNamesEnum.Florida.getName()) ? StateNamesEnum.Florida.getCode()
				: stateName.equals(StateNamesEnum.Georgia.getName()) ? StateNamesEnum.Georgia.getCode()
				: stateName.equals(StateNamesEnum.Hawaii.getName()) ? StateNamesEnum.Hawaii.getCode()
				: stateName.equals(StateNamesEnum.Idaho.getName()) ? StateNamesEnum.Idaho.getCode()
																		: stateName.equals(StateNamesEnum.Illinois.getName()) ? StateNamesEnum.Illinois.getCode()
																				: stateName.equals(StateNamesEnum.Indiana.getName()) ? StateNamesEnum.Indiana.getCode()
																						: stateName.equals(StateNamesEnum.Iowa.getName()) ? StateNamesEnum.Iowa.getCode()
																								: stateName.equals(StateNamesEnum.Kansas.getName()) ? StateNamesEnum.Kansas.getCode()
																										: stateName.equals(StateNamesEnum.Alabama.getName()) ? StateNamesEnum.Alabama.getCode()
																												: stateName.equals(StateNamesEnum.Alabama.getName()) ? StateNamesEnum.Alabama.getCode()
																														: stateName.equals(StateNamesEnum.Alabama.getName()) ? StateNamesEnum.Alabama.getCode()
																																: stateName.equals(StateNamesEnum.Alabama.getName()) ? StateNamesEnum.Alabama.getCode()
																																		: stateName.equals(StateNamesEnum.Alabama.getName()) ? StateNamesEnum.Alabama.getCode()
																																				: stateName.equals(StateNamesEnum.Alabama.getName()) ? StateNamesEnum.Alabama.getCode()
																																						: stateName.equals(StateNamesEnum.Alabama.getName()) ? StateNamesEnum.Alabama.getCode()
						:null;
		
		
	}
}