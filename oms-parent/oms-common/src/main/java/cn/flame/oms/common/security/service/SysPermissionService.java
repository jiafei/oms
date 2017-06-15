package cn.flame.oms.common.security.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.flame.oms.common.security.entity.SysPermission;
import cn.flame.oms.common.security.repository.SysPermissionRepository;
import cn.flame.oms.common.service.BaseService;
import cn.flame.oms.common.utils.JsonObject;

@Service
public class SysPermissionService extends BaseService<SysPermission, String> {
	@Autowired
	private SysPermissionRepository permissionRepository;

	public JsonObject<?> saveObj(SysPermission obj) {
		if (obj != null && StringUtils.isNotBlank(obj.getId())) {
			return update(obj);
		}
		if (permissionRepository.findByIdentity(obj.getIdentity()) != null) {
			return JsonObject.error("抱歉，该权限标识已存在！请更换。");
		}
		obj.setEditable(Boolean.TRUE);
		super.save(obj);
		return JsonObject.success(obj, "新增成功。");
	}

	public JsonObject<SysPermission> update(SysPermission obj) {
		SysPermission original = findOne(obj.getId());
		super.save(original);
		return JsonObject.success(original, "更新成功！");
	}
}