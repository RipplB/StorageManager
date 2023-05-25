package hu.bme.mit.alf.manuel.strgman;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("hu.bme.mit.alf.manuel.entityservice")
@EntityScan("hu.bme.mit.alf.manuel.entityservice")
public class AppConfig {
}
