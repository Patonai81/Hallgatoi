package hu.webuni.nyilvantarto.web.controller;

import com.querydsl.core.types.Predicate;
import hu.webuni.nyilvantarto.api.CourseControllerApi;
import hu.webuni.nyilvantarto.mapper.CourseMapper;
import hu.webuni.nyilvantarto.model.Course;
import hu.webuni.nyilvantarto.model.CourseDTO;
import hu.webuni.nyilvantarto.model.HistoryData;
import hu.webuni.nyilvantarto.model.HistoryDataCourseDTO;
import hu.webuni.nyilvantarto.repository.CourseRepository;
import hu.webuni.nyilvantarto.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class CourseController implements CourseControllerApi {

    private  final NativeWebRequest nativeWebRequest;
    private final PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;
    private final QuerydslPredicateArgumentResolver querydslPredicateArgumentResolver;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseRepository courseRepository;


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<CourseDTO> addCourse(CourseDTO courseDTO) {
        return ResponseEntity.ok(courseMapper.toCourseDTO(courseService.insert(courseMapper.toCourse(courseDTO))));
    }

    @Override
    public ResponseEntity<Void> deleteCourse(Long id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CourseDTO>> findCourse(CourseDTO courseDTO) {
        return ResponseEntity.ok(courseMapper.toCourseDTOListBasic(courseService.findCourses(courseMapper.toCourse(courseDTO))));
    }

    @Override
    public ResponseEntity<List<CourseDTO>> findCourse2(Boolean full, Integer page, Integer size, String sort, Long id, String name) {

        Pageable pageable = createPageable();
        boolean isFull = full == null ? false : true;
        List<Course> courseList = isFull ?
                courseService.findCoursesWithStudentANDTeachers(createPredicate(), pageable.getSort()) :
                courseRepository.findAll(createPredicate(), pageable).getContent();

        return isFull ? ResponseEntity.ok(courseMapper.toCourseDTOListFull(courseList)) : ResponseEntity.ok(courseMapper.toCourseDTOListBasic(courseList));

    }

    public void configPageable(@SortDefault("name")  Pageable pageable){}

    public void configPredicate(@QuerydslPredicate(root = Course.class) Predicate predicate){}


    private Pageable createPageable() {
        Method method = null;
        try {
            method = this.getClass().getMethod("configPageable", Pageable.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        MethodParameter methodParameter=new MethodParameter(method,0);
        ModelAndViewContainer mavContainer=null;
        WebDataBinderFactory binderFactory=null;


       return  pageableHandlerMethodArgumentResolver.resolveArgument(methodParameter,mavContainer,nativeWebRequest,binderFactory);

    }

    @SneakyThrows
    private Predicate createPredicate() {
        Method method = null;
        try {
            method = this.getClass().getMethod("configPredicate", Predicate.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        MethodParameter methodParameter=new MethodParameter(method,0);
        ModelAndViewContainer mavContainer=null;
        WebDataBinderFactory binderFactory=null;


        return (Predicate) querydslPredicateArgumentResolver.resolveArgument(methodParameter,mavContainer,nativeWebRequest,binderFactory);

    }

    @Override
    public ResponseEntity<CourseDTO> findCourseById(Long id) {
        return ResponseEntity.ok(courseMapper.toCourseDTO(courseService.findCourseById(id).orElse(new Course())));
    }

    @Override
    public ResponseEntity<CourseDTO> findCourseEntityInGivenTimeFrame(Long id, LocalDateTime time) {
        Optional<Course> resultOptional = courseService.getHistoryOfCourseAtDate(time,id);
        if (!resultOptional.isEmpty())
            return ResponseEntity.ok(courseMapper.toCourseDTOFull(resultOptional.get()));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDTO>> findCourseHistoryById(Long id) {

        List<HistoryData<Course>> resultService = courseService.getHistoryOfCourse(id);
        List<HistoryDataCourseDTO> result = new ArrayList<>();

        resultService.stream().forEach(item -> {
            HistoryDataCourseDTO tmpItem = new HistoryDataCourseDTO().entity(
                    courseMapper.toCourseDTOFull(item.getEntity())).revision(
                    item.getRevision()).date(
                    item.getDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime());
            tmpItem.setRevisionType(HistoryDataCourseDTO.RevisionTypeEnum.fromValue(item.getRevisionType().name()));
            result.add(tmpItem);
        });

        return ResponseEntity.ok(result);
    }


}
