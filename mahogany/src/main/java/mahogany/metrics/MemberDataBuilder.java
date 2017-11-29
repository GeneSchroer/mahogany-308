package mahogany.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mahogany.entities.Districts;
import mahogany.entities.Members;

public class MemberDataBuilder {

	public MemberStateData generateMetricData(List<Districts> districtList) {
		MemberStateData memberStateData = new MemberStateData();
		Map<String, MemberDistrictData> districtDataMap = new HashMap<String, MemberDistrictData>();
		
		for(Districts district: districtList) {
			MemberDistrictData districtData = new MemberDistrictData();
			List<MemberData> memberDataList= new ArrayList<MemberData>();
			
			for(Members member: district.getMembers()) {
				String name = member.getMemberName().getName();
				String party = member.getParty().getName();
				MemberData memberData = new MemberData();
				memberData.setName(name);
				memberData.setParty(party);
				memberDataList.add(memberData);
				districtData.setMemberData(memberDataList);
			}
			districtDataMap.put(district.getId().toString(), districtData);
		}
		
		memberStateData.setDistrictData(districtDataMap);
		memberStateData.setTestName(MetricOption.MEMBER_DATA.toString());
		return memberStateData;
		
	}
	
	
}
