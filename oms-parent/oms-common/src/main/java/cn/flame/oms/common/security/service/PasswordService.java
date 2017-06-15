package cn.flame.oms.common.security.service;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.flame.oms.common.exception.UserPasswordNotMatchException;
import cn.flame.oms.common.security.entity.SysUser;
import cn.flame.oms.common.utils.UserLogUtils;

@Service
public class PasswordService {
//	@Autowired
//	private CacheManager ehcacheManager;

//	private Cache loginRecordCache;

	@Value(value = "${user.password.maxRetryCount}")
	private int maxRetryCount = 10;

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	@PostConstruct
	public void init() {
//		loginRecordCache = ehcacheManager.getCache("loginRecordCache");
	}

	public void validate(SysUser user, String password) {
		String username = user.getUsername();

		int retryCount = 0;

		/*Element cacheElement = loginRecordCache.get(username);
		if (cacheElement != null) {
			retryCount = (Integer) cacheElement.getObjectValue();
			if (retryCount >= maxRetryCount) {
				UserLogUtils
						.log(username,
								"passwordError",
								"password error, retry limit exceed! password: {},max retry count {}",
								password, maxRetryCount);
				throw new UserPasswordRetryLimitExceedException(maxRetryCount);
			}
		}*/

		if (!matches(user, password)) {
//			loginRecordCache.put(new Element(username, ++retryCount));
			UserLogUtils.log(username, "passwordError",
					"password error! password: {} retry count: {}", password,
					retryCount);
			throw new UserPasswordNotMatchException();
		} else {
			clearLoginRecordCache(username);
		}
	}

	public boolean matches(SysUser user, String newPassword) {
		return user.getPassword()
				.equals(encryptPassword(user.getUsername(), newPassword,
						user.getSalt()));
	}

	public void clearLoginRecordCache(String username) {
//		loginRecordCache.remove(username);
	}

	public String encryptPassword(String username, String password, String salt) {
		return DigestUtils.md5Hex(username + password + salt);
	}

	public static void main(String[] args) {
		System.out.println(new PasswordService().encryptPassword("monitor",
				"123456", "iY71e4d123"));
	}
}