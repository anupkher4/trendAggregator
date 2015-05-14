package Twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class TrendsApplication {

    public static void main(String[] args) {
    	
    	SpringApplication.run(TrendsApplication.class, args);
    }
}
