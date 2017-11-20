package mahogany.metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mahogany.entities.Elections;
import mahogany.entities.Votes;

public class ElectionDataService implements AlgorithmTest<TestResult<DistrictData<VoteData>>> {

	@Override
	public TestResult<DistrictData<VoteData>> generateTestResults(List<Elections> electionList) {
		TestResult<DistrictData<VoteData>> electionDataResults = new TestResult<DistrictData<VoteData>>();
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
			for(Votes electionVote: election.getVotes()) {
				VoteData voteData = new VoteData();
				String party = electionVote.getParty().getName();
				
				if(party.equals("Democrat")) {
					democratVotes = electionVote.getVotes();
					democratPercent = electionVote.getPercentage();
					totalDemocratVotes += democratVotes;
					
					voteData.setVotes(democratVotes);
					voteData.setPercentage(democratPercent);
					
					voteMap.put("Democrat", voteData);
				}
				else if(party.equals("Republican")) {
					republicanVotes = electionVote.getVotes();
					republicanPercent = electionVote.getPercentage();
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
