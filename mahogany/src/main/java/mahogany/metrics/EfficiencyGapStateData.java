package mahogany.metrics;

public class EfficiencyGapStateData extends AbstractStateData<ElectionDistrictData<WastedVoteData>>{
	private Float efficiencyGap;
	private Integer totalRepublicanVotes;
	private Integer totalDemocratVotes;
	private Integer totalRepublicanSeats;
	private Integer totalDemocratSeats;
	
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
