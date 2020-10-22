package demoDistributedTransaction.bankService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankServiceAPI {
	
	public static void main(String[] args) {
		
		SpringApplication.run(BankServiceAPI.class, args);
		System.out.println("Bank service running, don't be worried");

	}

}
