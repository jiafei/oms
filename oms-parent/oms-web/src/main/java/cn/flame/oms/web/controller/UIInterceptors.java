package cn.flame.oms.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.flame.oms.common.utils.HttpRequests;

import com.google.common.collect.Maps;

public class UIInterceptors extends HandlerInterceptorAdapter {
	private Logger logger = LogManager.getLogger();
	private boolean debug;

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		/* 这里可以针对所有的Controller做一些统一处理 */
		if (handler instanceof HandlerMethod) {
			if (((HandlerMethod) handler).getBean() instanceof UIController) {
				UIController controller = (UIController) ((HandlerMethod) handler)
						.getBean();
				request.setAttribute("portalName", controller.getPortalName());
			}
		}

		Map<Boolean, String> booleanMap = Maps.newHashMap();
		booleanMap.put(true, "是");
		booleanMap.put(false, "否");
		request.setAttribute("BooleanMap", booleanMap);

		// 排序参数
		Map<String, Object> searchParams = HttpRequests
				.getParametersStartingWith(request, "Q_");
		request.setAttribute("sortType", request.getParameter("sortType"));
		request.setAttribute("searchParams", HttpRequests
				.encodeParameterStringWithPrefix(searchParams, "Q_"));
	}
}