package com.program.moist;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@MapperScan("com.program.moist.dao")
public class MoistApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoistApplication.class, args);
		log.info("项目启动成功");
	}

}
