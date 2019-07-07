package com.coolron.muxin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Auther: xf
 * @Date: 2019/1/5 18:09
 * @Description: 短信服务
 */
@ComponentScan(basePackages = {"com.coolron.muxin"})
@MapperScan("com.coolron.muxin.dao")
@SpringBootApplication
public class MuXinApplication {
    public static void main(String[] args) {
        SpringApplication.run(MuXinApplication.class);
    }
}
