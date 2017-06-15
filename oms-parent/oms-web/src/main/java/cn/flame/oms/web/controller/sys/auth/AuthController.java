package cn.flame.oms.web.controller.sys.auth;

import cn.flame.oms.web.controller.UIController;

public class AuthController extends UIController {
	public final static String PORTAL_PREFIX = "/sys/auth";
	public final static String PORTAL_NAME = "sys/auth";

	@Override
	public String getPortalName() {
		return PORTAL_NAME;
	}

	@Override
	public String getPortalPrefix() {
		return PORTAL_PREFIX;
	}
}