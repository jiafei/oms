package cn.flame.oms.web.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.google.code.kaptcha.Constants;

public class KaptchaValidateFilter extends AccessControlFilter {
	private boolean kaptchaEnabled = true;
	private String kaptchaParam = "kaptchaCode";
	private String kaptchaErrorUrl;

	public void setKaptchaEnabled(boolean kaptchaEnabled) {
		this.kaptchaEnabled = kaptchaEnabled;
	}

	public void setKaptchaParam(String kaptchaParam) {
		this.kaptchaParam = kaptchaParam;
	}

	public String getKaptchaErrorUrl() {
		return kaptchaErrorUrl;
	}

	public void setKaptchaErrorUrl(String kaptchaErrorUrl) {
		this.kaptchaErrorUrl = kaptchaErrorUrl;
	}

	@Override
	public boolean onPreHandle(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		request.setAttribute("kaptchaEnabled", kaptchaEnabled);
		return super.onPreHandle(request, response, mappedValue);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		// 验证码禁用
		// 此处对method:post的判断是必要的，method为get（非表单post提交）时放通。
		if (kaptchaEnabled == false
				|| !WebUtils.toHttp(request).getMethod()
						.equalsIgnoreCase(POST_METHOD)) {
			return true;
		}
		String expectedText = (String) WebUtils.toHttp(request).getSession()
				.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		String receivedText = request.getParameter(kaptchaParam);
		return receivedText != null
				&& receivedText.equalsIgnoreCase(expectedText);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		redirectToLogin(request, response);
		return false;
	}

	protected void redirectToLogin(ServletRequest request,
			ServletResponse response) throws IOException {
		WebUtils.issueRedirect(request, response, getKaptchaErrorUrl());
	}
}