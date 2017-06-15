package cn.flame.oms.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import cn.flame.oms.common.utils.DataTableParameter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class UIController {
	@InitBinder
	public void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		// binder.registerCustomEditor(Date.class, new CustomDateEditor(
		// new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		doInitBinder(request, binder);
	}

	protected void doInitBinder(HttpServletRequest requst,
			ServletRequestDataBinder binder) {
	}

	public abstract String getPortalName();

	public abstract String getPortalPrefix();

	protected String redirect(String path) {
		return "redirect:/" + getPortalName() + path;
	}

	protected String forward(String path) {
		return "forward:/" + getPortalName() + path;
	}

	protected String forwardRoot(String path) {
		return "forward:/" + path;
	}

	protected String page(String path) {
		return "/" + getPortalName() + path;
	}

	protected PageRequest buildPageRequest(HttpServletRequest request) {
		return buildPageRequest(request, Constants.PAGE_SIZE);
	}

	protected PageRequest buildPageRequest(HttpServletRequest request,
			int pageSize) {
		int pageNo = 0;
		try {
			pageNo = Integer.parseInt(request.getParameter("page")) - 1;
		} catch (Exception e) {
			pageNo = 0;
		}
		if (pageSize == 0) {
			pageSize = Constants.PAGE_SIZE;
		}
		return new PageRequest(pageNo, pageSize);
	}

	protected PageRequest buildPageRequest(HttpServletRequest request, Sort sort) {
		return buildPageRequest(request, Constants.PAGE_SIZE, sort);
	}

	protected PageRequest buildPageRequest(HttpServletRequest request,
			int pageSize, Sort sort) {
		int pageNo = 0;
		try {
			pageNo = Integer.parseInt(request.getParameter("page")) - 1;
		} catch (Exception e) {
			pageNo = 0;
		}
		if (pageSize == 0) {
			pageSize = Constants.PAGE_SIZE;
		}
		return new PageRequest(pageNo, pageSize, sort);
	}

	public String getWebInf(HttpSession session) {
		return session.getServletContext().getRealPath("/WEB-INF");
	}

	protected PageRequest buildPageRequest(DataTableParameter dataTableParam) {
		int iDisplayStart = dataTableParam.getiDisplayStart();
		int iDisplayLength = dataTableParam.getiDisplayLength();
		List<String> iSortColsName = dataTableParam.getiSortColsName();
		List<String> sSortDirs = dataTableParam.getsSortDirs();

		PageRequest pageRequest = null;
		if (CollectionUtils.isNotEmpty(iSortColsName)) {
			List<Order> orders = Lists.newArrayList();
			for (int i = 0; i < iSortColsName.size(); i++) {
				String sortCol = iSortColsName.get(i);
				String dir = sSortDirs.get(i);
				orders.add(new Order(Direction.fromStringOrNull(dir), sortCol));
			}
			Sort sort = new Sort(orders);
			pageRequest = new PageRequest(iDisplayStart / iDisplayLength,
					iDisplayLength, sort);
		} else {
			pageRequest = new PageRequest(iDisplayStart / iDisplayLength,
					iDisplayLength);
		}
		return pageRequest;
	}

	protected DataTableParameter getDataTableParameterByJsonParam(
			String jsonParam) {
		Map<String, Object> map = covertJsonStringToHashMap(jsonParam);
		int sEcho = (int) (map.get("sEcho") != null ? map.get("sEcho") : 0);
		int iDisplayStart = (int) (map.get("iDisplayStart") != null ? map
				.get("iDisplayStart") : 0);
		int iDisplayLength = (int) (map.get("iDisplayLength") != null ? map
				.get("iDisplayLength") : 0);
		int iColumns = (int) (map.get("iColumns") != null ? map.get("iColumns")
				: 0);
		int iSortingCols = (int) (map.get("iSortingCols") != null ? map
				.get("iSortingCols") : 0);

		List<String> mDataProps = Lists.newArrayList();
		List<Boolean> bSortables = Lists.newArrayList();
		for (int i = 0; i < iColumns; i++) {
			String dataProp = (String) map.get("mDataProp_" + i);
			Boolean sortable = (Boolean) map.get("bSortable_" + i);
			mDataProps.add(dataProp);
			bSortables.add(sortable);
		}

		List<Integer> iSortCols = Lists.newArrayList();
		List<String> sSortDirs = Lists.newArrayList();
		List<String> iSortColsName = Lists.newArrayList();
		for (int i = 0; i < iSortingCols; i++) {
			Integer sortCol = (Integer) map.get("iSortCol_" + i);
			String sortColName = mDataProps.get(sortCol);
			String sortDir = (String) map.get("sSortDir_" + i);
			iSortCols.add(sortCol);
			sSortDirs.add(sortDir);
			iSortColsName.add(sortColName);
		}

		return new DataTableParameter(sEcho, iDisplayStart, iDisplayLength,
				iColumns, mDataProps, bSortables, iSortingCols, iSortCols,
				iSortColsName, sSortDirs);
	}

	protected Map<String, Object> covertJsonStringToHashMap(String jsonParam) {
		JSONArray jsonArray = JSONArray.parseArray(jsonParam);
		Map<String, Object> map = Maps.newHashMap();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			map.put(jsonObj.getString("name"), jsonObj.get("value"));
		}
		return map;
	}
}