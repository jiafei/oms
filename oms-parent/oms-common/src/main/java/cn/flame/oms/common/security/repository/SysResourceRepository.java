package cn.flame.oms.common.security.repository;

import cn.flame.oms.common.repository.support.BaseRepository;
import cn.flame.oms.common.security.entity.SysResource;

public interface SysResourceRepository extends
		BaseRepository<SysResource, String> {
	SysResource findByIdentity(String identity);
}