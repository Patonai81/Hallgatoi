package hu.webuni.nyilvantarto.web.controller.ws;


import hu.webuni.nyilvantarto.service.RemainingFreeSemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/ws")
public class UpdateStudentController {

    @Autowired
    RemainingFreeSemesterService remainingFreeSemesterService;



    @GetMapping("/teszt")
    public void findCourseById() {
        remainingFreeSemesterService.getRemainingSemester(2);
        System.out.println("ok");
    }

}
