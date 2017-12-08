package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.UserDetails;



@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {

	UserDetails findByUserNameAndPassword(String userName, String password);
	UserDetails findByUserName(String userName);
}


