package mahogany.metrics;

import java.util.List;
import mahogany.entities.Elections;

public interface AlgorithmTest<T extends AbstractStateData<?>> {

	T generateMetricData(List<Elections> electionList);
	
}
