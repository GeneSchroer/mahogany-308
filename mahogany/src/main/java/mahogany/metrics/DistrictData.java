package mahogany.metrics;

import java.lang.reflect.Field;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DistrictData <T extends VoteData> {
	
	Map<String, T> voteData;
	
	
	
	public Map<String, T> getVoteData() {
		return voteData;
	}



	public void setVoteData(Map<String, T> voteData) {
		this.voteData = voteData;
	}



	public JsonNode toJsonNode() {
		ObjectNode jsonDistrictData = new ObjectMapper().createObjectNode(); 
		/*jsonDistrictData.put("republicanVotes", republicanVotes);
		jsonDistrictData.put("democratVotes", democratVotes);
		jsonDistrictData.put("republicanPercentage", republicanPercentage);
		jsonDistrictData.put("democratPercentage", democratPercentage);
		*/
		return jsonDistrictData;
	}
	
}
