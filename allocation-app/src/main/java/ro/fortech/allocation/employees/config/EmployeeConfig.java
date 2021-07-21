package ro.fortech.allocation.employees.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application-${spring.profiles.active}.properties")
@Configuration
public class EmployeeConfig {
    @Value("${baseUrl}")
    private String baseUrl;



}
