package cn.flame.oms.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.flame.oms.common.security.entity.SysUser;
import cn.flame.oms.common.security.service.SysResourceService;
import cn.flame.oms.common.utils.Menu;
import cn.flame.oms.web.controller.bind.CurrentUser;

@Controller
public class IndexController extends UIController {
	private static Logger logger = LogManager.getLogger();
	@Autowired
	private SysResourceService resourceService;

	@RequestMapping("/")
	public String root(@CurrentUser SysUser user, Model model) {
		logger.debug("In root.");
		List<Menu> menus = resourceService.findMenus(user);
		model.addAttribute("menus", menus);
		return "index";
	}

	@RequestMapping("/login")
	public void login(HttpServletRequest request, ModelMap model) {
		logger.debug("In login.");
	}

	@RequestMapping("/index")
	public void index(Model model) {
		logger.debug("In index.");
	}

	@Override
	public String getPortalName() {
		return "";
	}

	@Override
	public String getPortalPrefix() {
		return "";
	}
}