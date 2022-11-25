package com.higo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Created by Intellij IDEA.
 *
 * @author ACwin YANG
 * @date 2022/05/20
 */
@SpringBootApplication
@MapperScan(basePackages = "com.higo.model.dao")
@EnableSwagger2
public class MallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }

}
