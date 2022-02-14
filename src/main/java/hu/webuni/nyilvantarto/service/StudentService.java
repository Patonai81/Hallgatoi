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

    @Autowired
    UpdateJobService updateJobService;

   // @Scheduled(cron = "*/3 * * * * *")
  //  @Transactional
    @PostConstruct
    public void startUpdateRemainingSemestersTimer() {
        CronTrigger cronTrigger = new CronTrigger(schedulerConfig.getSchedule().getCronTiming());
        taskScheduler.schedule(updateJobService, cronTrigger);
    }



}
