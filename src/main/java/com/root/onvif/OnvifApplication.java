package com.root.onvif;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.root.onvif.mbg.mapper")//扫描注册数据表实体类
@SpringBootApplication
public class OnvifApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnvifApplication.class, args);
	}

}
