package cn.flame.oms.common.exception;

public class UserNotExistsException extends UserException {
	private static final long serialVersionUID = 8224606864270432872L;

	public UserNotExistsException() {
		super("user.not.exists", null);
	}
}