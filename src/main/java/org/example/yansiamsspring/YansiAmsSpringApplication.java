package org.example.yansiamsspring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.yansiamsspring.mapper")
public class YansiAmsSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(YansiAmsSpringApplication.class, args);
    }
}
