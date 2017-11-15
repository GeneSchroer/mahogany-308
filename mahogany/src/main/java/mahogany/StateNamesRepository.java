package mahogany;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateNamesRepository extends CrudRepository<StateNames, Long> {
	StateNames findByName(String name);
	
	
}
