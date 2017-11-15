package mahogany;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartiesRepository extends CrudRepository<Parties, Long>{
	Parties findByName(String name);
}
