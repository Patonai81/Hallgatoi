package hu.webuni.nyilvantarto.repository;

import com.querydsl.core.BooleanBuilder;
import hu.webuni.nyilvantarto.model.Course;
import hu.webuni.nyilvantarto.model.QCourse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long>, QuerydslPredicateExecutor<Course>, QuerydslBinderCustomizer<QCourse>,
        QueryDslMethodCustomiser<Course>/*, RevisionRepository<Course,Long,Long>*/ {


    @EntityGraph( attributePaths = {"studentList"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT c from Course c")
    public Set<Course> findCoursesWithStudents();

    @EntityGraph( attributePaths = {"studentList"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT c from Course c")
    public Set<Course> findCoursesWithTeachers();

    @Override
    default void customize(QuerydslBindings bindings, QCourse root) {
        bindings.bind(root.course_id).first((path, value) -> path.eq(value));

        bindings.bind(root.name).first(((path, value) -> {
            return path.startsWithIgnoreCase(value);
        }));

        bindings.bind(root.teacherList.any().name).all((path, values) -> {
            BooleanBuilder predicate = new BooleanBuilder();
            values.forEach( value -> predicate.or(path.startsWithIgnoreCase(value)));
            return Optional.of(predicate);
        });

        bindings.bind(root.studentList.any().id).all((path, values) -> {
            BooleanBuilder predicate = new BooleanBuilder();
            values.forEach( value -> predicate.or(path.eq(value)));
            return Optional.of(predicate);

        });

        /** Azokat a kurzusokat adom visszsa akinek van olyan tanulója, akinek a szemesztere a megadott szemeszterpár
        közé esik
       */

        bindings.bind(root.studentList.any().semester).all((path, values) -> {

            if (values ==  null || values.size() != 2)
                return Optional.empty();

            Iterator<? extends Integer> iter = values.iterator();
            Integer fromSemesterId = iter.next();
            Integer toSemesterId = iter.next();

          return Optional.of(path.between(fromSemesterId, toSemesterId));

        });

    }



}
