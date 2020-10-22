package user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserAPI {
	
	public static void main(String[] args) {
		SpringApplication.run(UserAPI.class, args);
		System.out.println("User API running");
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		System.out.println("Time: "+df.format(dateobj));
	}
}
