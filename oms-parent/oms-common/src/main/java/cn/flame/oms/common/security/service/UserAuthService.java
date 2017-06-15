package cn.flame.oms.common.security.service;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.flame.oms.common.security.entity.SysAuth;
import cn.flame.oms.common.security.entity.SysPermission;
import cn.flame.oms.common.security.entity.SysResource;
import cn.flame.oms.common.security.entity.SysRole;
import cn.flame.oms.common.security.entity.SysRoleResourcePermission;
import cn.flame.oms.common.security.entity.SysUser;
import cn.flame.oms.common.security.entity.enums.CommonStatus;

import com.google.common.collect.Sets;

@Service
public class UserAuthService {
	@Autowired
	private SysOrgService organizationService;

	@Autowired
	private SysAuthService authService;

	@Autowired
	private SysRoleService roleService;

	@Autowired
	private SysResourceService resourceService;

	@Autowired
	private SysPermissionService permissionService;

	public Set<SysRole> findRoles(SysUser user) {
		if (user == null) {
			return Sets.newHashSet();
		}
		SysAuth auth = authService.findByUser(user.getId());
		if (auth == null || auth.getRoleIds().isEmpty()) {
			return Sets.newHashSet();
		}
		Set<SysRole> roles = roleService.findShowRoles(authService.findByUser(
				user.getId()).getRoleIds());
		return roles;
	}

	public Set<String> findStringRoles(SysUser user) {
		return authService.findByUser(user.getId()).getRoleIds();
	}

	/**
	 * 根据角色获取 权限字符串 如sys:admin
	 *
	 * @param user
	 * @return
	 */
	public Set<String> findStringPermissions(SysUser user) {
		Set<String> permissions = Sets.newHashSet();

		Set<SysRole> roles = ((UserAuthService) AopContext.currentProxy())
				.findRoles(user);
		for (SysRole role : roles) {
			for (SysRoleResourcePermission rrp : role.getResourcePermissions()) {
				SysResource resource = resourceService.findOne(rrp
						.getResourceId());

				String actualResourceIdentity = resourceService
						.findActualResourceIdentity(resource);

				// 不可用 即没查到 或者标识字符串不存在
				if (resource == null
						|| StringUtils.isBlank(actualResourceIdentity)
						|| CommonStatus.Disabled.equals(resource.getStatus())) {
					continue;
				}

				for (String permissionId : rrp.getPermissionIds()) {
					SysPermission permission = permissionService
							.findOne(permissionId);
					permissions.add(actualResourceIdentity + ":"
							+ permission.getIdentity());
				}
			}
		}
		return permissions;
	}
}