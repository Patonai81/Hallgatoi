package hu.webuni.nyilvantarto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity

public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Long id;

    @EqualsAndHashCode.Include
    private String name;
    @EqualsAndHashCode.Exclude
    private LocalDate birthDate;

    @ManyToMany
    @JoinTable(
            name = "course_teaches",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> courses;

}
