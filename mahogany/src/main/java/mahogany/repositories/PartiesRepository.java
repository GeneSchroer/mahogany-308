package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.Parties;

@Repository
public interface PartiesRepository extends CrudRepository<Parties, Long>{
	Parties findByName(String name);
}
