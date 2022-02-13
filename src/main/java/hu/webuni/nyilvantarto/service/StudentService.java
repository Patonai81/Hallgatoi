package hu.webuni.nyilvantarto.service;

import hu.webuni.nyilvantarto.aspect.FaultRetry;
import hu.webuni.nyilvantarto.config.SchedulerConfig;
import hu.webuni.nyilvantarto.model.Student;
import hu.webuni.nyilvantarto.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Service
@DependsOn("SchedulerConfig")
public class StudentService {

    @Autowired
    RemainingFreeSemesterService remainingFreeSemesterService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TaskScheduler taskScheduler;

    @Autowired
    SchedulerConfig schedulerConfig;

   // @Scheduled(cron = "*/3 * * * * *")
  //  @Transactional
    @PostConstruct
    public void startUpdateRemainingSemestersTimer() {
        CronTrigger cronTrigger = new CronTrigger(schedulerConfig.getSchedule().getCronTiming());
        taskScheduler.schedule(() -> {

            studentRepository.findAll().forEach(student -> {
                updateStudent(student);
            });

            System.out.println("Updates were successfull");
        }, cronTrigger);
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

    @Async
    @FaultRetry
    public int vvv(){
        System.out.println("VVV called");
        return 0;
    }


}
