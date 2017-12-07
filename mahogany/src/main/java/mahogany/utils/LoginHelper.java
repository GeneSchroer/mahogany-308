package mahogany.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mahogany.entities.UserDetails;
import mahogany.loginUtils.AuthenticationFailureException;
import mahogany.loginUtils.DuplicateUserNameException;
import mahogany.repositories.UserDetailsRepository;
import mahogany.repositories.UserRolesRepository;

@Component
public class LoginHelper {

	@Autowired UserDetailsRepository userDetailsRepo;
	@Autowired UserRolesRepository userRolesRepo;
	
	public UserDetails registerNewUser(String userName, String password)  {
		
		// check if name is already taken
		UserDetails userDetailsEntity = userDetailsRepo.findByUserName(userName);
		
		if(userDetailsEntity != null) {
			throw new DuplicateUserNameException("User already exists");
		}
		else {
			userDetailsEntity = new UserDetails();
			userDetailsEntity.setUserName(userName);
			userDetailsEntity.setPassword(password);
			userDetailsEntity.setActive(1);
			userDetailsEntity.setRole(userRolesRepo.findByRoleName("USER"));
			userDetailsRepo.save(userDetailsEntity);
			return userDetailsEntity;
		}
		
	}
	
	public UserDetails loginUser(String userName, String password) {
		
		UserDetails user = userDetailsRepo.findByUserNameAndPassword(userName, password);
		if(user == null) {
			throw new AuthenticationFailureException("UserName/Password not recognized");
		}
		else if(user.getActive() == 1) {
			throw new AuthenticationFailureException("User is already logged in");
		}
		else {
			user.setActive(1);
			userDetailsRepo.save(user);
			return user;
		}
	}
	
	public void logoutUser(String userName) {
		UserDetails user = userDetailsRepo.findByUserName(userName);
		if(user != null) {
			user.setActive(0);
			userDetailsRepo.save(user);
		}
	}
	
}
