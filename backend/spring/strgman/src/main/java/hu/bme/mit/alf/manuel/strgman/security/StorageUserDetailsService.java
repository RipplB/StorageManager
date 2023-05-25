package hu.bme.mit.alf.manuel.strgman.security;

import hu.bme.mit.alf.manuel.entityservice.users.User;
import hu.bme.mit.alf.manuel.entityservice.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StorageUserDetailsService implements UserDetailsService {

	private final UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService
				.getByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		return new StorageUserDetails(user);
	}
}
