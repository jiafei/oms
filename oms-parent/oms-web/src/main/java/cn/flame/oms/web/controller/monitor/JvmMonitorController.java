package cn.flame.oms.web.controller.monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.flame.oms.web.controller.UIController;

@Controller
@RequestMapping("/monitor/jvm")
@RequiresPermissions("monitor:jvm:*")
public class JvmMonitorController extends UIController {
	private static Logger logger = LogManager.getLogger();

	@RequestMapping("/index")
	public void index() {
		logger.debug("in monitor index.");
	}

	@Override
	public String getPortalName() {
		return null;
	}

	@Override
	public String getPortalPrefix() {
		return null;
	}
}