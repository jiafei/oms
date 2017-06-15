package cn.flame.oms.web.controller.sys.user;

import cn.flame.oms.web.controller.UIController;

public class UserController extends UIController {
	public final static String PORTAL_PREFIX = "/sys/user";
	public final static String PORTAL_NAME = "sys/user";

	@Override
	public String getPortalName() {
		return PORTAL_NAME;
	}

	@Override
	public String getPortalPrefix() {
		return PORTAL_PREFIX;
	}
}