package cn.flame.oms.common.repository.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class DynamicSpecifications {
	public static <T> Specification<T> bySearchFilter(
			final Collection<SearchFilter> filters) {
		return new Specification<T>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				if (filters != null && !filters.isEmpty()) {
					List<Predicate> predicates = new ArrayList<Predicate>();
					for (SearchFilter filter : filters) {
						String[] names = StringUtils.split(
								filter.getFieldName(), ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}
						switch (filter.getOperator()) {
						case EQ:
							predicates.add(builder.equal(expression,
									filter.getValue()));
							break;
						case NE:
							predicates.add(builder.notEqual(expression,
									filter.getValue()));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%"
									+ filter.getValue() + "%"));
							break;
						case LL:
							predicates.add(builder.like(expression, "%"
									+ filter.getValue()));
							break;
						case RL:
							predicates.add(builder.like(expression,
									filter.getValue() + "%"));
							break;
						case NL:
							predicates.add(builder.notLike(expression, "%"
									+ filter.getValue() + "%"));
							break;
						case LNL:
							predicates.add(builder.notLike(expression,
									filter.getValue() + "%"));
							break;
						case RNL:
							predicates.add(builder.notLike(expression, "%"
									+ filter.getValue()));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression,
									(Comparable) filter.getValue()));
							break;
						case LT:
							predicates.add(builder.lessThan(expression,
									(Comparable) filter.getValue()));
							break;
						case GTE:
							predicates
									.add(builder.greaterThanOrEqualTo(
											expression,
											(Comparable) filter.getValue()));
							break;
						case LTE:
							predicates
									.add(builder.lessThanOrEqualTo(expression,
											(Comparable) filter.getValue()));
							break;
						case IN:
							predicates.add(expression.in(filter.getValue()));
							break;
						}
					}

					if (predicates.size() > 0) {
						return builder.and(predicates
								.toArray(new Predicate[predicates.size()]));
					}
				}

				return builder.conjunction();
			}
		};
	}

	public static <T> Specification<T> bySpecification(
			final Predicate.BooleanOperator operator,
			final Specification<T> specification,
			@SuppressWarnings("unchecked") final Specification<T>... others) {
		Specifications<T> specs = Specifications.where(specification);
		for (Specification<T> spec : others) {
			if (BooleanOperator.OR.equals(operator)) {
				specs = specs.or(spec);
			} else {
				specs = specs.and(spec);
			}
		}
		return specs;
	}
}