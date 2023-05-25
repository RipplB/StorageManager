package hu.bme.mit.alf.manuel.strgman.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

	private final StorageUserDetailsService userDetailsService;
	private final JwtHandling jwtHandling;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			log.error("No authorization set while trying to access secured endpoint {}", request.getContextPath());
			filterChain.doFilter(request, response);
			return;
		}
		String[] authHeaderSplit = authHeader.split(" ");
		if (authHeaderSplit.length != 2) {
			log.error("Invalid authorization header '{}' while trying to access secured endpoint {}", authHeader, request.getContextPath());
			filterChain.doFilter(request, response);
			return;
		}
		if (!authHeaderSplit[0].equals("Bearer")) {
			log.error("Invalid authorization type {} while trying to access secured endpoint {}", authHeaderSplit[0], request.getContextPath());
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeaderSplit[1];
		if (!jwtHandling.validateJwtToken(token)) {
			log.warn("Invalid jwt token {} while trying to access secured endpoint {}", token, request.getContextPath());
			filterChain.doFilter(request, response);
			return;
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtHandling.getUserNameFromJwtToken(token));
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails,
						null,
						userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}
}
