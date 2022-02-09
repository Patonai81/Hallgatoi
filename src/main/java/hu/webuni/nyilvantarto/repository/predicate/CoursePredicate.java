package hu.webuni.nyilvantarto.repository.predicate;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import hu.webuni.nyilvantarto.model.Course;
import hu.webuni.nyilvantarto.model.QCourse;
import hu.webuni.nyilvantarto.model.Teacher;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CoursePredicate {

 private Course course;

 public Predicate toPredicate(){

     List<Predicate> predicateList = new ArrayList<>();
     QCourse qcourse = QCourse.course;

     if (course.getCourse_id() != null && course.getCourse_id()>0){
         predicateList.add(qcourse.course_id.eq(course.getCourse_id()));
     }

     if (StringUtils.hasText(course.getName())){
         predicateList.add(qcourse.name.startsWithIgnoreCase(course.getName()));
     }

     if (course.getTeacherList() != null && !course.getTeacherList().isEmpty()){
         course.getTeacherList().stream().forEach(item -> {
             predicateList.add(qcourse.teacherList.any().name.startsWithIgnoreCase(item.getName()));
         });
        }

     if (course.getStudentList() != null && course.getStudentList().size() == 2) {
         predicateList.add(qcourse.course_id.between(0,course.getCourse_id()));
     }

     return ExpressionUtils.allOf(predicateList);

 }

}
