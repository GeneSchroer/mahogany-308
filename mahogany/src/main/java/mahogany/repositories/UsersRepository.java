package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.Users;



@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

	
	
}


