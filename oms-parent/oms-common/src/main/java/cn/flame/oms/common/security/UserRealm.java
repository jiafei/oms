package cn.flame.oms.common.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import cn.flame.oms.common.exception.UserBlockedException;
import cn.flame.oms.common.exception.UserException;
import cn.flame.oms.common.exception.UserNotExistsException;
import cn.flame.oms.common.exception.UserPasswordNotMatchException;
import cn.flame.oms.common.exception.UserPasswordRetryLimitExceedException;
import cn.flame.oms.common.security.entity.SysUser;
import cn.flame.oms.common.security.service.SysUserService;
import cn.flame.oms.common.security.service.UserAuthService;

public class UserRealm extends AuthorizingRealm {
	private static Logger logger = LogManager.getLogger();
	@Autowired
	private SysUserService userService;
	@Autowired
	private UserAuthService userAuthService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername().trim();
		String password = "";
		if (upToken.getPassword() != null) {
			password = new String(upToken.getPassword());
		}

		SysUser user = null;
		try {
			user = userService.login(username, password);
		} catch (UserNotExistsException e) {
			throw new UnknownAccountException(e.getMessage(), e);
		} catch (UserPasswordNotMatchException e) {
			throw new AuthenticationException(e.getMessage(), e);
		} catch (UserPasswordRetryLimitExceedException e) {
			throw new ExcessiveAttemptsException(e.getMessage(), e);
		} catch (UserBlockedException e) {
			throw new LockedAccountException(e.getMessage(), e);
		} catch (Exception e) {
			logger.error("login error", e);
			throw new AuthenticationException(new UserException(
					"user.unknown.error", null));
		}

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
				user.getUsername(), password.toCharArray(), getName());
		logger.debug("user:" + user);
		logger.debug("info:" + info);
		return info;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
		SysUser user = userService.findByUsername(username);

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(userAuthService.findStringRoles(user));
		authorizationInfo.setStringPermissions(userAuthService
				.findStringPermissions(user));

		return authorizationInfo;
	}

	private static final String OR_OPERATOR = " or ";
	private static final String AND_OPERATOR = " and ";
	private static final String NOT_OPERATOR = "not ";

	/**
	 * 支持or and not 关键词 不支持and or混用
	 *
	 * @param principals
	 * @param permission
	 * @return
	 */
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		if (permission.contains(OR_OPERATOR)) {
			String[] permissions = permission.split(OR_OPERATOR);
			for (String orPermission : permissions) {
				if (isPermittedWithNotOperator(principals, orPermission)) {
					return true;
				}
			}
			return false;
		} else if (permission.contains(AND_OPERATOR)) {
			String[] permissions = permission.split(AND_OPERATOR);
			for (String orPermission : permissions) {
				if (!isPermittedWithNotOperator(principals, orPermission)) {
					return false;
				}
			}
			return true;
		} else {
			return isPermittedWithNotOperator(principals, permission);
		}
	}

	private boolean isPermittedWithNotOperator(PrincipalCollection principals,
			String permission) {
		if (permission.startsWith(NOT_OPERATOR)) {
			return !super.isPermitted(principals,
					permission.substring(NOT_OPERATOR.length()));
		} else {
			return super.isPermitted(principals, permission);
		}
	}
}