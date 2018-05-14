package org.keirobm.jpacriteriabuilder.jpacriteria;


/**
 * Operations to create JPA queries
 * based on criterias.
 *
 * @author Angel Biedma
 */
public interface JPAQueryCriteriaOperations {

    String buildQuery();

    String buildQueryCount();

    <T> String buildQueryForCustomObject(Class<T> objectResultantClass, String... selectColumns);

    String buildQueryAsMap(String... selectColumns);

}
