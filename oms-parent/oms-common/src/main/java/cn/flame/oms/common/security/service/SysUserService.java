package cn.flame.oms.common.security.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.flame.oms.common.exception.UserBlockedException;
import cn.flame.oms.common.exception.UserNotExistsException;
import cn.flame.oms.common.exception.UserPasswordNotMatchException;
import cn.flame.oms.common.security.entity.SysUser;
import cn.flame.oms.common.security.entity.enums.UserStatus;
import cn.flame.oms.common.security.repository.SysUserRepository;
import cn.flame.oms.common.service.BaseService;
import cn.flame.oms.common.utils.JsonObject;
import cn.flame.oms.common.utils.UserLogUtils;

@Service
public class SysUserService extends BaseService<SysUser, String> {
	@Autowired
	private SysUserRepository userRepository;

	@Autowired
	private PasswordService passwordService;

	public JsonObject<?> saveObj(SysUser user) {
		if (user != null && StringUtils.isNotBlank(user.getId())) {
			return update(user);
		}
		if (findByUsername(user.getUsername()) != null) {
			return JsonObject.error("抱歉，该用户名已存在！请更换。");
		}
		if (user.getCreateDate() == null) {
			user.setCreateDate(new Date());
		}
		user.randomSalt();
		user.setPassword(passwordService.encryptPassword(user.getUsername(),
				user.getPassword(), user.getSalt()));

		userRepository.save(user);
		return JsonObject.success(user, "新增成功。");
	}

	public JsonObject<SysUser> update(SysUser user) {
		SysUser originalUser = findOne(user.getId());
		originalUser.setEmail(user.getEmail());
		originalUser.setMobilePhone(user.getMobilePhone());
		if (!user.getPassword().equals(originalUser.getPassword())) {
			user.setPassword(passwordService.encryptPassword(
					user.getUsername(), user.getPassword(), user.getSalt()));
		}

		userRepository.save(originalUser);
		return JsonObject.success(user, "更新成功！");
	}

	public SysUser findByUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return null;
		}
		return userRepository.findByUsername(username);
	}

	public SysUser findByEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return null;
		}
		return userRepository.findByEmail(email);
	}

	public SysUser findByMobilePhone(String mobilePhone) {
		if (StringUtils.isEmpty(mobilePhone)) {
			return null;
		}
		return userRepository.findByMobilePhone(mobilePhone);
	}

	public SysUser changePassword(SysUser user, String newPassword) {
		user.randomSalt();
		user.setPassword(passwordService.encryptPassword(user.getUsername(),
				newPassword, user.getSalt()));
		save(user);
		return user;
	}

	public SysUser changeStatus(SysUser opUser, SysUser user,
			UserStatus newStatus, String reason) {
		user.setStatus(newStatus);
		save(user);
		// userStatusHistoryService.log(opUser, user, newStatus, reason);
		return user;
	}

	public SysUser login(String username, String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			UserLogUtils.log(username, "loginError", "username is empty");
			throw new UserNotExistsException();
		}
		// 密码如果不在指定范围内 肯定错误
		if (password.length() < SysUser.PASSWORD_MIN_LENGTH
				|| password.length() > SysUser.PASSWORD_MAX_LENGTH) {
			UserLogUtils.log(username, "loginError",
					"password length error! password is between {} and {}",
					SysUser.PASSWORD_MIN_LENGTH, SysUser.PASSWORD_MAX_LENGTH);

			throw new UserPasswordNotMatchException();
		}

		SysUser user = null;

		// 此处需要走代理对象，目的是能走缓存切面
		SysUserService proxyUserService = (SysUserService) AopContext
				.currentProxy();
		if (maybeUsername(username)) {
			user = proxyUserService.findByUsername(username);
		}

		if (user == null && maybeEmail(username)) {
			user = proxyUserService.findByEmail(username);
		}

		if (user == null && maybeMobilePhoneNumber(username)) {
			user = proxyUserService.findByMobilePhone(username);
		}

		if (user == null) {
			UserLogUtils.log(username, "loginError", "user is not exists!");
			throw new UserNotExistsException();
		}

		passwordService.validate(user, password);

		if (user.getStatus() == UserStatus.Blocked) {
			UserLogUtils.log(username, "loginError", "user is locked!");
			// throw new UserLockedException(
			// userStatusHistoryService.getLastReason(user));
			throw new UserBlockedException("失败");
		}

		UserLogUtils.log(username, "loginSuccess", "");
		return user;
	}

	private boolean maybeUsername(String username) {
		if (!username.matches(SysUser.USERNAME_PATTERN)) {
			return false;
		}
		// 如果用户名不在指定范围内也是错误的
		if (username.length() < SysUser.USERNAME_MIN_LENGTH
				|| username.length() > SysUser.USERNAME_MAX_LENGTH) {
			return false;
		}

		return true;
	}

	private boolean maybeEmail(String username) {
		if (!username.matches(SysUser.EMAIL_PATTERN)) {
			return false;
		}
		return true;
	}

	private boolean maybeMobilePhoneNumber(String username) {
		if (!username.matches(SysUser.MOBILE_PHONE_NUMBER_PATTERN)) {
			return false;
		}
		return true;
	}

	public void changePassword(SysUser opUser, String[] ids, String newPassword) {
		SysUserService proxyUserService = (SysUserService) AopContext
				.currentProxy();
		for (String id : ids) {
			SysUser user = findOne(id);
			proxyUserService.changePassword(user, newPassword);
			UserLogUtils.log(user.getUsername(), "changePassword",
					"admin user {} change password!", opUser.getUsername());

		}
	}

	public void changeStatus(SysUser opUser, String[] ids,
			UserStatus newStatus, String reason) {
		SysUserService proxyUserService = (SysUserService) AopContext
				.currentProxy();
		for (String id : ids) {
			SysUser user = findOne(id);
			proxyUserService.changeStatus(opUser, user, newStatus, reason);
			UserLogUtils.log(user.getUsername(), "changeStatus",
					"admin user {} change status!", opUser.getUsername());
		}
	}

	// public Set<Map<String, Object>> findIdAndNames(
	// Map<String, Object> searchParams, String usernme) {
	// searchParams.put("LIKE_username", usernme);
	// searchParams.put("EQ_deleted", false);
	//
	// return Sets.newHashSet(Lists.transform(findAll(searchParams),
	// new Function<SysUser, Map<String, Object>>() {
	// @Override
	// public Map<String, Object> apply(SysUser input) {
	// Map<String, Object> data = Maps.newHashMap();
	// data.put("label", input.getUsername());
	// data.put("value", input.getId());
	// return data;
	// }
	// }));
	// }
}