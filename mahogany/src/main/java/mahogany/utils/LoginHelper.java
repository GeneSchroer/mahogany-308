package mahogany.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mahogany.entities.UserDetails;
import mahogany.entities.UserRoles;
import mahogany.entities.Users;
import mahogany.loginUtils.LoginStatus;
import mahogany.repositories.UserDetailsRepository;
import mahogany.repositories.UserRolesRepository;
import mahogany.repositories.UsersRepository;

@Component
public class LoginHelper {

	@Autowired UserDetailsRepository userDetailsRepo;
	@Autowired UserRolesRepository userRolesRepo;
	@Autowired UsersRepository usersRepo;
	
	public LoginStatus registerNewUser(String userName, String password) {
		
		// check if name is already taken
		UserDetails userDetailsEntity = userDetailsRepo.findByUserName(userName);
		
		if(userDetailsEntity != null) {
			return LoginStatus.USER_ALREADY_EXISTS;
		}
		else {
			userDetailsEntity = new UserDetails();
			userDetailsEntity.setUserName(userName);
			userDetailsEntity.setPassword(password);
			userDetailsEntity.setActive(1);
		}
		userDetailsRepo.save(userDetailsEntity);
		
		UserRoles userRole = userRolesRepo.findByRoleName("USER");
		Users usersEntity = new Users();
		
		usersEntity.setDetails(userDetailsEntity);
		List<UserRoles> roleList = new ArrayList<UserRoles>();
		roleList.add(userRole);
		usersEntity.setRoles(roleList);
		usersRepo.save(usersEntity);
		
		return LoginStatus.OK;
	}
}
