package org.keirobm.jpacriteriabuilder;

import org.keirobm.jpacriteriabuilder.criteriaset.ICriteriaSet;
import org.keirobm.jpacriteriabuilder.jpacriteria.JPAQueryCriteriaOperations;
import org.keirobm.jpacriteriabuilder.jpaquerybuilder.OneQueryJPACriteriaDynamicProxy;

import java.lang.reflect.Proxy;
import java.util.Objects;

public class JPAQueryCriteriaFactory {

    private JPAQueryCriteriaFactory() {}


    /**
     * Construct a proxy of a Criteria class that can
     * bind criterias and then build a JPA query that
     * return objects that criteria matches.
     *
     * @param customCriteriaInterface Class of an interface that
     *                                extends {@link ICriteriaSet}
     * @return Object that can be casted to custom interface
     * or {@link JPAQueryCriteriaOperations} to ask
     * for build JPA queries.
     */
    public static Object createJPACriteriaWithOneQuery(Class<? extends ICriteriaSet> customCriteriaInterface) {
        Objects.requireNonNull(customCriteriaInterface);
        if (!customCriteriaInterface.isInterface()) {
            throw new IllegalArgumentException(
                    "Custom Interface passed must be an Interface not a concrete or abstract class."
            );
        }

        final OneQueryJPACriteriaDynamicProxy handler = new OneQueryJPACriteriaDynamicProxy(customCriteriaInterface);

        return Proxy.newProxyInstance(
                JPAQueryCriteriaFactory.class.getClassLoader(),
                new Class[] {JPAQueryCriteriaOperations.class, customCriteriaInterface},
                handler
        );
    }

}
