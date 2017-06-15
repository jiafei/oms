package cn.flame.oms.common.exception;

public class UserBlockedException extends UserException {
	private static final long serialVersionUID = -4181890206949491094L;

	public UserBlockedException(String reason) {
		super("user.blocked", new Object[] { reason });
	}
}