package hu.bme.mit.alf.manuel.strgman.user;

import hu.bme.mit.alf.manuel.entityservice.users.Role;
import hu.bme.mit.alf.manuel.entityservice.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDtoMapper {

	private final PasswordEncoder passwordEncoder;

	public User of(UserDto dto, List<Role> roles) {
		User user = new User();
		user.setLoginName(dto.getLoginName());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setFullName(dto.getFullName());
		user.setEmail(dto.getEmail());
		user.setRoles(roles);
		return user;
	}
}
