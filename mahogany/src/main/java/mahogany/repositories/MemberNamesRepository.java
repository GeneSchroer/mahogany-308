package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.MemberNames;

@Repository
public interface MemberNamesRepository extends CrudRepository<MemberNames, Long>{
	MemberNames findByName(String name);
}
