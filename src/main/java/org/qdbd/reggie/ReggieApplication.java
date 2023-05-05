package org.qdbd.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan(basePackages = {"org.qdbd.reggie.filter"})
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) throws RuntimeException {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功");

    }
}
