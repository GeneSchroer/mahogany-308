package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.UserRoles;




@Repository
public interface UserRolesRepository extends CrudRepository<UserRoles, Long> {

	UserRoles findByRoleName(String roleName);
	
}


