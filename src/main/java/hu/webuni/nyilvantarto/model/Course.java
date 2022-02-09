package hu.webuni.nyilvantarto.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NamedEntityGraph(name = "Course.teacher", attributeNodes = @NamedAttributeNode(value = "teacherList"))
@NamedEntityGraph(name = "Course.student", attributeNodes = @NamedAttributeNode(value = "studentList"))
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long course_id;
    private String name;


    @ManyToMany(mappedBy = "courses",fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<Student> studentList;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<Teacher> teacherList;



}
