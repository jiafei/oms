package cn.flame.oms.common.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import cn.flame.oms.common.security.entity.SysRoleResourcePermission;
import cn.flame.oms.common.security.repository.SysRoleResourcePermissionRepository;
import cn.flame.oms.common.service.BaseService;

@Service
public class SysRoleResourcePermissionService extends
		BaseService<SysRoleResourcePermission, String> {
	@Autowired
	private SysRoleResourcePermissionRepository rrpRepository;
	@Autowired
	private SysRoleService roleService;
	@Autowired
	private SysResourceService resourceService;

	public SysRoleResourcePermission findByRoleAndResource(String roleId,
			String resourceId) {
		return rrpRepository.findByRoleAndResource(roleId, resourceId);
	}

	public SysRoleResourcePermission save(SysRoleResourcePermission obj) {
		SysRoleResourcePermission rrp = rrpRepository.findByRoleAndResource(obj
				.getRole().getId(), obj.getResource().getId());
		if (rrp != null) {
			rrp.setPermissionIds(obj.getPermissionIds());
		} else {
			rrp = obj;
		}
		if (rrp.getPermissionIds() != null
				&& rrp.getPermissionIds().contains("1")) {
			rrp.setPermissionIds(Sets.newHashSet("1"));
		}
		return rrpRepository.save(rrp);
	}
}