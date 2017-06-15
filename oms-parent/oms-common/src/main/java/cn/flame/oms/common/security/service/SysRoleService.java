package cn.flame.oms.common.security.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.flame.oms.common.security.entity.SysRole;
import cn.flame.oms.common.security.entity.SysRoleResourcePermission;
import cn.flame.oms.common.security.entity.enums.CommonStatus;
import cn.flame.oms.common.security.repository.SysRoleRepository;
import cn.flame.oms.common.security.repository.SysRoleResourcePermissionRepository;
import cn.flame.oms.common.service.BaseService;
import cn.flame.oms.common.utils.JsonObject;

import com.google.common.collect.Sets;

@Service
public class SysRoleService extends BaseService<SysRole, String> {
	@Autowired
	private SysRoleRepository roleRepository;
	@Autowired
	private SysRoleResourcePermissionRepository rrpRepository;

	public void changeStatus(String[] ids, CommonStatus newStatus) {
		SysRoleService proxyService = (SysRoleService) AopContext
				.currentProxy();
		for (String id : ids) {
			SysRole role = findOne(id);
			proxyService.changeStatus(role, newStatus);
		}
	}

	public SysRole changeStatus(SysRole role, CommonStatus newStatus) {
		role.setStatus(newStatus);
		save(role);
		return role;
	}

	public JsonObject<?> saveObj(SysRole obj) {
		if (obj != null && StringUtils.isNotBlank(obj.getId())) {
			return update(obj);
		}
		if (roleRepository.findByIdentity(obj.getIdentity()) != null) {
			return JsonObject.error("抱歉，该角色标识已存在！请更换。");
		}
		obj.setEditable(Boolean.TRUE);
		roleRepository.save(obj);
		return JsonObject.success(obj, "新增成功。");
	}

	public JsonObject<SysRole> update(SysRole role) {
		List<SysRoleResourcePermission> localResourcePermissions = role
				.getResourcePermissions();
		for (int i = 0, l = localResourcePermissions.size(); i < l; i++) {
			SysRoleResourcePermission localResourcePermission = localResourcePermissions
					.get(i);
			localResourcePermission.setRole(role);
			SysRoleResourcePermission dbResourcePermission = findRoleResourcePermission(localResourcePermission);
			if (dbResourcePermission != null) {// 出现在先删除再添加的情况
				dbResourcePermission.setRole(localResourcePermission.getRole());
				// dbResourcePermission.setResourceId(localResourcePermission
				// .getResourceId());
				dbResourcePermission.setResource(localResourcePermission
						.getResource());
				dbResourcePermission.setPermissionIds(localResourcePermission
						.getPermissionIds());
				localResourcePermissions.set(i, dbResourcePermission);
			}
		}
		super.save(role);
		return JsonObject.success(role, "更新成功！");
	}

	public Set<SysRole> findShowRoles(Set<String> roleIds) {
		Set<SysRole> roles = Sets.newHashSet();
		// TODO 如果角色很多 此处应该写查询
		for (SysRole role : findAll()) {
			// 判断角色是否可用
			if (!CommonStatus.Enabled.equals(role.getStatus())) {
				continue;
			}
			if (roleIds.contains(role.getId())) {
				roles.add(role);
			}
		}
		return roles;
	}

	private SysRoleResourcePermission findRoleResourcePermission(
			SysRoleResourcePermission roleResourcePermission) {
		return rrpRepository.findRoleResourcePermission(
				roleResourcePermission.getRole(),
				roleResourcePermission.getResourceId());
	}
}