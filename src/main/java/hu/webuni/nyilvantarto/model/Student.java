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


    @ManyToMany
    @JoinTable(
            name = "course_participate",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> courses;

}
