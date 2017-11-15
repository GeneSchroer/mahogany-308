package mahogany;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembersRepository extends CrudRepository<Members, Long> {
	Members findByNameIdAndPartyId(Long nameId, Long partyId);
}
