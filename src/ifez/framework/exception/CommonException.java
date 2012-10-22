package ifez.framework.exception;

public class CommonException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CommonException() {
		super();
	}
	public CommonException(String message) {
		super(message);
	}
	public CommonException(Throwable t) {
		super(t);
	}
	public CommonException(String message, Throwable t) {
		super(message, t);
	}
}

