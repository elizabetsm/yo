package yo.proj;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Slf4j
@EnableJpaRepositories
public class Application {

    public static void main(String[] args) {
        log.info("Bot on startup");
        SpringApplication.run(Application.class, args);
        log.info("Bot started!!!");
    }

}
