package hu.webuni.nyilvantarto.service;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Predicate;
import hu.webuni.nyilvantarto.aspect.FaultRetry;
import hu.webuni.nyilvantarto.model.Course;
import hu.webuni.nyilvantarto.model.HistoryData;
import hu.webuni.nyilvantarto.model.QCourse;
import hu.webuni.nyilvantarto.model.Student;
import hu.webuni.nyilvantarto.repository.CourseRepository;
import hu.webuni.nyilvantarto.repository.predicate.CoursePredicate;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentService studentService;

    @PersistenceContext
    EntityManager entityManager;

    @FaultRetry
    public List<Course> findCourses(Course input) {
        CoursePredicate coursePredicate = new CoursePredicate(input);
        return ImmutableList.copyOf(courseRepository.findAll(coursePredicate.toPredicate()));
    }

    @Transactional
    @Cacheable("course")
    public List<Course> findCoursesWithStudentANDTeachers(Predicate predicate, Sort sort) {
        List<Course> result = new ArrayList<Course>();
        courseRepository.findAll(predicate).forEach(result::add);
        result = courseRepository.findAllWithEntity(QCourse.course.in(result), "Course.student");
        result = courseRepository.findAllWithEntity(QCourse.course.in(result), "Course.teacher", sort);
        return result;
    }

    public Optional<Course> findCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public Course insert(Course course){
        Course attachedCourse = courseRepository.save(course);
        if (course.getStudentList() != null) {
            course.getStudentList().stream().forEach( item -> {
                Student attachedStudent = studentService.insert(item);
                if (attachedStudent.getCourses()!=null) {
                    attachedStudent.getCourses().add(attachedCourse);
                }else {
                    attachedStudent.setCourses(Arrays.asList(attachedCourse));
                }

            });
        }
        return attachedCourse;
    }

    @Modifying
    @Transactional
    public void delete(Long id){
        courseRepository.deleteById(id);
    }

    @Transactional
    @SuppressWarnings({"rawtypes","unchecked"})
    public List<HistoryData<Course>> getHistoryOfCourse(Long id){

        List result = AuditReaderFactory.get(entityManager).createQuery().forRevisionsOfEntity(Course.class,false,true)
                .add(AuditEntity.property("course_id").eq(id)).getResultList()
                .stream().map( item -> {
                    Object[] itemTmb = (Object[]) item;
                    DefaultRevisionEntity defaultRevisionEntity = (DefaultRevisionEntity) itemTmb[1];
                    Course course = (Course) itemTmb[0];
                    //Betöltés kikényszerítése
                    course.getTeacherList().size();
                    course.getStudentList().size();

                    return new HistoryData<Course>(
                            course,
                            (RevisionType) itemTmb[2],
                            defaultRevisionEntity.getId(),
                            defaultRevisionEntity.getRevisionDate()
                    );
                }).toList();

        return result;
    }

    @Transactional
    @SuppressWarnings({"rawtypes","unchecked"})
    public List<HistoryData<Course>> getHistoryOfCourse2(Long id) {

    //AuditReaderFactory.get(entityManager).createQuery().forEntitiesAtRevision()
return  null;
    }

    }
