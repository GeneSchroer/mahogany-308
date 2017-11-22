package mahogany.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.Districts;
import mahogany.entities.StateNames;


@Repository
public interface DistrictsRepository extends CrudRepository<Districts, Long> {
	
	@Query("select d from Districts d, StateNames s where d.stateName = ?1 and d.districtNumber= ?2 and d.year=?3")
	Districts findByStateEntityAndDistrictNumberAndYear(StateNames stateName, Integer districtNumber, Integer year);
	
	@Query("select d from Districts d where d.stateName.name = ?1 and d.districtNumber= ?2 and d.year=?3")
	Districts findByStateNameAndDistrictNumberAndYear(String stateName, Integer districtNumber, Integer year);
	
	@Query("select d from Districts d where d.stateName.name = ?1 and d.year=?2")
	List<Districts> findAllByStateAndYear(String name, Integer year);
	
	@Query("select distinct d.year from Districts d")
	List<Integer> findAllSessionsOfYear();
}
