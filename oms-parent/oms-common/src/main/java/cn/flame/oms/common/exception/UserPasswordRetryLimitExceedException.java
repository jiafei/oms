package cn.flame.oms.common.exception;

public class UserPasswordRetryLimitExceedException extends UserException {
	private static final long serialVersionUID = -3837146478721902995L;

	public UserPasswordRetryLimitExceedException(int retryLimitCount) {
		super("user.password.retry.limit.exceed",
				new Object[] { retryLimitCount });
	}
}