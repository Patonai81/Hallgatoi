package hu.webuni.nyilvantarto.service;


import hu.webuni.nyilvantarto.exception.BackendNotAvailableException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RemainingFreeSemesterService {


    public int getRemainingSemester(int central_studentId){

        if (new Random().nextInt(2) == 0)
            throw new BackendNotAvailableException();

        return new Random().nextInt(10) ;
    }



}
