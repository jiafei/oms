package cn.flame.oms.common.security.entity;

import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import cn.flame.oms.common.entity.BaseSysEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysPermission extends BaseSysEntity<String> {
	/**
	 * 前端显示名称
	 */
	private String name;
	/**
	 * 系统中验证时使用的权限标识
	 */
	private String identity;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
}