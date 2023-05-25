package hu.bme.mit.alf.manuel.entityservice.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public static Stream<Role> unfoldCompositeRoles(Role parentRole) {
		Stream<Role> role = Stream.of(parentRole);
		Stream<Role> roles = parentRole.getCompositeRoles().stream().flatMap(UserService::unfoldCompositeRoles);
		return Stream.concat(role, roles);
	}

	public Optional<User> getByUsername(String username) {
		return userRepository.findUserByLoginName(username);
	}

	public List<Role> roleNameListToRoles(List<String> roleNames) {
		return roleRepository.findAllByNameIn(roleNames);
	}

	public String createUser(User user) {
		return userRepository.save(user).getId().toString();
	}

}
