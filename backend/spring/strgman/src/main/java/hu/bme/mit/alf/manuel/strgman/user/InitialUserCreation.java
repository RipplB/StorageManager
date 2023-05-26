package hu.bme.mit.alf.manuel.strgman.user;

import hu.bme.mit.alf.manuel.entityservice.users.User;
import hu.bme.mit.alf.manuel.entityservice.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialUserCreation {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Value("${admin.username}")
	private String username;
	@Value("${admin.password}")
	private String password;

	@EventListener(ApplicationReadyEvent.class)
	public void createAdminUser() {
		if (userService.numberOfUsers() != 0)
			return;
		log.info("No users present in database. Creating initial admin user with name {} and password {}", username, password);
		User admin = new User();
		admin.setLoginName(username);
		admin.setPassword(passwordEncoder.encode(password));
		admin.setFullName("Initial admin user");
		admin.setRoles(userService.roleNameListToRoles(List.of("MANAGER")));
		userService.createUser(admin);
	}

}
