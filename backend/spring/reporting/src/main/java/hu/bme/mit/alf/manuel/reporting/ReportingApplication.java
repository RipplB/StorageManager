package hu.bme.mit.alf.manuel.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("hu.bme.mit.alf.manuel.entityservice")
@EntityScan("hu.bme.mit.alf.manuel.entityservice")
@SpringBootApplication
@ComponentScan("hu.bme.mit.alf.manuel")
public class ReportingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportingApplication.class, args);
	}

}
