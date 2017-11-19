package mahogany.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import mahogany.entities.Districts;
import mahogany.entities.Elections;
import mahogany.entities.Votes;

public class EfficiencyGapTest implements AlgorithmTest<EfficiencyGapResults>{

	@Override
	public EfficiencyGapResults  generateTestResults(List<Districts> districtList) {

		EfficiencyGapResults efficiencyResult = new EfficiencyGapResults();
		
		Map<String, DistrictData<WastedVoteData>> districtDataMap = new HashMap<String, DistrictData<WastedVoteData>>();
		//efficiencyResult.setDistrictData(districtDataList);
		
		
		
		Integer totalDemocratVotes = 0;
		Integer totalDemocratSeats = 0;
		Integer totalRepublicanVotes = 0;
		Integer totalRepublicanSeats = 0;
		Float efficiencyGap = 0.0f;
		
		
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
			if(districtElection.getParty().getName().equals("Democrat")) {
				++totalDemocratSeats;
				districtData.setWinningParty("Democrat");
			}
			else if (districtElection.getParty().getName().equals("Republican")) {
				++totalRepublicanSeats;
				districtData.setWinningParty("Republican");
			}
		
			
			for(Votes electionVote: districtElection.getVotes()) {
				WastedVoteData voteData = new WastedVoteData();
				String party = electionVote.getParty().getName();
				
				if(party.equals("Democrat")) {
					democratVotes = electionVote.getVotes();
					democratPercent = electionVote.getPercentage();
					democratWastedVotes = getWastedVotes(democratVotes, democratPercent);
					totalDemocratVotes += democratVotes;
					
					voteData.setVotes(democratVotes);
					voteData.setPercentage(democratPercent);
					voteData.setWastedVotes(democratWastedVotes);
					
					wastedVoteMap.put("Democrat", voteData);
				}
				else if(party.equals("Republican")) {
					republicanVotes = electionVote.getVotes();
					republicanPercent = electionVote.getPercentage();
					republicanWastedVotes += getWastedVotes(republicanVotes, republicanPercent); 
					totalRepublicanVotes += republicanVotes;

					
					voteData.setVotes(republicanVotes);
					voteData.setPercentage(republicanPercent);
					voteData.setWastedVotes(republicanWastedVotes);
					
					wastedVoteMap.put("Republican", voteData);
				}
			}
			districtData.setVoteData(wastedVoteMap);
			districtDataMap.put(district.getId().toString(), districtData);
			
			
		}
		
		efficiencyResult.setDistrictData(districtDataMap);
		efficiencyResult.setTotalDemocratVotes(totalDemocratVotes);
		efficiencyResult.setTotalRepublicanVotes(totalRepublicanVotes);
		efficiencyResult.setTotalDemocratSeats(totalDemocratSeats);
		efficiencyResult.setTotalRepublicanSeats(totalRepublicanSeats);
		
		Integer totalVotes = totalDemocratVotes + totalRepublicanVotes;
		Integer totalSeats = totalDemocratSeats + totalRepublicanSeats;
		
		efficiencyGap = (float)(((double)totalRepublicanVotes/totalVotes) - 0.5) - 2 * (float)(((double)totalRepublicanSeats/totalSeats) - 0.5);
		
		efficiencyResult.setEfficiencyGap(efficiencyGap);
		
		efficiencyResult.setTestName(TestNames.EFFICIENCY_GAP);
		
		ObjectMapper x = new ObjectMapper();
		System.out.println(x.valueToTree(efficiencyResult));
		return efficiencyResult;
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
