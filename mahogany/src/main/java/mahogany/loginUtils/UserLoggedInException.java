package mahogany.loginUtils;

public class UserLoggedInException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserLoggedInException() {
		super("User is already logged in");
	}
}
