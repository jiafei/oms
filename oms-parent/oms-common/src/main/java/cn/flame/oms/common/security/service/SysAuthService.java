package cn.flame.oms.common.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.flame.oms.common.security.entity.SysAuth;
import cn.flame.oms.common.security.repository.SysAuthRepository;
import cn.flame.oms.common.service.BaseService;

@Service
public class SysAuthService extends BaseService<SysAuth, String> {
	@Autowired
	private SysAuthRepository authRepository;
	@Autowired
	private SysUserService userService;

	public SysAuth findByUser(String userId) {
		return authRepository.findByUserId(userId);
	}
}