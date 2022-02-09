package hu.webuni.nyilvantarto.repository;

import hu.webuni.nyilvantarto.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;


public interface StudentRepository extends JpaRepository<Student, Long> {

    @QueryHints(value = { @QueryHint(name = HINT_CACHEABLE, value = "true")})
    @Transactional
    @Query("select s from Student s where s.id= :id")
    public Student findStudentByOwnId(Long id);

}
