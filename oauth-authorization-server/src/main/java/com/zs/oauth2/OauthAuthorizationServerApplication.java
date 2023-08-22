package com.zs.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 35536
 */
@SpringBootApplication
@MapperScan("com.zs.oauth2.mapper")
public class OauthAuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthAuthorizationServerApplication.class, args);
    }

}
