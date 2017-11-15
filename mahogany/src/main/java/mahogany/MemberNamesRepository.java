package mahogany;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberNamesRepository extends CrudRepository<MemberNames, Long>{
	MemberNames findByName(String name);
}
