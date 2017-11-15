package mahogany;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DistrictsRepository extends CrudRepository<Districts, Long> {
	//Districts findFirstByCongress(Integer congress);
	
	@Query("select d from Districts d, StateNames s where d.stateName = ?1 and d.number= ?2 and d.congress=?3")
	Districts findByStateEntityAndNumberAndCongress(StateNames stateName, Integer districtNumber, Integer congress);
	
	@Query("select d from Districts d where d.stateName.id = ?1 and d.number= ?2 and d.congress=?3")
	Districts findByStateNameAndNumberAndCongress(String stateName, Integer districtNumber, Integer congress);
	
	@Query("select d from Districts d where d.stateName.name = ?1 and d.congress=?2")
	List<Districts> findAllByStateAndCongress(String name, Integer congress);
	
	//@Query("select d from Districts d, StateNames s where d.stateId = s.id and s.name=?1 and d.congress=?2")
	//Districts findByStateAndCongress(String name, Integer congress);
	
	//@Query("select d from Districts d, StateNames s where d.stateId = s.id and s.name = ?1 and d.number = ?2 and d.congress = ?3")
	//Districts findByStateAndNumberAndCongress(String stateName, Integer number, Integer congress);
}
