package mahogany.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mahogany.entities.UserDetails;
import mahogany.loginUtils.LoginStatus;
import mahogany.repositories.UserDetailsRepository;
import mahogany.repositories.UserRolesRepository;

@Component
public class LoginHelper {

	@Autowired UserDetailsRepository userDetailsRepo;
	@Autowired UserRolesRepository userRolesRepo;
	
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
			userDetailsEntity.setRole(userRolesRepo.findByRoleName("USER"));
			userDetailsRepo.save(userDetailsEntity);
			return LoginStatus.OK;
		}
		
	}
	
	public LoginStatus loginUser(String userName, String password) {
		
		UserDetails user = userDetailsRepo.findByUserNameAndPassword(userName, password);
		if(user == null) {
			return LoginStatus.NO_SUCH_USER;
		}
		else if(user.getActive() == 1) {
			return LoginStatus.ALREADY_LOGGED_IN;
		}
		else {
			user.setActive(1);
			userDetailsRepo.save(user);
			return LoginStatus.OK;
		}
	}
	
	public LoginStatus logoutUser(String userName) {
		UserDetails user = userDetailsRepo.findByUserName(userName);
		if(user == null) {
			return LoginStatus.NO_SUCH_USER;
		}
		else {
			user.setActive(0);
			userDetailsRepo.save(user);
			return LoginStatus.OK;
		}
	}
	
}
