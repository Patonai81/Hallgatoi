package hu.webuni.nyilvantarto.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Cacheable
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @ToString.Exclude
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
    List<Course> courses;

}
