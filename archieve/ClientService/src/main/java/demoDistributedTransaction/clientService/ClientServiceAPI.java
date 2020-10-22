package demoDistributedTransaction.clientService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientServiceAPI {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceAPI.class, args);
		System.out.println("Client service running, don't be worried");

	}

}
