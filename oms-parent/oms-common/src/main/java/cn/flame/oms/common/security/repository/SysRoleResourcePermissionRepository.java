package cn.flame.oms.common.security.repository;

import org.springframework.data.jpa.repository.Query;

import cn.flame.oms.common.repository.support.BaseRepository;
import cn.flame.oms.common.security.entity.SysRole;
import cn.flame.oms.common.security.entity.SysRoleResourcePermission;

public interface SysRoleResourcePermissionRepository extends
		BaseRepository<SysRoleResourcePermission, String> {
	// 使用hibernate查询缓存的方式如下：
	// @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true")
	// })
	@Query("from SysRoleResourcePermission where role=?1 and resourceId=?2")
	SysRoleResourcePermission findRoleResourcePermission(SysRole role,
			String resourceId);

	@Query("from SysRoleResourcePermission where role.id=?1 and resource.id=?2")
	SysRoleResourcePermission findByRoleAndResource(String roleId,
			String resourceId);
}