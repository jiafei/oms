package cn.flame.oms.web.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.flame.oms.common.security.entity.SysUser;
import cn.flame.oms.common.security.entity.enums.UserStatus;
import cn.flame.oms.common.security.service.SysUserService;
import cn.flame.oms.web.controller.Constants;

/**
 * 验证用户过滤器 1、用户是否锁定
 */
public class SysUserFilter extends AccessControlFilter {

	@Autowired
	private SysUserService userService;

	/**
	 * 用户删除了后重定向的地址
	 */
	private String userNotfoundUrl;
	/**
	 * 用户锁定后重定向的地址
	 */
	private String userBlockedUrl;
	/**
	 * 未知错误
	 */
	private String userUnknownErrorUrl;

	public String getUserNotfoundUrl() {
		return userNotfoundUrl;
	}

	public void setUserNotfoundUrl(String userNotfoundUrl) {
		this.userNotfoundUrl = userNotfoundUrl;
	}

	public String getUserBlockedUrl() {
		return userBlockedUrl;
	}

	public void setUserBlockedUrl(String userBlockedUrl) {
		this.userBlockedUrl = userBlockedUrl;
	}

	public String getUserUnknownErrorUrl() {
		return userUnknownErrorUrl;
	}

	public void setUserUnknownErrorUrl(String userUnknownErrorUrl) {
		this.userUnknownErrorUrl = userUnknownErrorUrl;
	}

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {
		Subject subject = getSubject(request, response);
		if (subject == null) {
			return true;
		}

		String username = (String) subject.getPrincipal();
		// TODO 此处注意缓存 防止大量的查询db
		SysUser user = userService.findByUsername(username);
		// 把当前用户放到session中
		request.setAttribute(Constants.CURRENT_USER, user);
		// 用于druid监控中"Principal"属性列的显示
		WebUtils.toHttp(request).getSession()
				.setAttribute(Constants.CURRENT_USERNAME, username);

		return true;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		SysUser user = (SysUser) request.getAttribute(Constants.CURRENT_USER);
		if (user == null) {
			return true;
		}

		if (user.getStatus() == UserStatus.Blocked) {
			getSubject(request, response).logout();
			saveRequestAndRedirectToLogin(request, response);
			return false;
		}
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		getSubject(request, response).logout();
		saveRequestAndRedirectToLogin(request, response);
		return true;
	}

	protected void redirectToLogin(ServletRequest request,
			ServletResponse response) throws IOException {
		SysUser user = (SysUser) request.getAttribute(Constants.CURRENT_USER);
		String url = null;
		if (user.getStatus() == UserStatus.Blocked) {
			url = getUserBlockedUrl();
		} else {
			url = getUserUnknownErrorUrl();
		}

		WebUtils.issueRedirect(request, response, url);
	}
}