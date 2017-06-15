package cn.flame.oms.common.security.repository;

import cn.flame.oms.common.repository.support.BaseRepository;
import cn.flame.oms.common.security.entity.SysRole;

public interface SysRoleRepository extends BaseRepository<SysRole, String> {
	SysRole findByIdentity(String identity);
}