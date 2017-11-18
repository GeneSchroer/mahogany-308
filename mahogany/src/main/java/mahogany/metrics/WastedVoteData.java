package mahogany.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WastedVoteData extends VoteData {
	
	
	Integer wastedVotes;
	public Integer getWastedVotes() {
		return wastedVotes;
	}
	public void setWastedVotes(Integer wastedVotes) {
		this.wastedVotes = wastedVotes;
	}
	
	/*public JsonNode toJsonNode() {
		ObjectNode jsonWastedVotes= (ObjectNode)super.toJsonNode();
		jsonWastedVotes.put("republicanWastedVotes", republicanWastedVotes);
		jsonWastedVotes.put("democratWastedVotes", democratWastedVotes);
		
		return jsonWastedVotes;
	}*/
	
}
