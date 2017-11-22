package mahogany.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.Districts;
import mahogany.entities.Elections;
import mahogany.entities.StateNames;

@Repository
public interface ElectionsRepository extends CrudRepository<Elections, Long> {
	
	@Query("select e from Elections e, Districts d, StateNames s where e.district = d and d.stateName = s and s.name = ?1 and d.year = ?2")
	List<Elections> findAllByStateAndYear(String stateName, Integer year);
	//@Query("Select e from Elections e, Districts d, StateNames s where e.districtId = d.id and d.stateId = s.id and s.name = ?1 and d.number=?2 and d.year=?3")
	//Elections findByStateNameAndDistrictNumberAndYear(String stateName, Integer districtNumber, Integer year);

	Elections findByDistrict(Districts district);
	
	@Query("Select e from Elections e, Districts d  where e.district = d and d.stateName = ?1 and d.districtNumber=?2 and d.year=?3")
	Elections findByStateNameAndDistrictNumberAndYear2(StateNames entity, Integer districtNumber, Integer year);

}

