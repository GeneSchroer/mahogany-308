package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.StateNames;

@Repository
public interface StateNamesRepository extends CrudRepository<StateNames, Long> {
	StateNames findByName(String name);
	
	
}
