package org.keirobm.jpacriteriabuilder.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate how to extract a property
 * from an argument when argument is
 * a complex object and you need a property
 * from an object.
 *
 * @author Angel Biedma
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DbPropertyPath {

    String value();

    Class<?> type();

}
