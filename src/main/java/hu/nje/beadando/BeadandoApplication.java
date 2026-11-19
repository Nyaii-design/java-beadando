package hu.nje.beadando;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeadandoApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeadandoApplication.class, args);
        System.out.println("Az alkalmazás fut: http://localhost:8080");
    }
}
