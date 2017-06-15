package cn.flame.oms.web.controller.sys.role;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.flame.oms.common.security.entity.SysRole;
import cn.flame.oms.common.security.entity.SysRoleResourcePermission;
import cn.flame.oms.common.security.entity.enums.CommonStatus;
import cn.flame.oms.common.security.service.SysPermissionService;
import cn.flame.oms.common.security.service.SysResourceService;
import cn.flame.oms.common.security.service.SysRoleResourcePermissionService;
import cn.flame.oms.common.security.service.SysRoleService;
import cn.flame.oms.common.utils.DataTableJO;
import cn.flame.oms.common.utils.DataTableParameter;
import cn.flame.oms.common.utils.HttpRequests;
import cn.flame.oms.common.utils.JsonObject;

@Controller
@RequestMapping(RoleController.PORTAL_PREFIX)
public class RoleIndexController extends RoleController {
	private static Logger logger = LogManager.getLogger();
	@Autowired
	private SysResourceService sysResourceService;
	@Autowired
	private SysPermissionService sysPermissionService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleResourcePermissionService rrpService;

	@RequestMapping("list")
	public void list() {
	}

	@RequestMapping("listData")
	@ResponseBody
	public DataTableJO<SysRole> listData(String jsonParam,
			HttpServletRequest request, Model model) {
		Map<String, Object> searchParams = HttpRequests
				.getParametersStartingWith(request, "Q_");
		DataTableParameter dataTableParam = getDataTableParameterByJsonParam(jsonParam);
		Page<SysRole> page = sysRoleService.findAll(searchParams,
				buildPageRequest(dataTableParam));
		DataTableJO<SysRole> dtJO = new DataTableJO<SysRole>();
		dtJO.setAaData(page.getContent());
		dtJO.setiTotalDisplayRecords(new Long(page.getTotalElements())
				.intValue());
		dtJO.setiTotalRecords(page.getSize());
		return dtJO;
	}

	@RequestMapping("preAdd")
	public String preAdd(String id, Model model) {
		SysRole obj = null;
		if (StringUtils.isNotBlank(id)) {
			obj = sysRoleService.findOne(id);
		}
		model.addAttribute("obj", obj);
		return getPortalName() + "/add";
	}

	@RequestMapping("add")
	@ResponseBody
	public JsonObject<?> add(SysRole obj) {
		return sysRoleService.saveObj(obj);
	}

	@RequestMapping("changeStatus/{newStatus}")
	@ResponseBody
	public JsonObject<String> changeStatus(String id,
			@PathVariable("newStatus") CommonStatus newStatus) {
		sysRoleService.changeStatus(new String[] { id }, newStatus);
		return JsonObject.success();
	}

	@RequestMapping("manage")
	public void manage(HttpServletRequest request) {
	}

	@RequestMapping("manageData")
	@ResponseBody
	public DataTableJO<SysRoleResourcePermission> manageData(String jsonParam,
			HttpServletRequest request, Model model) {
		Map<String, Object> searchParams = HttpRequests
				.getParametersStartingWith(request, "Q_");
		DataTableParameter dataTableParam = getDataTableParameterByJsonParam(jsonParam);
		Page<SysRoleResourcePermission> page = rrpService.findAll(searchParams,
				buildPageRequest(dataTableParam));
		DataTableJO<SysRoleResourcePermission> dtJO = new DataTableJO<SysRoleResourcePermission>();
		dtJO.setAaData(page.getContent());
		dtJO.setiTotalDisplayRecords(new Long(page.getTotalElements())
				.intValue());
		dtJO.setiTotalRecords(page.getSize());
		return dtJO;
	}

	@RequestMapping("preAddResource")
	public String preAddResource(String roleId, String resourceId, Model model) {
		model.addAttribute("resourceList", sysResourceService.findEnable());
		model.addAttribute("permissionList", sysPermissionService.findAll());
		model.addAttribute("obj",
				rrpService.findByRoleAndResource(roleId, resourceId));
		return getPortalName() + "/addResource";
	}

	@RequestMapping("addResource")
	@ResponseBody
	public JsonObject<String> addResource(SysRoleResourcePermission obj) {
		rrpService.save(obj);
		return JsonObject.success();
	}

	@RequestMapping("checkExist")
	@ResponseBody
	public JsonObject<String> checkExist(String roleId, String resourceId) {
		if (rrpService.findByRoleAndResource(roleId, resourceId) == null) {
			return JsonObject.success();
		} else {
			return JsonObject.error("该角色已拥有该资源，请选择其他资源，若需修改该资源权限，请操作\"管理\"按钮");
		}
	}
}