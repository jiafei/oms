package cn.flame.oms.web.filter;

import javax.servlet.ServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import cn.flame.oms.common.security.service.SysUserService;

/**
 * 基于几点修改： 1、onLoginFailure 时 把异常添加到request attribute中 而不是异常类名
 * <p>
 * 2、登录成功时：成功页面重定向：
 * <p>
 * 2.1、如果前一个页面是登录页面，-->2.3
 * <p>
 * 2.2、如果有SavedRequest 则返回到SavedRequest
 * <p>
 * 2.3、否则根据当前登录的用户决定返回到管理员首页/前台首页
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
	@Autowired
	private SysUserService sysUserService;

	@Override
	protected void setFailureAttribute(ServletRequest request,
			AuthenticationException ae) {
		request.setAttribute(getFailureKeyAttribute(), ae);
	}

	// /**
	// * 默认的成功地址
	// */
	// private String defaultSuccessUrl;

	/**
	 * 管理员默认的成功地址
	 */
	// private String adminDefaultSuccessUrl;

	// public void setDefaultSuccessUrl(String defaultSuccessUrl) {
	// this.defaultSuccessUrl = defaultSuccessUrl;
	// }
	//
	// public String getDefaultSuccessUrl() {
	// return defaultSuccessUrl;
	// }

	// public void setAdminDefaultSuccessUrl(String adminDefaultSuccessUrl) {
	// this.adminDefaultSuccessUrl = adminDefaultSuccessUrl;
	// }
	//
	// public String getAdminDefaultSuccessUrl() {
	// return adminDefaultSuccessUrl;
	// }

	/**
	 * 根据用户选择成功地址
	 * 此处可以需要根据用户角色或其他业务细节定向到不同的登录成功页面
	 *
	 * @return
	 */
	@Override
	public String getSuccessUrl() {
		// String username = (String) SecurityUtils.getSubject().getPrincipal();
		// SysUser user = sysUserService.findByUsername(username);
		// if (user != null && Boolean.TRUE.equals(user.getAdmin())) {
		// return getAdminDefaultSuccessUrl();
		// }
		return super.getSuccessUrl();
	}
}