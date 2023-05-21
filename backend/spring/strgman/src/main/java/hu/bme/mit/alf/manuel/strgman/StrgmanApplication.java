package hu.bme.mit.alf.manuel.strgman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "hu.bme.mit.alf.manuel")
public class StrgmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrgmanApplication.class, args);
	}

}
