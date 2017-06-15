package cn.flame.oms.common.security.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import cn.flame.oms.common.entity.BaseEntity;
import cn.flame.oms.common.plugin.entity.Treeable;
import cn.flame.oms.common.security.entity.enums.OrgType;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysOrg extends BaseEntity<String> implements Treeable<String> {
	/**
	 * 标题
	 */
	private String name;
	@Enumerated(EnumType.STRING)
	private OrgType type;
	private String parentId;
	private String parentIds;
	private Integer weight;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 是否有叶子节点
	 */
	@Formula(value = "(select count(*) from sys_org f_t where f_t.parent_id = id)")
	private boolean hasChildren;

	public SysOrg() {
	}

	public SysOrg(String id) {
		setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OrgType getType() {
		return type;
	}

	public void setType(OrgType type) {
		this.type = type;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Override
	public String makeSelfAsNewParentIds() {
		return getParentIds() + getId() + getSeparator();
	}

	@Override
	public String getSeparator() {
		return "/";
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public boolean isRoot() {
		// if (getParentId() != null && getParentId() == 0) {
		if (StringUtils.isBlank(getParentId())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isLeaf() {
		if (isRoot()) {
			return false;
		}
		if (isHasChildren()) {
			return false;
		}

		return true;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
}