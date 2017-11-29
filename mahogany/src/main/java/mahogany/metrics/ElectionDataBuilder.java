package mahogany.metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mahogany.entities.Elections;
import mahogany.entities.Votes;

public class ElectionDataBuilder implements AlgorithmTest<ElectionStateData> {

	@Override
	public ElectionStateData generateMetricData(List<Elections> electionList) {
		
		ElectionStateData electionDataResults = new ElectionStateData();
		Map<String, ElectionDistrictData<VoteData>> districtDataMap = new HashMap<String, ElectionDistrictData<VoteData>>();
		
		Integer totalStateVotes = 0;
		Integer totalSeats = 0;
		
		Integer totalDemocratVotes = 0;
		Integer totalDemocratSeats = 0;
		Integer totalRepublicanVotes = 0;
		Integer totalRepublicanSeats = 0;
		
		//get election data for each district
		for(Elections election: electionList) {
			
			ElectionDistrictData<VoteData> districtData = new ElectionDistrictData<VoteData>();
			Map<String, VoteData> voteDataMap = new HashMap<String, VoteData>();
			Integer democratVotes = 0;
			Float democratPercent = 0.0f;
			Integer republicanVotes = 0;
			Float republicanPercent = 0.0f;
			
			// get total seats won by both parties
			// as well as the winning party for that district.
			if(election.getParty().getName().equals("Democrat")) {
				++totalDemocratSeats;
				districtData.setWinningParty("Democrat");
			}
			else if (election.getParty().getName().equals("Republican")) {
				++totalRepublicanSeats;
				districtData.setWinningParty("Republican");
			}
			
			// collect the total votes each party received
			for(Entry<Long, Votes> entry: election.getVotes().entrySet()) {
				
				Votes electionVote = entry.getValue();
				VoteData voteData = new VoteData();
				String partyName = electionVote.getParty().getName();
				
				// get the votes for the district
				// and add that to the total votes for that party.
				if(partyName.equals("Democrat")) {
					democratVotes = electionVote.getVotes();
					totalDemocratVotes += democratVotes;
					
					voteData.setVotes(democratVotes);
					voteData.setPercentage(democratPercent);
					
					voteDataMap.put("Democrat", voteData);
				}
				else if(partyName.equals("Republican")) {
					republicanVotes = electionVote.getVotes();
					totalRepublicanVotes += republicanVotes;
					
					voteData.setVotes(republicanVotes);
					voteData.setPercentage(republicanPercent);
					
					voteDataMap.put("Republican", voteData);
				}
			}
			
			//Add the voteData to the map.
			districtData.setVoteData(voteDataMap);
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
