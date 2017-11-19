package mahogany.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.Districts;
import mahogany.entities.Elections;
import mahogany.entities.Parties;
import mahogany.entities.Votes;

@Repository
public interface VotesRepository extends CrudRepository<Votes, Long> {
	
	//@Query("Select v from Votes V, Elections e, Districts d where v.election = e and e.district = d and d.stateName.name = ?1 and d.number = ?2 and d.congress = ?3")
	//List<Votes> findByStateNameAndNumberAndCongress(String stateName, Integer number, Integer congress);
	
	@Query("select v from Votes v, Elections e where v.election = e and e.district = ?1 and v.party = ?2")
	Votes findByDistrictAndParty(Districts district, Parties party);
	
	@Query("select v from Votes v, Elections e where v.election = e and e.district = ?1")
	List<Votes> findAllByDistrict(Districts district);
	
	Votes findByElectionAndParty(Elections election, Parties party);
	//List<Votes> findByElectionIdAndPartyId(Long electionId);
}
