package cn.flame.oms.web.controller.sys.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.flame.oms.common.security.entity.SysResource;
import cn.flame.oms.common.security.service.SysResourceService;
import cn.flame.oms.common.utils.JsonObject;

@Controller
@RequestMapping(ResourceController.PORTAL_PREFIX)
public class ResourceIndexController extends ResourceController {
	private static Logger logger = LogManager.getLogger();
	@Autowired
	private SysResourceService sysResourceService;

	@RequestMapping("list")
	public void list(HttpServletRequest request, Model model) {
	}

	@RequestMapping("listTreeData")
	@ResponseBody
	public JsonObject<List<SysResource>> listTreeData() {
		return JsonObject.success(sysResourceService
				.findAll(new Sort("weight")));
	}

	@RequestMapping("preAdd")
	public String preAdd(String id, String parentId, Model model) {
		SysResource obj = null;
		if (StringUtils.isNotBlank(parentId)) {
			obj = sysResourceService.findOne(parentId);
		}
		model.addAttribute("obj", obj);
		return getPortalName() + "/add";
	}

	@RequestMapping("add")
	@ResponseBody
	public JsonObject<?> add(String parentId, SysResource obj) {
		SysResource parent = null;
		if (StringUtils.isNotBlank(parentId)) {
			parent = sysResourceService.findOne(parentId);
			sysResourceService.appendChild(parent, obj);
		}
		return sysResourceService.saveObj(obj);
	}

	@RequestMapping("preEdit")
	public String preEdit(String id, Model model) {
		SysResource obj = null;
		SysResource parentObj = null;
		if (StringUtils.isNotBlank(id)) {
			obj = sysResourceService.findOne(id);
			parentObj = sysResourceService.findOne(obj.getParentId());
		}
		model.addAttribute("obj", obj);
		model.addAttribute("parentObj", parentObj);
		return getPortalName() + "/edit";
	}

	@RequestMapping("edit")
	@ResponseBody
	public JsonObject<String> edit(SysResource obj) {
		sysResourceService.update(obj);
		return JsonObject.success();
	}
}