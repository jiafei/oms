package cn.flame.oms.web.controller.sys.role;

import cn.flame.oms.web.controller.UIController;

public class RoleController extends UIController {
	public final static String PORTAL_PREFIX = "/sys/role";
	public final static String PORTAL_NAME = "sys/role";

	@Override
	public String getPortalName() {
		return PORTAL_NAME;
	}

	@Override
	public String getPortalPrefix() {
		return PORTAL_PREFIX;
	}
}