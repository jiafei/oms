package cn.flame.oms.web.controller.sys.permission;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.flame.oms.common.security.entity.SysPermission;
import cn.flame.oms.common.security.service.SysPermissionService;
import cn.flame.oms.common.utils.DataTableJO;
import cn.flame.oms.common.utils.DataTableParameter;
import cn.flame.oms.common.utils.HttpRequests;
import cn.flame.oms.common.utils.JsonObject;

@Controller
@RequestMapping(PermissionController.PORTAL_PREFIX)
public class PermissionIndexController extends PermissionController {
	private static Logger logger = LogManager.getLogger();
	@Autowired
	private SysPermissionService sysPermissionService;

	@RequestMapping("list")
	public void list() {
	}

	@RequestMapping("listData")
	@ResponseBody
	public DataTableJO<SysPermission> listData(String jsonParam,
			HttpServletRequest request, Model model) {
		Map<String, Object> searchParams = HttpRequests
				.getParametersStartingWith(request, "Q_");
		DataTableParameter dataTableParam = getDataTableParameterByJsonParam(jsonParam);
		Page<SysPermission> page = sysPermissionService.findAll(searchParams,
				buildPageRequest(dataTableParam));
		DataTableJO<SysPermission> dtJO = new DataTableJO<SysPermission>();
		dtJO.setAaData(page.getContent());
		dtJO.setiTotalDisplayRecords(new Long(page.getTotalElements())
				.intValue());
		dtJO.setiTotalRecords(page.getSize());
		return dtJO;
	}

	@RequestMapping("preAdd")
	public String preAdd(String id, Model model) {
		SysPermission obj = null;
		if (StringUtils.isNotBlank(id)) {
			obj = sysPermissionService.findOne(id);
		}
		model.addAttribute("obj", obj);
		return getPortalName() + "/add";
	}

	@RequestMapping("add")
	@ResponseBody
	public JsonObject<?> add(SysPermission obj) {
		return sysPermissionService.saveObj(obj);
	}
}