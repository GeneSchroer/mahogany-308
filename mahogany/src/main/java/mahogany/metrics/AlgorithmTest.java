package mahogany.metrics;

import java.util.List;
import java.util.Map;

import mahogany.entities.Districts;
import mahogany.entities.Elections;

public interface AlgorithmTest<T extends GerrymanderData> {

	T generateMetricData(List<Elections> electionList);
	
}
