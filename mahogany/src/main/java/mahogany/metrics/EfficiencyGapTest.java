package mahogany.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import mahogany.Districts;
import mahogany.Elections;
import mahogany.Votes;

public class EfficiencyGapTest implements AlgorithmTest<EfficiencyGapResults>{

	@Override
	public EfficiencyGapResults  generateTestResults(List<Districts> districtList) {

		EfficiencyGapResults efficiencyResult = new EfficiencyGapResults();
		
		Map<String, DistrictData<WastedVoteData>> districtDataMap = new HashMap<String, DistrictData<WastedVoteData>>();
		//efficiencyResult.setDistrictData(districtDataList);
		
		
		
		Integer totalDemocratVotes = 0;
		Integer totalRepublicanVotes = 0;
		
		
		for(Districts district: districtList) {
			DistrictData<WastedVoteData> districtData = new DistrictData<WastedVoteData>();
			Map<String, WastedVoteData> wastedVoteMap = new HashMap<String, WastedVoteData>();
			
			Integer democratVotes = 0;
			Float democratPercent = 0.0f;
			Integer democratWastedVotes = 0;
			Integer republicanVotes = 0;
			Float republicanPercent = 0.0f;
			Integer republicanWastedVotes = 0;

			Elections districtElection = district.getElection();
			
		
			for(Votes electionVote: districtElection.getVotes()) {
				WastedVoteData voteData = new WastedVoteData();
				String party = electionVote.getParty().getName();
				
				if(party.equals("Democrat")) {
					democratVotes = electionVote.getCount();
					democratPercent = electionVote.getPercentage();
					democratWastedVotes = getWastedVotes(democratVotes, democratPercent);
					
					voteData.setVotes(democratVotes);
					voteData.setPercentage(democratPercent);
					voteData.setWastedVotes(democratWastedVotes);
					
					wastedVoteMap.put("Democrat", voteData);
				}
				else if(party.equals("Republican")) {
					republicanVotes = electionVote.getCount();
					republicanPercent = electionVote.getPercentage();
					republicanWastedVotes += getWastedVotes(republicanVotes, republicanPercent); 
					
					voteData.setVotes(republicanVotes);
					voteData.setPercentage(republicanPercent);
					voteData.setWastedVotes(republicanWastedVotes);
					
					wastedVoteMap.put("Republican", voteData);
				}
			}
		
			districtDataMap.put(district.getId().toString(), districtData);
			totalDemocratVotes += democratVotes;
			totalRepublicanVotes += republicanVotes;

		}
		ObjectMapper x = new ObjectMapper();
		System.out.println(x.valueToTree(districtDataMap));
		return null;
	}

	public Integer getWastedVotes(Integer votes, Float percentage) {
		//if the candidate lost, all votes are wasted votes
		if(percentage < 0.5) {
			return votes;
		}
		// if they won, all votes after 50% are wasted votes
		else {
			return votes - (int)((votes/percentage)/2);
		}
	}
}
