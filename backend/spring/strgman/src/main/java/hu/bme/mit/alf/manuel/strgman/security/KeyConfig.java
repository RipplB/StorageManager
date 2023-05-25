package hu.bme.mit.alf.manuel.strgman.security;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Configuration
public class KeyConfig {

	@Value("${security.jwtSecret}")
	private String jwtSecret;

	@Bean
	public Key jwtSigningKey() {
		return new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
	}

}
