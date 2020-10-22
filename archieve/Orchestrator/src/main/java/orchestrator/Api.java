package orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Api {
	public static void main(String[] args) {
		SpringApplication.run(Api.class, args);
		System.out.println("Orchestrator running");
	}
}
