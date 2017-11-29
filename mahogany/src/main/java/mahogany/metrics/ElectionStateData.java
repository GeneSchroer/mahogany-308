package mahogany.metrics;

public class ElectionStateData extends AbstractStateData<ElectionDistrictData<VoteData>> {
	private Integer totalRepublicanVotes;
	private Integer totalDemocratVotes;
	private Integer totalRepublicanSeats;
	private Integer totalDemocratSeats;
	
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
