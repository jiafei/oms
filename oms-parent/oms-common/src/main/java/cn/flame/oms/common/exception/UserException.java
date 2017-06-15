package cn.flame.oms.common.exception;


public class UserException extends BaseException {
	private static final long serialVersionUID = -4371430945164314738L;

	public UserException(String code, Object[] args) {
		super("user", code, args, null);
	}
}