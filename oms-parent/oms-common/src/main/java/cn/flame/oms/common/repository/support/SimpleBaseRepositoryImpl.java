package cn.flame.oms.common.repository.support;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class SimpleBaseRepositoryImpl<M, ID extends Serializable> extends
		SimpleJpaRepository<M, ID> implements BaseRepository<M, ID> {
	public static final String LOGIC_DELETE_ALL_QUERY_STRING = "update %s x set x.deleted=true where x in (?1)";
	public static final String DELETE_ALL_QUERY_STRING = "delete from %s x where x in (?1)";
	public static final String FIND_QUERY_STRING = "from %s x where 1=1 ";
	public static final String COUNT_QUERY_STRING = "select count(x) from %s x where 1=1 ";

	private final EntityManager em;
	private final JpaEntityInformation<M, ID> entityInformation;


	private Class<M> entityClass;
	private String entityName;
	private String idName;

	public SimpleBaseRepositoryImpl(
			JpaEntityInformation<M, ID> entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);

		this.entityInformation = entityInformation;
		this.entityClass = this.entityInformation.getJavaType();
		this.entityName = this.entityInformation.getEntityName();
		this.idName = this.entityInformation.getIdAttributeNames().iterator()
				.next();
		this.em = entityManager;
	}
}