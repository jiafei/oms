package cn.flame.oms.common.security.entity.enums;

/**
 * 组织机构类型
 */
public enum OrgType {
	HEAD("总公司"), BRANCH("分公司"), DEPT("部门"), GROUP("分部");

	private final String name;

	private OrgType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}