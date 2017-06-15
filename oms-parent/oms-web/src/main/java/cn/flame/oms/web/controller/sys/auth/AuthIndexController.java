package cn.flame.oms.web.controller.sys.auth;

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

import cn.flame.oms.common.security.entity.SysAuth;
import cn.flame.oms.common.security.service.SysAuthService;
import cn.flame.oms.common.utils.DataTableJO;
import cn.flame.oms.common.utils.DataTableParameter;
import cn.flame.oms.common.utils.HttpRequests;

@Controller
@RequestMapping(AuthController.PORTAL_PREFIX)
public class AuthIndexController extends AuthController {
	private static Logger logger = LogManager.getLogger();
	@Autowired
	private SysAuthService sysAuthService;

	@RequestMapping("list")
	public void list() {
	}

	@RequestMapping("listData")
	@ResponseBody
	public DataTableJO<SysAuth> listData(String jsonParam,
			HttpServletRequest request, Model model) {
		Map<String, Object> searchParams = HttpRequests
				.getParametersStartingWith(request, "Q_");
		DataTableParameter dataTableParam = getDataTableParameterByJsonParam(jsonParam);
		Page<SysAuth> page = sysAuthService.findAll(searchParams,
				buildPageRequest(dataTableParam));
		DataTableJO<SysAuth> dtJO = new DataTableJO<SysAuth>();
		dtJO.setAaData(page.getContent());
		dtJO.setiTotalDisplayRecords(new Long(page.getTotalElements())
				.intValue());
		dtJO.setiTotalRecords(page.getSize());
		return dtJO;
	}

	@RequestMapping("preAdd")
	public String preAdd(String id, Model model) {
		SysAuth obj = null;
		if (StringUtils.isNotBlank(id)) {
			obj = sysAuthService.findOne(id);
		}
		model.addAttribute("obj", obj);
		return getPortalName() + "/add";
	}

	@RequestMapping("add")
	public void add(SysAuth obj) {
		sysAuthService.save(obj);
	}
}