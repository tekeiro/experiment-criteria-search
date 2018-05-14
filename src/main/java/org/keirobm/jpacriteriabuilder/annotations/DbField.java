package org.keirobm.jpacriteriabuilder.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicate what JPA column
 * will be used in JPA query
 * for criteria method where
 * this annotation is applied.
 *
 * Example:
 * <pre>
 *  \@DbField("%l.author")
 * </pre>
 *
 *
 * @author Angel Biedma
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DbField {

    String value();

}
