package hu.bme.mit.alf.manuel.strgman.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtHandling {

	@Value("${security.jwtExpirationMs}")
	private int jwtExpirationMs;

	private final Key jwtSigningKey;

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(jwtSigningKey).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(jwtSigningKey).build().parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String generateTokenFromUsernameAndRoles(String username, Collection<String> roles) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.claim("roles", roles)
				.signWith(jwtSigningKey, SignatureAlgorithm.HS512)
				.compact();
	}
}
