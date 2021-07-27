package ro.fortech.allocation;


import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ro.fortech.allocation")
@EnableJpaRepositories(basePackages = "ro.fortech.allocation")
@EntityScan(basePackages = "ro.fortech.allocation")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}