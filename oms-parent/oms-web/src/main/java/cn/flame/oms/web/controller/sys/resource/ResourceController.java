package cn.flame.oms.web.controller.sys.resource;

import cn.flame.oms.web.controller.UIController;

public class ResourceController extends UIController {
	public final static String PORTAL_PREFIX = "/sys/resource";
	public final static String PORTAL_NAME = "sys/resource";

	@Override
	public String getPortalName() {
		return PORTAL_NAME;
	}

	@Override
	public String getPortalPrefix() {
		return PORTAL_PREFIX;
	}
}