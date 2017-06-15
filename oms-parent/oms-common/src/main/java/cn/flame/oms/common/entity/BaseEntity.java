package cn.flame.oms.common.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * <p>
 * 抽象实体基类，提供统一的ID，和相关的基本功能方法
 * <p>
 * 如果是Oracle请参考{@link BaseOracleEntity}
 */
@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> {
	@Id
	@GeneratedValue(generator = "system_uuid2")
	@GenericGenerator(name = "system_uuid2", strategy = "uuid2")
	private ID id;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}
}