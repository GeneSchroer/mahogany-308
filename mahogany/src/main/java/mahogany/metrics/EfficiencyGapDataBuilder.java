package mahogany.metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;

import mahogany.entities.Elections;
import mahogany.entities.Votes;

public class EfficiencyGapDataBuilder implements AlgorithmTest<EfficiencyGapStateData>{

	@Override
	public EfficiencyGapStateData generateMetricData(List<Elections> electionList) {

		EfficiencyGapStateData efficiencyResult = new EfficiencyGapStateData();
		
		Map<String, ElectionDistrictData<WastedVoteData>> districtDataMap = new HashMap<String, ElectionDistrictData<WastedVoteData>>();
		
		
		Integer totalStateVotes = 0;
		Integer totalSeats = 0;
		
		Integer totalDemocratVotes = 0;
		Integer totalDemocratSeats = 0;
		Integer totalRepublicanVotes = 0;
		Integer totalRepublicanSeats = 0;
		Float efficiencyGap = 0.0f;
		
		
		for(Elections election: electionList) {
			ElectionDistrictData<WastedVoteData> districtData = new ElectionDistrictData<WastedVoteData>();
			Map<String, WastedVoteData> wastedVoteMap = new HashMap<String, WastedVoteData>();
			
			Integer democratVotes = 0;
			Float democratPercent = 0.0f;
			Integer democratWastedVotes = 0;
			Integer republicanVotes = 0;
			Float republicanPercent = 0.0f;
			Integer republicanWastedVotes = 0;
			
			
			totalStateVotes += election.getTotalVotes();
			++totalSeats;
			
			if(election.getParty().getName().equals("Democrat")) {
				++totalDemocratSeats;
				districtData.setWinningParty("Democrat");
			}
			else if (election.getParty().getName().equals("Republican")) {
				++totalRepublicanSeats;
				districtData.setWinningParty("Republican");
			}
			else {
				districtData.setWinningParty("Other");
			}
		
			for(Entry<Long, Votes> entry: election.getVotes().entrySet()) {
	
				Votes electionVote = entry.getValue();
				
				WastedVoteData voteData = new WastedVoteData();
				String party = electionVote.getParty().getName();
				
				if(party.equals("Democrat")) {
					democratVotes = electionVote.getVotes();
					democratWastedVotes = getWastedVotes(democratVotes, election.getTotalVotes());
					totalDemocratVotes += democratVotes;
					
					voteData.setVotes(democratVotes);
					voteData.setPercentage((float)democratVotes/election.getTotalVotes());
					voteData.setWastedVotes(democratWastedVotes);
					
					wastedVoteMap.put("Democrat", voteData);
				}
				else if(party.equals("Republican")) {
					republicanVotes = electionVote.getVotes();
					republicanWastedVotes += getWastedVotes(republicanVotes, election.getTotalVotes()); 
					totalRepublicanVotes += republicanVotes;

					
					voteData.setVotes(republicanVotes);
					voteData.setPercentage((float)democratVotes/election.getTotalVotes());
					voteData.setWastedVotes(republicanWastedVotes);
					
					wastedVoteMap.put("Republican", voteData);
				}
			}
			districtData.setVoteData(wastedVoteMap);
			districtDataMap.put(election.getDistrict().getId().toString(), districtData);
			
			//System.out.println((float)republicanWastedVotes/republicanVotes);
			//System.out.println((float)democratWastedVotes/democratVotes);
		}
		
		efficiencyResult.setDistrictData(districtDataMap);
		efficiencyResult.setTotalDemocratVotes(totalDemocratVotes);
		efficiencyResult.setTotalRepublicanVotes(totalRepublicanVotes);
		efficiencyResult.setTotalDemocratSeats(totalDemocratSeats);
		efficiencyResult.setTotalRepublicanSeats(totalRepublicanSeats);
		
		
		efficiencyGap = (float)(((double)totalRepublicanVotes/totalStateVotes) - 0.5) - 2 * (float)(((double)totalRepublicanSeats/totalSeats) - 0.5);
		
		efficiencyResult.setEfficiencyGap(efficiencyGap);
		
		efficiencyResult.setTestName(MetricOption.EFFICIENCY_GAP.toString());
		
		ObjectMapper x = new ObjectMapper();
		System.out.println(x.valueToTree(efficiencyResult));
		return efficiencyResult;
	}

	public Integer getWastedVotes(Integer votes, Integer totalVotes) {
		//if the candidate lost, all votes are wasted votes
		Float percentage = (float) votes/totalVotes;
		
		if(percentage < 0.5) {
			return votes;
		}
		// if they won, all votes after 50% are wasted votes
		else {
			return votes - (int)((votes/percentage)/2);
		}
	}
}
