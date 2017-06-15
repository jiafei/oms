package cn.flame.oms.common.security.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import cn.flame.oms.common.entity.BaseSysEntity;
import cn.flame.oms.common.security.entity.enums.CommonStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysRole extends BaseSysEntity<String> {
	private String name;
	private String identity;
	private String description;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = SysRoleResourcePermission.class, mappedBy = "role", orphanRemoval = true)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy
	private List<SysRoleResourcePermission> resourcePermissions;

	@Enumerated(EnumType.STRING)
	private CommonStatus status = CommonStatus.Enabled;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SysRoleResourcePermission> getResourcePermissions() {
		if (resourcePermissions == null) {
			resourcePermissions = Lists.newArrayList();
		}
		return resourcePermissions;
	}

	public void setResourcePermissions(
			List<SysRoleResourcePermission> resourcePermissions) {
		this.resourcePermissions = resourcePermissions;
	}

	public void addResourcePermission(
			SysRoleResourcePermission roleResourcePermission) {
		roleResourcePermission.setRole(this);
		getResourcePermissions().add(roleResourcePermission);
	}

	public CommonStatus getStatus() {
		return status;
	}

	public void setStatus(CommonStatus status) {
		this.status = status;
	}
}