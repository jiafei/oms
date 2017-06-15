package cn.flame.oms.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class UIHandlerExceptionResolver implements HandlerExceptionResolver {
	private Logger logger = LoggerFactory
			.getLogger(UIHandlerExceptionResolver.class);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object o, Exception ex) {
		logger.error(ex.getMessage(), ex);
		try {
			// 根据http accept决定返回错误页面或包含错误信息的json数据
			String accept = request.getHeader("Accept");
			if (StringUtils.isEmpty(accept)) {
				accept = request.getHeader("accept");
			}
			if (accept.indexOf("json") != -1) {
				handlerJsonException(ex, request, response);
				return null;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		String path = "";
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exception", ex);
		if(ex instanceof UnauthorizedException) {
			path = "common/unauthorized";
		} else {
			path = "common/500";
		}
		modelAndView.setViewName(path);

		return modelAndView;
	}

	protected void handlerJsonException(Exception ex,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		response.setCharacterEncoding("UTF-8");
//		response.setHeader("Content-Type", "application/json");
//		String message = StringUtils.isEmpty(ex.getMessage()) ? "未知错误" : ex
//				.getMessage();
//		mapper.writeValue(response.getWriter(),
//				JsonObject.alert(message, WebMessageLevel.ERROR));
	}
}