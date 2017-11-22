package mahogany.metrics;

import java.util.Map;

public class GerrymanderData<T extends DistrictData<?>> {
	private TestNames testName;
	private Integer totalRepublicanVotes;
	private Integer totalDemocratVotes;
	private Integer totalRepublicanSeats;
	private Integer totalDemocratSeats;
	private Map<String, T> districtData;
	
	public TestNames getTestName() {
		return testName;
	}
	
	public void setTestName(TestNames testName) {
		this.testName = testName;
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
	
	public Map<String, T> getDistrictData(){
		return districtData;
	}
	
	public void setDistrictData(Map<String, T> districtData){
		this.districtData = districtData;
	}
	
}
