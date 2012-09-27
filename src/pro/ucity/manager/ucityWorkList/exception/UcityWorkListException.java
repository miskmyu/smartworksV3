package pro.ucity.manager.ucityWorkList.exception;

import net.smartworks.server.engine.common.exception.SwException;

public class UcityWorkListException extends SwException {
	private static final long serialVersionUID = 1L;
	public UcityWorkListException() {
		super();
	}
	public UcityWorkListException(String message) {
		super(message);
	}
	public UcityWorkListException(Throwable t) {
		super(t);
	}
	public UcityWorkListException(String message, Throwable t) {
		super(message, t);
	}
}
