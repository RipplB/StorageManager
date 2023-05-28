package hu.bme.mit.alf.manuel.strgman.security;

import hu.bme.mit.alf.manuel.entityservice.users.Role;
import hu.bme.mit.alf.manuel.entityservice.users.User;
import hu.bme.mit.alf.manuel.entityservice.users.UserService;
import hu.bme.mit.alf.manuel.strgman.GenericDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/${endpoints.secure}")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtHandling jwtHandling;
	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<GenericDto<String>> login(@RequestParam String username, @RequestParam String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		StorageUserDetails userDetails = (StorageUserDetails) authentication.getPrincipal();
		String token = jwtHandling.generateTokenFromUsernameAndRoles(username, userDetails.getAuthorities().stream().map(Objects::toString).collect(Collectors.toSet()));
		return ResponseEntity.ok(new GenericDto<>(token));
	}

	@PostMapping("/refresh")
	public ResponseEntity<GenericDto<String>> refresh(Principal principal) {
		String username = principal.getName();
		User user = userService.getByUsername(username).orElseThrow();
		String token = jwtHandling.generateTokenFromUsernameAndRoles(username, user.getRoles().stream().flatMap(UserService::unfoldCompositeRoles).map(Role::getName).collect(Collectors.toSet()));
		return ResponseEntity.ok(new GenericDto<>(token));
	}

}
