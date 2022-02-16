package hu.webuni.nyilvantarto.model;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Audited
@Data
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private LocalDate birthDate;
    private int semester;

    @Column(name = "central_id")
    private int central_id;
    @Column(name = "used_free_semesters")
    private int used_free_semesters;


    @ManyToMany
    @JoinTable(
            name = "course_participate",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @HashCodeExclude
    List<Course> courses;

}
