package mahogany.metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mahogany.entities.Elections;
import mahogany.entities.Votes;

public class ElectionDataService implements AlgorithmTest<GerrymanderData<DistrictData<VoteData>>> {

	@Override
	public GerrymanderData<DistrictData<VoteData>> generateMetricData(List<Elections> electionList) {
		GerrymanderData<DistrictData<VoteData>> electionDataResults = new GerrymanderData<DistrictData<VoteData>>();
		Map<String, DistrictData<VoteData>> districtDataMap = new HashMap<String, DistrictData<VoteData>>();
		
		Integer totalDemocratVotes = 0;
		Integer totalDemocratSeats = 0;
		Integer totalRepublicanVotes = 0;
		Integer totalRepublicanSeats = 0;
		
		for(Elections election: electionList) {
			DistrictData<VoteData> districtData = new DistrictData<VoteData>();
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
			districtDataMap.put(election.getId().toString(), districtData);
			
			
		}
		
		electionDataResults.setDistrictData(districtDataMap);
		electionDataResults.setTotalDemocratVotes(totalDemocratVotes);
		electionDataResults.setTotalRepublicanVotes(totalRepublicanVotes);
		electionDataResults.setTotalDemocratSeats(totalDemocratSeats);
		electionDataResults.setTotalRepublicanSeats(totalRepublicanSeats);
		electionDataResults.setTestName(TestNames.NONE);

		
		return electionDataResults;
	}

}
