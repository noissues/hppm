package com.hppm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hppm.mapper")
public class HppmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HppmServerApplication.class, args);
    }
}
