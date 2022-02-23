package hu.webuni.nyilvantarto.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Teszt {
    public static void main(String[] args) {
        String date = "20220222130334";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime time = LocalDateTime.parse(date,formatter);
        System.out.println(time);

        System.out.println(LocalDateTime.now().minusMinutes(5).isAfter(time));

    }
}
