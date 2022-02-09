package hu.webuni.nyilvantarto.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDTO {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private int semester;



}
