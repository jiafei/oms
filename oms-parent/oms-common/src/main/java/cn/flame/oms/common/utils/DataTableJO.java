package cn.flame.oms.common.utils;

import java.util.List;

/**
 * http://legacy.datatables.net/usage/server-side
 */
public class DataTableJO<T> {
	private List<T> data; // 数据
	private int iTotalDisplayRecords; // 得到的记录数
	private int iTotalRecords;// 数据库中记录数
	private int sEcho; // 请求服务器端次数

	public List<T> getAaData() {
		return data;
	}

	public void setAaData(List<T> aaData) {
		this.data = aaData;
	}

	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public int getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public int getsEcho() {
		return sEcho;
	}

	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}
}