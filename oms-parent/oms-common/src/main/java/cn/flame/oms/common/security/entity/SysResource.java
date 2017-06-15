package cn.flame.oms.common.security.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import cn.flame.oms.common.entity.BaseSysEntity;
import cn.flame.oms.common.plugin.entity.Treeable;
import cn.flame.oms.common.security.entity.enums.CommonStatus;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysResource extends BaseSysEntity<String> implements
		Treeable<String> {
	/**
	 * 标题
	 */
	private String name;
	/**
	 * 资源标识符 用于权限匹配的 如sys:resource
	 */
	private String identity;
	/**
	 * 点击后前往的地址 菜单才有
	 */
	private String url;
	/**
	 * 父路径
	 */
	private String parentId;
	private String parentIds;
	private Boolean isMenu;
	private Integer weight;
	/**
	 * 图标
	 */
	private String icon;
	@Formula(value="(select t_parent.weight from sys_resource t_parent where t_parent.id=(select t.parent_id from sys_resource t where t.id=id))")
	private Integer parentWeight;
	/**
	 * 是否有叶子节点
	 */
	@Formula(value = "(select count(*) from sys_resource f_t where f_t.parent_id = id)")
	private boolean hasChildren;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getTreetableIds() {
		String selfId = makeSelfAsNewParentIds().replace("/", "-");
		return selfId.substring(0, selfId.length() - 1);
	}

	public String getTreetableParentIds() {
		String parentIds = getParentIds().replace("/", "-");
		return parentIds.substring(0, parentIds.length() - 1);
	}

	@Override
	public String getSeparator() {
		return "/";
	}

	public Boolean getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getParentWeight() {
		return parentWeight;
	}

	public void setParentWeight(Integer parentWeight) {
		this.parentWeight = parentWeight;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public boolean isRoot() {
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

	public CommonStatus getStatus() {
		return status;
	}

	public void setStatus(CommonStatus status) {
		this.status = status;
	}
}