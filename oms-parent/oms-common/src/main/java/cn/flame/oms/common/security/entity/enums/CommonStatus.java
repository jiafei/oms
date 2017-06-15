package cn.flame.oms.common.security.entity.enums;

public enum CommonStatus {
	Enabled("启用"), Disabled("禁用");

	private final String name;

	private CommonStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}