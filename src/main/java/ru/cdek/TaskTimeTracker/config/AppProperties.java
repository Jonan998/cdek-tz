package ru.cdek.TaskTimeTracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  private Jwt jwt = new Jwt();

  @Getter
  @Setter
  public static class Jwt {
    private String secret;
    private long expirationMillis;
  }
}
