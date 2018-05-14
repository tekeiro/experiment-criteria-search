package org.keirobm.jpacriteriabuilder.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate JPA Entity, alias used
 * in JPA Query and optionally
 * a list of default joins.
 *
 *
 * @author Angel Biedma
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DbEntity {

    String value();

    String letter();

    String[] defaultJoins() default {};


}
