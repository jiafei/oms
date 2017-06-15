package cn.flame.oms.common.utils;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class Menu implements Serializable {
	private static final long serialVersionUID = -4587743252926297117L;
	private String id;
	private String identity;
	private String name;
	private String icon;
	private String url;

	private List<Menu> children;

	public Menu(String id, String identity, String name, String icon, String url) {
		this.id = id;
		this.identity = identity;
		this.name = name;
		this.icon = icon;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Menu> getChildren() {
		if (children == null) {
			children = Lists.newArrayList();
		}
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public boolean isHasChildren() {
		return !getChildren().isEmpty();
	}

	@Override
	public String toString() {
		return "Menu{" + "id=" + id + ", name='" + name + '\'' + ", icon='"
				+ icon + '\'' + ", url='" + url + '\'' + ", children="
				+ children + '}';
	}
}