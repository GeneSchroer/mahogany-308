package mahogany;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends CrudRepository<Votes, Long> {
	//List<Votes> findByElectionIdAndPartyId(Long electionId);
}
