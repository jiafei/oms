package cn.flame.oms.common.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

public class HttpRequests {
	public static Map<String, Object> getParametersStartingWith(
			ServletRequest request, String prefix) {
		if (request == null) {
			return new HashMap<String, Object>();
		}
		Enumeration<?> paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String content = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					continue;
				}
				if (values.length > 1) {
					params.put(content, values);
				} else {
					params.put(content, values[0]);
				}
			}
		}
		return params;
	}

	public static String encodeParameterStringWithPrefix(
			Map<String, Object> params, String prefix) {
		if (params == null || params.size() == 0) {
			return "";
		}

		if (prefix == null) {
			prefix = "";
		}

		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			queryStringBuilder.append(prefix).append(entry.getKey())
					.append('=').append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append('&');
			}
		}
		return queryStringBuilder.toString();
	}
}