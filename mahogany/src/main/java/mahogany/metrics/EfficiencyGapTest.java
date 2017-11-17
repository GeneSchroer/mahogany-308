package mahogany.metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mahogany.Districts;
import mahogany.Elections;
import mahogany.Votes;

public class EfficiencyGapTest implements AlgorithmTest<Float>{

	@Override
	public Map<String, TestResult<Float>> generateTestResults(List<Districts> districtList) {
		Map<String, TestResult<Float>> testResultMap = new HashMap<String, TestResult<Float>>();
		
		Integer democratWastedVotes = 0;
		Integer republicanWastedVotes = 0;
		
		for(Districts district: districtList) {
			Elections districtElection = district.getElection();
			
			Integer democratVotes = 0;
			Float democratPercent = 0.0f;
			
			Integer republicanVotes = 0;
			Float republicanPercent = 0.0f;
			for(Votes electionVote: districtElection.getVotes()) {
				String party = electionVote.getParty().getName();
				if(party.equals("Democrat")) {
					democratVotes = electionVote.getCount();
					democratPercent = electionVote.getPercentage();
				}
				else if(party.equals("Republican")) {
					republicanVotes = electionVote.getCount();
					republicanPercent = electionVote.getPercentage();
					
				}
			}
			democratWastedVotes += getWastedVotes(democratVotes, democratPercent);
			republicanWastedVotes += getWastedVotes(republicanVotes, republicanPercent); 
		
		
		}
		
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
