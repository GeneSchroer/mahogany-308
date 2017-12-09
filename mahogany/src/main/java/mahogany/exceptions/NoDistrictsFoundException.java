package mahogany.exceptions;

public class NoDistrictsFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoDistrictsFoundException() {
		super("No Districts Were Found");
	}
	
	public NoDistrictsFoundException(String string) {
		super(string);
	}
}
