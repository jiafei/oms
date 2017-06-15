package cn.flame.oms.common.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *  
 * <p>
 * 抽象实体基类，为Oracle这类由seq统一生成主键，和相关的基本功能方法
 * <p>
 * 如果是采用如MySQL这类自动生成主键的数据库，可参考{@link BaseEntity}通过UUID方式生成主键。
 * <p/>
 * 子类只需要在类头上加 @SequenceGenerator(name="seq", sequenceName="你的sequence名字")
 */
@MappedSuperclass
public abstract class BaseOracleEntity<PK extends Serializable> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	private PK id;

	public PK getId() {
		return id;
	}

	public void setId(PK id) {
		this.id = id;
	}
}