package cn.flame.oms.web.controller.sys.user;

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

import cn.flame.oms.common.security.entity.SysAuth;
import cn.flame.oms.common.security.entity.SysUser;
import cn.flame.oms.common.security.entity.enums.UserStatus;
import cn.flame.oms.common.security.service.SysAuthService;
import cn.flame.oms.common.security.service.SysResourceService;
import cn.flame.oms.common.security.service.SysRoleService;
import cn.flame.oms.common.security.service.SysUserService;
import cn.flame.oms.common.utils.DataTableJO;
import cn.flame.oms.common.utils.DataTableParameter;
import cn.flame.oms.common.utils.HttpRequests;
import cn.flame.oms.common.utils.JsonObject;
import cn.flame.oms.web.controller.bind.CurrentUser;

@Controller
@RequestMapping(UserController.PORTAL_PREFIX)
public class UserIndexController extends UserController {
	private static Logger logger = LogManager.getLogger();
	@Autowired
	private SysResourceService sysResourceService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysAuthService sysAuthService;

	// @Autowired
	// private PushApi pushApi;

	// @Autowired
	// private MessageService messageService;

	// @Autowired
	// private CalendarService calendarService;

	@RequestMapping("list")
	public void list() {
	}

	@RequestMapping("listData")
	@ResponseBody
	public DataTableJO<SysUser> listData(String jsonParam,
			HttpServletRequest request, Model model) {
		Map<String, Object> searchParams = HttpRequests
				.getParametersStartingWith(request, "Q_");
		DataTableParameter dataTableParam = getDataTableParameterByJsonParam(jsonParam);
		Page<SysUser> page = sysUserService.findAll(searchParams,
				buildPageRequest(dataTableParam));
		DataTableJO<SysUser> dtJO = new DataTableJO<SysUser>();
		dtJO.setAaData(page.getContent());
		dtJO.setiTotalDisplayRecords(new Long(page.getTotalElements())
				.intValue());
		dtJO.setiTotalRecords(page.getSize());
		return dtJO;
	}

	@RequestMapping("preInfoEdit")
	public String preInfoEdit(String username, Model model) {
		SysUser obj = null;
		if (StringUtils.isNotBlank(username)) {
			obj = sysUserService.findByUsername(username);
		}
		model.addAttribute("obj", obj);
		return getPortalName() + "/infoEdit";
	}

	@RequestMapping("infoEdit")
	@ResponseBody
	public JsonObject<?> infoEdit(SysUser obj) {
		return sysUserService.saveObj(obj);
	}

	@RequestMapping("preAdd")
	public String preAdd(String id, Model model) {
		SysUser obj = null;
		if (StringUtils.isNotBlank(id)) {
			obj = sysUserService.findOne(id);
		}
		model.addAttribute("obj", obj);
		return getPortalName() + "/add";
	}

	@RequestMapping("add")
	@ResponseBody
	public JsonObject<?> add(SysUser obj) {
		return sysUserService.saveObj(obj);
	}

	@RequestMapping("changeStatus/{newStatus}")
	@ResponseBody
	public JsonObject<String> changeStatus(String id,
			@PathVariable("newStatus") UserStatus newStatus,
			@CurrentUser SysUser opUser, String reason) {
		sysUserService.changeStatus(opUser, new String[] { id }, newStatus,
				reason);
		return JsonObject.success();
	}

	@RequestMapping("preAddRole")
	public String preAddRole(String userId, Model model) {
		model.addAttribute("roleList", sysRoleService.findAll());
		model.addAttribute("obj", sysAuthService.findByUser(userId));
		model.addAttribute("user", sysUserService.findOne(userId));
		return getPortalName() + "/addRole";
	}

	@RequestMapping("addRole")
	@ResponseBody
	public JsonObject<String> addRole(SysAuth obj) {
		sysAuthService.save(obj);
		return JsonObject.success();
	}

	@RequestMapping("index")
	public void index(@CurrentUser SysUser user, Model model) {
		logger.debug("In index.");
	}
}