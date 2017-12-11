package mahogany.metrics;

public class EfficiencyGapStateData extends AbstractStateData<ElectionDistrictData<WastedVoteData>>{
	private Integer totalRepublicanVotes;
	private Integer totalDemocratVotes;
	private Integer totalRepublicanSeats;
	private Integer totalDemocratSeats;
	Float republicanGap = 0.0f;
	Float democratGap = 0.0f;
	
	
	public Float getRepublicanGap() {
		return republicanGap;
	}

	public void setRepublicanGap(Float republicanEfficiencyGap) {
		this.republicanGap = republicanEfficiencyGap;
	}

	public Float getDemocratGap() {
		return democratGap;
	}

	public void setDemocratGap(Float democratEfficiencyGap) {
		this.democratGap = democratEfficiencyGap;
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
