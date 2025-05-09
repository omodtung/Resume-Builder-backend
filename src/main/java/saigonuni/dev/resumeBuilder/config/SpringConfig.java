package saigonuni.dev.resumeBuilder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAsync
@EnableScheduling
public class SpringConfig {

//   @Scheduled(fixedDelay = 100000)
  public void doSomething() {
    System.out.println("Scheduled task running every 5 seconds");
  }
}
