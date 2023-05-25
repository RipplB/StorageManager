package hu.bme.mit.alf.manuel.strgman;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
@EnableJpaRepositories("hu.bme.mit.alf.manuel.entityservice")
@EntityScan("hu.bme.mit.alf.manuel.entityservice")
public class AppConfig {

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/**");
	}

}
