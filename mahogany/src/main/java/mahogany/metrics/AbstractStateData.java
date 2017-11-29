package mahogany.metrics;

import java.util.Map;

public abstract class AbstractStateData<T extends AbstractDistrictData> {
	private String testName;
	private Map<String, T> districtData;
	
	public String getTestName() {
		return testName;
	}
	
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public Map<String, T> getDistrictData(){
		return districtData;
	}
	
	public void setDistrictData(Map<String, T> districtData){
		this.districtData = districtData;
	}
	
}
