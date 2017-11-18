package mahogany.metrics;

import java.util.List;

import mahogany.Districts;

public class MetricTestService {

	
	
	public TestResult<?> getTestResults(List<Districts> districtList, String option){
		if(option.equals("Efficiency Gap")) {
			TestResult<?> results = new EfficiencyGapTest().generateTestResults(districtList);
			return results;
		}
		else {
			return null;
		}
		
		
	}
}
