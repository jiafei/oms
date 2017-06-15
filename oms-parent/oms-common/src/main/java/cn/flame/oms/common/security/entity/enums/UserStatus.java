package cn.flame.oms.common.security.entity.enums;

public enum UserStatus {
	Normal("正常状态"), Blocked("停用状态");

	private final String name;

	private UserStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}