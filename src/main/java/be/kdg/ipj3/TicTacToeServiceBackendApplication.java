package be.kdg.ipj3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TicTacToeServiceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeServiceBackendApplication.class, args);
    }

}
