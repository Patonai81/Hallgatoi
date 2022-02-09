package hu.webuni.nyilvantarto.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TeacherDTO {

    private Long id;
    private String name;
    private LocalDate birthDate;


}
