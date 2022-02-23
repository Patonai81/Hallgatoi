package hu.webuni.nyilvantarto.web.controller;

import hu.webuni.nyilvantarto.model.CourseAVGDTO;
import hu.webuni.nyilvantarto.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/async")
public class AsyncController {

    @Autowired
    CourseService courseService;

    @GetMapping("/analytics")
    @Async
    public CompletableFuture<List<CourseAVGDTO>> getApiCourseAnalytics2() {
        System.out.println(Thread.currentThread().getName());
        return CompletableFuture.completedFuture(courseService.getAVGSemesterOfCOurses());
    }


}
