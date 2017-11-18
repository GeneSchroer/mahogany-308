package mahogany.metrics;

import java.util.List;
import java.util.Map;

import mahogany.Districts;

public interface AlgorithmTest<T extends TestResult> {

	T generateTestResults(List<Districts> districts);
	
}
