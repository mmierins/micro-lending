package mmierins.microlending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
    "mmierins.microlending.condition",
    "mmierins.microlending.service.impl",
    "mmierins.microlending.domain",
    "mmierins.microlending.repository"
})
public class MicroLendingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroLendingApplication.class, args);
    }

}
