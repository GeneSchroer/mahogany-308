package mahogany.metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;

import mahogany.entities.Elections;
import mahogany.entities.Votes;

public class EfficiencyGapDataBuilder implements DataBuilder<EfficiencyGapStateData>{

	@Override
	public EfficiencyGapStateData generateDataObject(List<Elections> electionList) {

		EfficiencyGapStateData efficiencyResult = new EfficiencyGapStateData();
		Map<String, ElectionDistrictData<WastedVoteData>> districtDataMap = new HashMap<String, ElectionDistrictData<WastedVoteData>>();
		
		Integer totalStateVotes = 0;
		Integer totalSeats = 0;
		
		
		
		Integer totalDemocratVotes = 0;
		Integer totalDemocratSeats = 0;
		Integer totalRepublicanVotes = 0;
		Integer totalRepublicanSeats = 0;
		
		
		
		//get election data for each district
		for(Elections election: electionList) {
			
			ElectionDistrictData<WastedVoteData> districtData = new ElectionDistrictData<WastedVoteData>();
			Map<String, WastedVoteData> wastedVoteMap = new HashMap<String, WastedVoteData>();
			Integer democratVotes = 0;
			Integer democratWastedVotes = 0;
			Integer republicanVotes = 0;
			Integer republicanWastedVotes = 0;
			
			
			totalStateVotes += election.getTotalVotes();
			++totalSeats;
			
			// get total seats won by main 2 parties.
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
				String partyName = electionVote.getParty().getName();
				
			// get the votes for the district
				// and add that to the total votes for that party.
				if(partyName.equals("Democrat")) {
					democratVotes = electionVote.getVotes();
					democratWastedVotes = getWastedVotes(democratVotes, election.getTotalVotes());
					totalDemocratVotes += democratVotes;
					
					voteData.setVotes(democratVotes);
					voteData.setPercentage((float)democratVotes/election.getTotalVotes());
					voteData.setWastedVotes(democratWastedVotes);
					
					wastedVoteMap.put("Democrat", voteData);
				}
				else if(partyName.equals("Republican")) {
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
			
		}
		
		efficiencyResult.setDistrictData(districtDataMap);
		efficiencyResult.setTotalDemocratVotes(totalDemocratVotes);
		efficiencyResult.setTotalRepublicanVotes(totalRepublicanVotes);
		efficiencyResult.setTotalDemocratSeats(totalDemocratSeats);
		efficiencyResult.setTotalRepublicanSeats(totalRepublicanSeats);
		
		Float democratGap = getEfficiencyGap(totalDemocratVotes, totalStateVotes, totalDemocratSeats, totalSeats);
		Float republicanGap = getEfficiencyGap(totalRepublicanVotes, totalStateVotes, totalRepublicanSeats, totalSeats);
		
		efficiencyResult.setDemocratGap(democratGap);
		efficiencyResult.setRepublicanGap(republicanGap);
		
		
		efficiencyResult.setTestName(MetricOption.EFFICIENCY_GAP.toString());
		
//	ObjectMapper x = new ObjectMapper();
//	System.out.println(x.valueToTree(efficiencyResult));
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
	
	
	public Float getEfficiencyGap(Integer partyVotes, Integer totalVotes, Integer partySeats, Integer totalSeats) {
		Float efficiencyGap = (float)(((double)partyVotes/totalVotes) - 0.5) - 2 * (float)(((double)partySeats/totalSeats) - 0.5);
		
		return efficiencyGap;
	}
	
}
