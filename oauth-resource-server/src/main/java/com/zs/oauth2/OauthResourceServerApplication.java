package com.zs.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author 35536
 */
@EnableResourceServer
@SpringBootApplication
@MapperScan("com.ruizhi.sass.mapper")
public class OauthResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthResourceServerApplication.class, args);
    }

}
