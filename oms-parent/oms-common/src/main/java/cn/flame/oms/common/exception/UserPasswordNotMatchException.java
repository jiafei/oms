package cn.flame.oms.common.exception;

public class UserPasswordNotMatchException extends UserException {
	private static final long serialVersionUID = -391225314115113365L;

	public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}