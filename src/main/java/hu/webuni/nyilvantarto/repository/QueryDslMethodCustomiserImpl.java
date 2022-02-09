package hu.webuni.nyilvantarto.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import hu.webuni.nyilvantarto.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.persistence.EntityManager;
import java.util.List;

public class QueryDslMethodCustomiserImpl extends SimpleJpaRepository<Course, Long>
        implements QueryDslMethodCustomiser {

    private final EntityManager entityManager;
    private final EntityPath<Course> path;
    private final PathBuilder<Course> builder;
    private final Querydsl querydsl;

    @Autowired
    public QueryDslMethodCustomiserImpl(EntityManager entityManager) {
        super(Course.class, entityManager);
        this.entityManager = entityManager;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Course.class);
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }


    @Override
    public List findAllWithEntity(Predicate predicate, String entitygraph, Sort sort) {
        JPAQuery query = createQuery(predicate);
        querydsl.applySorting(sort,query);
        query.setHint(EntityGraph.EntityGraphType.LOAD.getKey(), entityManager.getEntityGraph(entitygraph));
        return query.fetch();
    }


    @Override
    public List findAllWithEntity(Predicate predicate, String entitygraph) {
        return  findAllWithEntity(predicate, entitygraph,Sort.unsorted());
    }

    @Override
    public Page findAllWithEntityANDPaging(Predicate predicate, String entitygraph, Pageable pageable) {

   /*
        JPAQuery countQuery = createQuery(predicate);
        JPAQuery query = (JPAQuery) querydsl.applyPagination(pageable, createQuery(predicate));

        query.setHint(EntityGraph.EntityGraphType.LOAD.getKey(),
                entityManager.getEntityGraph(entitygraph));

        Long total = countQuery.();
        List<T> content = total > pageable.getOffset() ? query.list(path) :
                Collections.<T> emptyList();

        return new PageImpl<>(content, pageable, total);

    */
        return null;
    }


    private JPAQuery createQuery(Predicate predicate) {
        return querydsl.createQuery(path).where(predicate);
    }


}
