package com;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCanalClient
@EnableMongoRepositories("com.zjtech.coolie.document")
public class CoolieServiceApplication {

  public static void main(String args[]) {
    SpringApplication.run(CoolieServiceApplication.class, args);
  }
}
