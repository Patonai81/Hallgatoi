package hu.webuni.nyilvantarto.service;

import hu.webuni.nyilvantarto.aspect.FaultRetry;
import hu.webuni.nyilvantarto.model.Student;
import hu.webuni.nyilvantarto.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UpdateJobService implements Runnable {


    @Autowired
    StudentRepository studentRepository;

    @Autowired
    RemainingFreeSemesterService remainingFreeSemesterService;


    @Override
   // @FaultRetry
    public void run() {
        studentRepository.findAll().forEach(student -> {
            updateStudent(student);
        });

        System.out.println("Updates were successfull");

    }


    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateStudent(Student student) {
        student.setUsed_free_semesters(getRemainingSemester(student.getCentral_id()));
        studentRepository.save(student);

    }

    @Transactional
    public int getRemainingSemester(Integer id){
        vvv();
        return remainingFreeSemesterService.getRemainingSemester(id);

    }

    public int vvv(){
        System.out.println("VVV called");
        return 0;
    }


}
