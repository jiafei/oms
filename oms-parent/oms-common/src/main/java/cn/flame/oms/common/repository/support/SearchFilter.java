package cn.flame.oms.common.repository.support;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class SearchFilter {
	public enum Operator {
		/**
		 * equals
		 */
		EQ,
		/**
		 * not equals
		 */
		NE,
		/**
		 * like
		 */
		LIKE,
		/**
		 * left like
		 */
		LL,
		/**
		 * right like
		 */
		RL,
		/**
		 * not like
		 */
		NL,
		/**
		 * left not like
		 */
		LNL,
		/**
		 * right not like
		 */
		RNL,
		/**
		 * great than
		 */
		GT,
		/**
		 * less than
		 */
		LT,
		/**
		 * great than or equals
		 */
		GTE,
		/**
		 * less than or equals
		 */
		LTE,
		/**
		 * in
		 */
		IN
	}

	public enum ParameterType {
		S, D
	}

	private String fieldName;
	private Object value;
	private Operator operator;
	private ParameterType parameterType;

	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	public SearchFilter(String fieldName, Operator operator, Object value,
			ParameterType parameterType) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;

		this.parameterType = parameterType;
		if (this.parameterType == null) {
			this.parameterType = ParameterType.S;
		}
	}

	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 */
	public static Map<String, SearchFilter> parse(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String && StringUtils.isBlank((String) value)) {
				continue;
			}

			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2 && names.length != 3) {
				throw new IllegalArgumentException(key
						+ " is not a valid search filter name");
			}
			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);
			ParameterType parameterType = null;
			if (names.length == 3) {
				parameterType = ParameterType.valueOf(names[2]);
			}

			// 创建searchFilter
			SearchFilter filter = new SearchFilter(filedName, operator, value,
					parameterType);
			filters.put(key, filter);
		}

		return filters;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getValue() {
		switch (parameterType) {
		case D:
			String strValue = String.valueOf(value);
			try {
				return DateUtils.parseDate(strValue, "yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		default:
			return value;
		}
	}

	public Operator getOperator() {
		return operator;
	}

	public ParameterType getParameterType() {
		return parameterType;
	}
}