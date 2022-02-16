package hu.webuni.nyilvantarto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Audited
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
    @EqualsAndHashCode.Exclude
    Set<Student> studentList;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    Set<Teacher> teacherList;



}
