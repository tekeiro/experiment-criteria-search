package org.keirobm.jpacriteriabuilder.criteriaset;

/**
 * Base interface for all your
 * custom CriteriaSet interfaces.
 *
 * @author Angel Biedma
 */
public interface ICriteriaSet {

    ICriteriaSet setOrderBy(String... orderBy);

    ICriteriaSet setLimit(int maxResults);

    ICriteriaSet setFirstResult(int firstResult);

    ICriteriaSet addJoins(String... joins);

    ICriteriaSet setGrouping(String... groupings);


}
