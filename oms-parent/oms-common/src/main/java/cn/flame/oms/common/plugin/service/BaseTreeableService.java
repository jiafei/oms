package cn.flame.oms.common.plugin.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.flame.oms.common.entity.BaseEntity;
import cn.flame.oms.common.plugin.entity.Treeable;
import cn.flame.oms.common.repository.RepositoryHelper;
import cn.flame.oms.common.repository.support.DynamicSpecifications;
import cn.flame.oms.common.repository.support.SearchFilter;
import cn.flame.oms.common.service.BaseService;
import cn.flame.oms.common.utils.ReflectUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class BaseTreeableService<M extends BaseEntity<ID> & Treeable<ID>, ID extends Serializable>
		extends BaseService<M, ID> {
	private final String DELETE_CHILDREN_QL;
	private final String UPDATE_CHILDREN_PARENT_IDS_QL;
	private final String FIND_SELF_AND_NEXT_SIBLINGS_QL;
	private final String FIND_NEXT_WEIGHT_QL;

	private RepositoryHelper repositoryHelper;

	protected BaseTreeableService() {
		Class<M> entityClass = ReflectUtils
				.findParameterizedType(getClass(), 0);
		repositoryHelper = new RepositoryHelper(entityClass);
		String entityName = repositoryHelper.getEntityName(entityClass);

		DELETE_CHILDREN_QL = String.format(
				"delete from %s where id=?1 or parentIds like concat(?2, %s)",
				entityName, "'%'");

		UPDATE_CHILDREN_PARENT_IDS_QL = String
				.format("update %s set parentIds=(?1 || substring(parentIds, length(?2)+1)) where parentIds like concat(?2, %s)",
						entityName, "'%'");

		FIND_SELF_AND_NEXT_SIBLINGS_QL = String
				.format("from %s where parentIds = ?1 and weight>=?2 order by weight asc",
						entityName);

		FIND_NEXT_WEIGHT_QL = String
				.format("select case when max(weight) is null then 1 else (max(weight) + 1) end from %s where parentId = ?1",
						entityName);

	}

	@Override
	public M save(M m) {
		if (m.getWeight() == null) {
			m.setWeight(nextWeight(m.getParentId()));
		}
		return super.save(m);
	}

	@Transactional
	public void deleteSelfAndChild(M m) {
		repositoryHelper.batchUpdate(DELETE_CHILDREN_QL, m.getId(),
				m.makeSelfAsNewParentIds());
	}

	public void deleteSelfAndChild(List<M> mList) {
		for (M m : mList) {
			deleteSelfAndChild(m);
		}
	}

	public void appendChild(M parent, M child) {
		child.setParentId(parent.getId());
		child.setParentIds(parent.makeSelfAsNewParentIds());
		child.setWeight(nextWeight(parent.getId()));
//		save(child);
	}

	public int nextWeight(ID id) {
		return repositoryHelper.<Integer> findOne(FIND_NEXT_WEIGHT_QL, id);
	}

	/**
	 * 移动节点 根节点不能移动
	 *
	 * @param source
	 *            源节点
	 * @param target
	 *            目标节点
	 * @param moveType
	 *            位置
	 */
	public void move(M source, M target, String moveType) {
		if (source == null || target == null || source.isRoot()) { // 根节点不能移动
			return;
		}

		// 如果是相邻的兄弟 直接交换weight即可
		boolean isSibling = source.getParentId().equals(target.getParentId());
		boolean isNextOrPrevMoveType = "next".equals(moveType)
				|| "prev".equals(moveType);
		if (isSibling && isNextOrPrevMoveType
				&& Math.abs(source.getWeight() - target.getWeight()) == 1) {

			// 无需移动
			if ("next".equals(moveType)
					&& source.getWeight() > target.getWeight()) {
				return;
			}
			if ("prev".equals(moveType)
					&& source.getWeight() < target.getWeight()) {
				return;
			}

			int sourceWeight = source.getWeight();
			source.setWeight(target.getWeight());
			target.setWeight(sourceWeight);
			return;
		}

		// 移动到目标节点之后
		if ("next".equals(moveType)) {
			List<M> siblings = findSelfAndNextSiblings(target.getParentIds(),
					target.getWeight());
			siblings.remove(0);// 把自己移除

			if (siblings.size() == 0) { // 如果没有兄弟了 则直接把源的设置为目标即可
				int nextWeight = nextWeight(target.getParentId());
				updateSelftAndChild(source, target.getParentId(),
						target.getParentIds(), nextWeight);
				return;
			} else {
				moveType = "prev";
				target = siblings.get(0); // 否则，相当于插入到实际目标节点下一个节点之前
			}
		}

		// 移动到目标节点之前
		if ("prev".equals(moveType)) {

			List<M> siblings = findSelfAndNextSiblings(target.getParentIds(),
					target.getWeight());
			// 兄弟节点中包含源节点
			if (siblings.contains(source)) {
				// 1 2 [3 source] 4
				siblings = siblings.subList(0, siblings.indexOf(source) + 1);
				int firstWeight = siblings.get(0).getWeight();
				for (int i = 0; i < siblings.size() - 1; i++) {
					siblings.get(i).setWeight(siblings.get(i + 1).getWeight());
				}
				siblings.get(siblings.size() - 1).setWeight(firstWeight);
			} else {
				// 1 2 3 4 [5 new]
				int nextWeight = nextWeight(target.getParentId());
				int firstWeight = siblings.get(0).getWeight();
				for (int i = 0; i < siblings.size() - 1; i++) {
					siblings.get(i).setWeight(siblings.get(i + 1).getWeight());
				}
				siblings.get(siblings.size() - 1).setWeight(nextWeight);
				source.setWeight(firstWeight);
				updateSelftAndChild(source, target.getParentId(),
						target.getParentIds(), source.getWeight());
			}

			return;
		}
		// 否则作为最后孩子节点
		int nextWeight = nextWeight(target.getId());
		updateSelftAndChild(source, target.getId(),
				target.makeSelfAsNewParentIds(), nextWeight);
	}

	/**
	 * 把源节点全部变更为目标节点
	 *
	 * @param source
	 * @param newParentIds
	 */
	private void updateSelftAndChild(M source, ID newParentId,
			String newParentIds, int newWeight) {
		String oldSourceChildrenParentIds = source.makeSelfAsNewParentIds();
		source.setParentId(newParentId);
		source.setParentIds(newParentIds);
		source.setWeight(newWeight);
		save(source);
		String newSourceChildrenParentIds = source.makeSelfAsNewParentIds();
		repositoryHelper.batchUpdate(UPDATE_CHILDREN_PARENT_IDS_QL,
				newSourceChildrenParentIds, oldSourceChildrenParentIds);
	}

	/**
	 * 查找目标节点及之后的兄弟 注意：值与越小 越排在前边
	 *
	 * @param parentIds
	 * @param currentWeight
	 * @return
	 */
	protected List<M> findSelfAndNextSiblings(String parentIds,
			int currentWeight) {
		return repositoryHelper.<M> findAll(FIND_SELF_AND_NEXT_SIBLINGS_QL,
				parentIds, currentWeight);
	}

	/**
	 * 查看与name模糊匹配的名称
	 *
	 * @param name
	 * @return
	 */
	public Set<String> findNames(Map<String, Object> searchParams, String name,
			ID excludeId) {
		M excludeM = findOne(excludeId);
		searchParams.put("LIKE_name", name);

		addExcludeSearchFilter(searchParams, excludeM);

		return Sets.newHashSet(Lists.transform(findAll(searchParams),
				new Function<M, String>() {
					@Override
					public String apply(M input) {
						return input.getName();
					}
				}));
	}

	/**
	 * 查询子子孙孙
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<M> findChildren(List<M> parents,
			Map<String, Object> searchParams) {
		if (parents.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		searchParams.put("LL_parentIds", parents.get(0)
				.makeSelfAsNewParentIds());
		Specification<M> spec = DynamicSpecifications
				.bySearchFilter(SearchFilter.parse(searchParams).values());
		Specification<M>[] others = new Specification[parents.size() - 1];
		for (int i = 1; i < parents.size(); i++) {
			Map<String, Object> paramOther = Maps.newHashMap();
			paramOther.put("LL_parentIds", parents.get(i)
					.makeSelfAsNewParentIds());
			others[i - 1] = DynamicSpecifications.bySearchFilter(SearchFilter
					.parse(paramOther).values());
		}
		Specification<M> specResult = DynamicSpecifications.bySpecification(
				BooleanOperator.OR, spec, others);

		List<M> children = findAll(specResult);
		return children;
	}

	public List<M> findAllByName(Map<String, Object> searchParams, M excludeM) {
		addExcludeSearchFilter(searchParams, excludeM);
		return findAll(searchParams);
	}

	/**
	 * 查找根和一级节点
	 *
	 * @param searchable
	 * @return
	 */
	public List<M> findRootAndChild(Map<String, Object> searchParams) {
		searchParams.put("EQ_parentId", "0");
		List<M> models = findAll(searchParams);

		if (models.size() == 0) {
			return models;
		}
		List<ID> ids = Lists.newArrayList();
		for (int i = 0; i < models.size(); i++) {
			ids.add(models.get(i).getId());
		}
		searchParams.remove("EQ_parentId");
		searchParams.put("IN_parentId", ids);

		models.addAll(findAll(searchParams));
		return models;
	}

	public Set<ID> findAncestorIds(Iterable<ID> currentIds) {
		Set<ID> parents = Sets.newHashSet();
		for (ID currentId : currentIds) {
			parents.addAll(findAncestorIds(currentId));
		}
		return parents;
	}

	@SuppressWarnings("unchecked")
	public Set<ID> findAncestorIds(ID currentId) {
		Set<ID> ids = Sets.newHashSet();
		M m = findOne(currentId);
		if (m == null) {
			return ids;
		}
		for (String idStr : StringUtils.tokenizeToStringArray(m.getParentIds(),
				"/")) {
			if (!StringUtils.isEmpty(idStr)) {
				ids.add((ID) (idStr));
			}
		}
		return ids;
	}

	/**
	 * 递归查询祖先
	 *
	 * @param parentIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<M> findAncestor(String parentIds) {
		if (StringUtils.isEmpty(parentIds)) {
			return Collections.EMPTY_LIST;
		}
		String[] ids = StringUtils.tokenizeToStringArray(parentIds, "/");
		Map<String, Object> searchParams = Maps.newHashMap();
		searchParams.put("IN_id", ids);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<M> specification = DynamicSpecifications
				.bySearchFilter(filters.values());
		return Lists.reverse(findAll(specification));
	}

	public void addExcludeSearchFilter(Map<String, Object> searchParams,
			M excludeM) {
		if (excludeM == null) {
			return;
		}
		searchParams.put("NE_id", excludeM.getId());
		searchParams.put("RNL_parentIds", excludeM.makeSelfAsNewParentIds());
	}
}