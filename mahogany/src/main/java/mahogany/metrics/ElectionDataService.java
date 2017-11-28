package mahogany.metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mahogany.entities.Elections;
import mahogany.entities.Votes;

public class ElectionDataService implements AlgorithmTest<ElectionStateData> {

	@Override
	public ElectionStateData generateMetricData(List<Elections> electionList) {
		ElectionStateData electionDataResults = new ElectionStateData();
		Map<String, ElectionDistrictData<VoteData>> districtDataMap = new HashMap<String, ElectionDistrictData<VoteData>>();
		
		Integer totalDemocratVotes = 0;
		Integer totalDemocratSeats = 0;
		Integer totalRepublicanVotes = 0;
		Integer totalRepublicanSeats = 0;
		
		for(Elections election: electionList) {
			ElectionDistrictData<VoteData> districtData = new ElectionDistrictData<VoteData>();
			Map<String, VoteData> voteMap = new HashMap<String, VoteData>();

			
			Integer democratVotes = 0;
			Float democratPercent = 0.0f;
			Integer republicanVotes = 0;
			Float republicanPercent = 0.0f;
			if(election.getParty().getName().equals("Democrat")) {
				++totalDemocratSeats;
				districtData.setWinningParty("Democrat");
			}
			else if (election.getParty().getName().equals("Republican")) {
				++totalRepublicanSeats;
				districtData.setWinningParty("Republican");
			}
			for(Entry<Long, Votes> entry: election.getVotes().entrySet()) {
				Votes electionVote = entry.getValue();
				VoteData voteData = new VoteData();
				String party = electionVote.getParty().getName();
				
				if(party.equals("Democrat")) {
					democratVotes = electionVote.getVotes();
					totalDemocratVotes += democratVotes;
					
					voteData.setVotes(democratVotes);
					voteData.setPercentage(democratPercent);
					
					voteMap.put("Democrat", voteData);
				}
				else if(party.equals("Republican")) {
					republicanVotes = electionVote.getVotes();
					totalRepublicanVotes += republicanVotes;

					
					voteData.setVotes(republicanVotes);
					voteData.setPercentage(republicanPercent);
					
					voteMap.put("Republican", voteData);
				}
			}
			districtData.setVoteData(voteMap);
			districtDataMap.put(election.getDistrict().getId().toString(), districtData);
			
			
		}
		
		electionDataResults.setDistrictData(districtDataMap);
		electionDataResults.setTotalDemocratVotes(totalDemocratVotes);
		electionDataResults.setTotalRepublicanVotes(totalRepublicanVotes);
		electionDataResults.setTotalDemocratSeats(totalDemocratSeats);
		electionDataResults.setTotalRepublicanSeats(totalRepublicanSeats);
		electionDataResults.setTestName(MetricOption.ELECTION_DATA.toString());

		
		return electionDataResults;
	}

}
