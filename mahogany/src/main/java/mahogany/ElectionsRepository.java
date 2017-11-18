package mahogany;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionsRepository extends CrudRepository<Elections, Long> {
	
	//@Query("Select e from Elections e, Districts d, StateNames s where e.districtId = d.id and d.stateId = s.id and s.name = ?1 and d.number=?2 and d.congress=?3")
	//Elections findByStateNameAndDistrictNumberAndCongress(String stateName, Integer districtNumber, Integer congress);

	Elections findByDistrict(Districts district);
	
	@Query("Select e from Elections e, Districts d  where e.district = d and d.stateName = ?1 and d.number=?2 and d.congress=?3")
	Elections findByStateNameAndDistrictNumberAndCongress2(StateNames entity, Integer districtNumber, Integer congress);

}

