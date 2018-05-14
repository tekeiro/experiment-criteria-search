package org.keirobm.usage_jpacriteriabuilder;

import org.keirobm.jpacriteriabuilder.JPAQueryCriteriaFactory;
import org.keirobm.jpacriteriabuilder.jpacriteria.JPAQueryCriteriaOperations;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class UsageSampleBooks {


    private static Date nowPlusDays(int days) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }


    public static void main(String[] args) {
        // Prepare data
        Editorial e1 = new Editorial(1, "Editorial 1");
        Editorial e2 = new Editorial(2, "Editorial 2");

        Author a1 = new Author(1, "Paco Lopez", e1);
        Author a2 = new Author(2, "Maria Lopez", e1);
        Author a3 = new Author(3, "Lucia Sanchez", e2);
        Author a4 = new Author(4, "Teresa Fuelles", e2);


        // Create criteria handler
        final Object criteriaBuilder = JPAQueryCriteriaFactory
                .createJPACriteriaWithOneQuery(SampleBooksCriteria.class);


        // Append criteria
        //   You must cast object returns from Factory to your custom Criteria interface
        ((SampleBooksCriteria) criteriaBuilder)
                .isComedyFalse()
                .isReadAtNotNull()
                .withScoreGt(150)
                .withAuthorsIn(Arrays.asList(a1, a4))
                .withEditorialNotIn(a2)
                .withPriceInRange(30.5f, 70.5f)
                .withTitleEndsWith(" of everything")
                .withPublishedAtDatesBetween(nowPlusDays(-5), nowPlusDays(2))
                .withTitleContaining("History of")
                .setLimit(60)
                .setOrderBy("%l.score ASC, %l.price DESC")
                .addJoins("INNER JOIN %l.editorial e")
                .setGrouping("%l.title", "%l.author.name")
        ;


        // Ask to criteria builder to build JPA Query
        final JPAQueryCriteriaOperations operations = (JPAQueryCriteriaOperations) criteriaBuilder;
        final String queryStr = operations.buildQuery();
        System.out.println("Query string: " + queryStr);
    }

}
