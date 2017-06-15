package cn.flame.oms.common.security.repository;

import cn.flame.oms.common.repository.support.BaseRepository;
import cn.flame.oms.common.security.entity.SysPermission;

public interface SysPermissionRepository extends
		BaseRepository<SysPermission, String> {
	SysPermission findByIdentity(String identity);
}