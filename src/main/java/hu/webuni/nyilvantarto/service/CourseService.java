package hu.webuni.nyilvantarto.service;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Predicate;
import hu.webuni.nyilvantarto.model.Course;
import hu.webuni.nyilvantarto.model.QCourse;
import hu.webuni.nyilvantarto.repository.CourseRepository;
import hu.webuni.nyilvantarto.repository.predicate.CoursePredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public List<Course> findCourses(Course input) {
        CoursePredicate coursePredicate = new CoursePredicate(input);
        return ImmutableList.copyOf(courseRepository.findAll(coursePredicate.toPredicate()));
    }

    @Transactional
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
}
