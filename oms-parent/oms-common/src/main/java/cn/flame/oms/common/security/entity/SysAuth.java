package cn.flame.oms.common.security.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import cn.flame.oms.common.entity.BaseEntity;
import cn.flame.oms.common.repository.support.CollectionToStringUserType;

@TypeDef(name = "SetToStringUserType", typeClass = CollectionToStringUserType.class, parameters = {
		@Parameter(name = "separator", value = ","),
		@Parameter(name = "collectionType", value = "java.util.HashSet"),
		@Parameter(name = "elementType", value = "java.lang.String") })
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysAuth extends BaseEntity<String> {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private SysUser user;
	@Type(type = "SetToStringUserType")
	private Set<String> roleIds;

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public Set<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Set<String> roleIds) {
		this.roleIds = roleIds;
	}
}