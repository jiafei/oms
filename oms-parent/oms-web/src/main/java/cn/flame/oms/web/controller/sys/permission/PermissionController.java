package cn.flame.oms.web.controller.sys.permission;

import cn.flame.oms.web.controller.UIController;

public class PermissionController extends UIController {
	public final static String PORTAL_PREFIX = "/sys/permission";
	public final static String PORTAL_NAME = "sys/permission";

	@Override
	public String getPortalName() {
		return PORTAL_NAME;
	}

	@Override
	public String getPortalPrefix() {
		return PORTAL_PREFIX;
	}
}