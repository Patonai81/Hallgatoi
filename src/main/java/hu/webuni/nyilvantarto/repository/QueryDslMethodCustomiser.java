package hu.webuni.nyilvantarto.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface  QueryDslMethodCustomiser<T> {

    List findAllWithEntity(Predicate predicate, String entitygraph, Sort sort);

    List<T> findAllWithEntity(Predicate predicate, String entitygraph);

    Page<T> findAllWithEntityANDPaging(Predicate predicate, String entitygraph, Pageable pageable);

}
