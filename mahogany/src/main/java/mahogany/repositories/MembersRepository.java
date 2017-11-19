package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.Districts;
import mahogany.entities.Members;

@Repository
public interface MembersRepository extends CrudRepository<Members, Long> {
	Members findByNameIdAndPartyId(Long nameId, Long partyId);

	Members findFirstByDistrict(Districts district);

}
