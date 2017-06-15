package cn.flame.oms.common.repository.support;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class SimpleBaseRepositoryFactoryBean<R extends JpaRepository<M, ID>, M, ID extends Serializable>
		extends JpaRepositoryFactoryBean<R, M, ID> {

	public SimpleBaseRepositoryFactoryBean() {
	}

	protected RepositoryFactorySupport createRepositoryFactory(
			EntityManager entityManager) {
		return new SimpleBaseRepositoryFactory<M, ID>(entityManager);
	}
}

class SimpleBaseRepositoryFactory<M, ID extends Serializable> extends
		JpaRepositoryFactory {

	private EntityManager entityManager;

	public SimpleBaseRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	protected Object getTargetRepository(RepositoryInformation information) {
		JpaEntityInformation<M, ID> entityInformation = getEntityInformation((Class<M>) information
				.getDomainType());
		return new SimpleBaseRepositoryImpl<M, ID>(entityInformation,
				entityManager);
	}

	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return BaseRepository.class;
	}
}