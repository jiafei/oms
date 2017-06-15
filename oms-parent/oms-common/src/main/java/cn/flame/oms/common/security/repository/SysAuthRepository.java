package cn.flame.oms.common.security.repository;

import org.springframework.data.jpa.repository.Query;

import cn.flame.oms.common.repository.support.BaseRepository;
import cn.flame.oms.common.security.entity.SysAuth;

public interface SysAuthRepository extends BaseRepository<SysAuth, String> {
	@Query("from SysAuth where user.id=?1")
	SysAuth findByUserId(String userId);
}