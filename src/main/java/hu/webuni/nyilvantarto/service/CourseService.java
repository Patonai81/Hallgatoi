package hu.webuni.nyilvantarto.service;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Predicate;
import hu.webuni.nyilvantarto.aspect.FaultRetry;
import hu.webuni.nyilvantarto.model.*;
import hu.webuni.nyilvantarto.repository.CourseRepository;
import hu.webuni.nyilvantarto.repository.predicate.CoursePredicate;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.history.Revision;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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

        List result = getAuditReader().createQuery().forRevisionsOfEntity(Course.class,false,true)
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
    public Optional <Course> getHistoryOfCourseAtDate(LocalDateTime timeOfEntity, Long entityID) {

        AuditQuery query = getAuditReader().createQuery()
                .forRevisionsOfEntity( Course.class, true, false )
                .add( AuditEntity.revisionProperty( "timestamp" ).le(timeOfEntity.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .add(AuditEntity.property("course_id").eq(entityID))
                .addOrder(AuditEntity.revisionProperty( "timestamp" ).desc());

        List<Course> result = query.getResultList();
        if (result != null) {
            Course tmp = result.get(0);
            tmp.getStudentList().size();
            tmp.getTeacherList().size();
            return Optional.of(tmp);
        }

        return Optional.empty();
    }

    private AuditReader getAuditReader() {
        return AuditReaderFactory.get(entityManager);
    }



    /*
    @Transactional
    @Async
    public CompletableFuture<List<CourseAVGDTO>> getAVGSemesterOfCOurses(){
        System.out.println(Thread.currentThread().getName());
        return CompletableFuture.completedFuture(courseRepository.findAVGSemesterByCourse());
    }
*/

    @Transactional

    public List<CourseAVGDTO> getAVGSemesterOfCOurses(){
        System.out.println(Thread.currentThread().getName());
        return courseRepository.findAVGSemesterByCourse();
    }
}
