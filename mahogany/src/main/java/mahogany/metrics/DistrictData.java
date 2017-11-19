package mahogany.metrics;

import java.util.Map;

public class DistrictData <T extends VoteData> {
	
	String winningParty;
	Map<String, T> voteData;
	

	public String getWinningParty() {
		return winningParty;
	}
	
	public void setWinningParty(String winningParty) {
		this.winningParty = winningParty;
	}
	
	public Map<String, T> getVoteData() {
		return voteData;
	}

	public void setVoteData(Map<String, T> voteData) {
		this.voteData = voteData;
	}



	
}
