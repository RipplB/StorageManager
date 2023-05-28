package hu.bme.mit.alf.manuel.strgman.user;

import hu.bme.mit.alf.manuel.entityservice.users.Role;
import hu.bme.mit.alf.manuel.entityservice.users.UserService;
import hu.bme.mit.alf.manuel.strgman.GenericDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/${endpoints.user}")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserDtoMapper userMapper;

	@PostMapping
	@Secured("MANAGER")
	public ResponseEntity<GenericDto<String>> registerUser(@RequestBody @Valid UserDto userDto) {
		List<String> roleNameList = userDto.getRoleNames();
		List<Role> roles = userService.roleNameListToRoles(roleNameList);
		if (roles.size() != roleNameList.size()) {
			String errorMsg = String.format("Invalid role list %s", String.join(", ", roleNameList));
			log.error(errorMsg);
			return ResponseEntity.badRequest().body(new GenericDto<>(errorMsg));
		}
		return ResponseEntity.ok(new GenericDto<>(userService.createUser(userMapper.of(userDto, roles))));
	}



}
