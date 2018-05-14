package org.keirobm.usage_jpacriteriabuilder;

import org.keirobm.jpacriteriabuilder.annotations.DbEntity;
import org.keirobm.jpacriteriabuilder.annotations.DbField;
import org.keirobm.jpacriteriabuilder.annotations.DbPropertyPath;
import org.keirobm.jpacriteriabuilder.criteriaset.ICriteriaSet;

import java.util.Date;
import java.util.List;


@DbEntity(value = "Book", letter = "b", defaultJoins = {"JOIN b.author a", "LEFT JOIN b.genre g"})
public interface SampleBooksCriteria extends ICriteriaSet {


    @DbField("%l.terror")
    SampleBooksCriteria isTerrorTrue();

    @DbField("%l.comedy")
    SampleBooksCriteria isComedyFalse();

    @DbField("%l.readAt")
    SampleBooksCriteria isReadAtNull();

    @DbField("%l.readAt")
    SampleBooksCriteria isReadAtNotNull();

    @DbField("%l.score")
    SampleBooksCriteria withScoreGt(int score);

    @DbField("%l.readAt")
    SampleBooksCriteria withReadAtLt(Date readAt);

    @DbField("%l.price")
    SampleBooksCriteria withPriceInRange(float min, float max);

    @DbField("%l.author.id")
    @DbPropertyPath(value="id", type=Integer.class)
    SampleBooksCriteria withAuthorEq(Author author);

    @DbField("%l.author.id")
    @DbPropertyPath(value="id", type=Integer.class)
    SampleBooksCriteria withAuthorsIn(List<Author> authors);


    @DbField("%l.author.editorial.id")
    @DbPropertyPath(value="editorial.id", type=Integer.class)
    SampleBooksCriteria withEditorialNotIn(Author... authors);


    @DbField("%l.title")
    SampleBooksCriteria withTitleStartsWith(String titlePart);

    @DbField("%l.title")
    SampleBooksCriteria withTitleEndsWith(String titlePart);

    @DbField("%l.title")
    SampleBooksCriteria withTitleContaining(String titlePart);

    @DbField("%l.publishedAt")
    SampleBooksCriteria withPublishedAtDatesBetween(Date startDate, Date endDate);
}
