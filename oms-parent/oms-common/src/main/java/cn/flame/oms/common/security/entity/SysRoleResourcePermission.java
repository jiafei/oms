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

import com.google.common.collect.Sets;

@TypeDef(name = "SetToStringUserType", typeClass = CollectionToStringUserType.class, parameters = {
		@Parameter(name = "separator", value = ","),
		@Parameter(name = "collectionType", value = "java.util.HashSet"),
		@Parameter(name = "elementType", value = "java.lang.String") })
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysRoleResourcePermission extends BaseEntity<String> {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private SysRole role;
	// private String resourceId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "resource_id")
	private SysResource resource;
	@Type(type = "SetToStringUserType")
	private Set<String> permissionIds;

	public SysRoleResourcePermission() {
	}

	public SysRoleResourcePermission(String id) {
		setId(id);
	}

	public SysRole getRole() {
		return role;
	}

	public void setRole(SysRole role) {
		this.role = role;
	}

	public SysResource getResource() {
		return resource;
	}

	public void setResource(SysResource resource) {
		this.resource = resource;
	}

	public String getResourceId() {
		return resource.getId();
	}

	// public void setResourceId(String resourceId) {
	// this.resourceId = resourceId;
	// }

	public Set<String> getPermissionIds() {
		if (permissionIds == null) {
			permissionIds = Sets.newHashSet();
		}
		return permissionIds;
	}

	public void setPermissionIds(Set<String> permissionIds) {
		this.permissionIds = permissionIds;
	}

	@Override
	public String toString() {
		return "RoleResourcePermission{id=" + this.getId() + ",roleId="
				+ (role != null ? role.getId() : "null") + ", resourceId="
				+ resource.getId() + ", permissionIds=" + permissionIds + '}';
	}
}