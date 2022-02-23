package hu.webuni.nyilvantarto.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Data
@Configuration("SchedulerConfig")
@EnableScheduling
@ConfigurationProperties(prefix = "hallgato")
@EnableAsync
public class SchedulerConfig {

    Schedule schedule = new Schedule();

    @Data
    public static class Schedule {
        public String cronTiming = new String();
    }
}