package mahogany.metrics;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class TestResult<T extends DistrictData<?>> {
	private TestNames testName;
	private Map<String, T> districtData;
	
	public abstract JsonNode toJsonNode();

	
	public TestNames getTestName() {
		return testName;
	}
	
	public void setTestName(TestNames testName) {
		this.testName = testName;
	}
	
	public Map<String, T> getDistrictData(){
		return districtData;
	}
	
	public void setDistrictData(Map<String, T> districtData){
		this.districtData = districtData;
	}
	
}
