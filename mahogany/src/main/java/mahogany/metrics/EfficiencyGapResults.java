package mahogany.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EfficiencyGapResults extends TestResult<DistrictData<WastedVoteData>>{
	private Integer totalRepublicanVotes;
	private Integer totalDemocratVotes;
	private Integer totalRepublicanSeats;
	private Integer totalDemocratSeats;
	private Float efficiencyGap;

	public JsonNode toJsonNode() {
		ObjectNode jsonEfficiencyGap = new ObjectMapper().createObjectNode();
		/*jsonEfficiencyGap.put("efficiencyGap", getEfficiencyGap());
		ObjectNode districtDataNode = jsonEfficiencyGap.putObject("data");
		
		for(WastedVoteData data: getDistrictData()) {
			String districtIdString = data.getDistrictId().toString();
			ObjectNode dataNode = districtDataNode.putObject(districtIdString);
			dataNode.replace(districtIdString, data.toJsonNode());
		}*/
		
		return jsonEfficiencyGap;
	}
	
	public Float getEfficiencyGap() {
		return efficiencyGap;
	}

	public void setEfficiencyGap(Float efficiencyGap) {
		this.efficiencyGap = efficiencyGap;
	}
	
	public Integer getTotalRepublicanVotes() {
		return totalRepublicanVotes;
	}

	public void setTotalRepublicanVotes(Integer totalRepublicanVotes) {
		this.totalRepublicanVotes = totalRepublicanVotes;
	}

	public Integer getTotalDemocratVotes() {
		return totalDemocratVotes;
	}

	public void setTotalDemocratVotes(Integer totalDemocratVotes) {
		this.totalDemocratVotes = totalDemocratVotes;
	}

	public Integer getTotalRepublicanSeats() {
		return totalRepublicanSeats;
	}

	public void setTotalRepublicanSeats(Integer totalRepublicanSeats) {
		this.totalRepublicanSeats = totalRepublicanSeats;
	}

	public Integer getTotalDemocratSeats() {
		return totalDemocratSeats;
	}

	public void setTotalDemocratSeats(Integer totalDemocratSeats) {
		this.totalDemocratSeats = totalDemocratSeats;
	}
	
	
	
}
