package cn.flame.oms.common.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 *
 * @param <ID>
 */
@MappedSuperclass
public abstract class BaseSysEntity<ID extends Serializable> extends
		BaseEntity<ID> {
	private Boolean editable;

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
}