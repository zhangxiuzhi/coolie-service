package zjtech;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Instead using @SpringBootApplication, add @ComponentScan and @EnableAutoConfiguration to scan and load components
 * from com and zjtech packages
 */
@ComponentScan(value = {"com.xpand.starter.cannal", "zjtech"})
@EnableAutoConfiguration
@EnableScheduling
@EnableCanalClient
@EnableMongoRepositories
public class CoolieServiceApplication {

  public static void main(String args[]) {
    SpringApplication.run(CoolieServiceApplication.class, args);
  }
}
