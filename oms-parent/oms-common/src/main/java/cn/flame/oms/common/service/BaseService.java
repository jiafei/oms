package cn.flame.oms.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import cn.flame.oms.common.repository.support.BaseRepository;
import cn.flame.oms.common.repository.support.DynamicSpecifications;
import cn.flame.oms.common.repository.support.SearchFilter;

public abstract class BaseService<T, ID extends Serializable> {
	private static Logger logger = LogManager.getLogger();
	@Autowired
	protected BaseRepository<T, ID> baseRepository;

	public T save(T m) {
		return baseRepository.save(m);
	}

	public T saveAndFlush(T m) {
		m = save(m);
		baseRepository.flush();
		return m;
	}

	public void delete(ID id) {
		baseRepository.delete(id);
	}

	public void delete(T m) {
		baseRepository.delete(m);
	}

	public T findOne(ID id) {
		return baseRepository.findOne(id);
	}

	public boolean exists(ID id) {
		return baseRepository.exists(id);
	}

	public long count() {
		return baseRepository.count();
	}

	public List<T> findAll() {
		return baseRepository.findAll();
	}

	public List<T> findAll(Sort sort) {
		return baseRepository.findAll(sort);
	}

	public Page<T> findAll(Pageable pageable) {
		return baseRepository.findAll(pageable);
	}

	public List<T> findAll(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> specification = DynamicSpecifications
				.bySearchFilter(filters.values());
		return baseRepository.findAll(specification);
	}

	public Page<T> findAll(Map<String, Object> searchParams, Pageable pageable) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> specification = DynamicSpecifications
				.bySearchFilter(filters.values());
		return baseRepository.findAll(specification, pageable);
	}

	public List<T> findAll(Specification<T> specification) {
		return baseRepository.findAll(specification);
	}

	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		return baseRepository.findAll(spec, pageable);
	}

	public List<T> findAll(Specification<T> specification, Sort sort) {
		return baseRepository.findAll(specification, sort);
	}
}