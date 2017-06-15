package cn.flame.oms.common.security.repository;

import cn.flame.oms.common.repository.support.BaseRepository;
import cn.flame.oms.common.security.entity.SysUser;

public interface SysUserRepository extends BaseRepository<SysUser, String> {
	SysUser findByUsername(String username);

	SysUser findByMobilePhone(String mobilePhone);

	SysUser findByEmail(String email);
}