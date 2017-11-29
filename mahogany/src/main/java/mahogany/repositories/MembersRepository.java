package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.Districts;
import mahogany.entities.MemberNames;
import mahogany.entities.Members;
import mahogany.entities.Parties;

@Repository
public interface MembersRepository extends CrudRepository<Members, Long> {
	
	
	Members findByMemberNameAndPartyAndDistrict(MemberNames memberName, Parties party, Districts district);

	Members findFirstByDistrict(Districts district);

}
