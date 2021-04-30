package com.yjw.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class BlogApplication {

//    @PostConstruct
//    void started(){
//        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
//        TimeZone.setDefault(timeZone);
//    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(BlogApplication.class, args);
    }

}
