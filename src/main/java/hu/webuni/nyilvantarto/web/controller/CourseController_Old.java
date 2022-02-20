package hu.webuni.nyilvantarto.web.controller;

import com.querydsl.core.types.Predicate;
import hu.webuni.nyilvantarto.mapper.CourseMapper;
import hu.webuni.nyilvantarto.model.Course;
import hu.webuni.nyilvantarto.model.CourseDTO;
import hu.webuni.nyilvantarto.model.HistoryData;
import hu.webuni.nyilvantarto.repository.CourseRepository;
import hu.webuni.nyilvantarto.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequestMapping("/api/course")
public class CourseController_Old {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseRepository courseRepository;

    @PostMapping("/findCourse")
    public List<CourseDTO> findCourse(@RequestBody CourseDTO input) {
        return courseMapper.toCourseDTOListBasic(courseService.findCourses(courseMapper.toCourse(input)));
    }

    @PostMapping("/findCourse2")
    public List<CourseDTO> findCourse2(@QuerydslPredicate(root = Course.class) Predicate predicate, @RequestParam Optional<Boolean> full, @SortDefault("name") Pageable pageable) {
        boolean isFull = full.orElse(false);
        List<Course> courseList = isFull ?
                courseService.findCoursesWithStudentANDTeachers(predicate, pageable.getSort()) :
                courseRepository.findAll(predicate, pageable).getContent();

        return isFull ? courseMapper.toCourseDTOListFull(courseList) : courseMapper.toCourseDTOListBasic(courseList);
    }

    @GetMapping("/findCourse/{id}")
    public CourseDTO findCourseById(@PathVariable("id") Long id) {
        return courseMapper.toCourseDTO(courseService.findCourseById(id).orElse(new Course()));
    }

    @PostMapping("/add")
    public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
        return courseMapper.toCourseDTO(courseService.insert(courseMapper.toCourse(courseDTO)));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(id);
        System.out.println("Operation delete successfully done");
    }

    @GetMapping("/findCourseHistory/{id}")
    public List<HistoryData<CourseDTO>> findCourseHistoryById(@PathVariable("id") Long id) {

        List<HistoryData<Course>> resultService = courseService.getHistoryOfCourse(id);
        List<HistoryData<CourseDTO>> result = new ArrayList<>();

        resultService.stream().forEach(item -> {
            result.add(new HistoryData<>(
                    courseMapper.toCourseDTOFull(item.getEntity()),
                    item.getRevisionType(),
                    item.getRevision(),
                    item.getDate()
            ));
        });

        return result;
    }

    @GetMapping("/findCourseHistoryByTime/{id}/entity/{time}")
    public CourseDTO findCourseEntityInGivenTimeFrame(@PathVariable("id") Long id,@PathVariable("time") LocalDateTime timeOfEntity) {

        Optional<Course> resultOptional = courseService.getHistoryOfCourseAtDate(timeOfEntity,id);
        if (!resultOptional.isEmpty())
            return courseMapper.toCourseDTOFull(resultOptional.get());
        return null;
    }


}
