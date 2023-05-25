package hu.bme.mit.alf.manuel.strgman.security;

import hu.bme.mit.alf.manuel.entityservice.users.Role;
import hu.bme.mit.alf.manuel.entityservice.users.User;
import hu.bme.mit.alf.manuel.entityservice.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StorageUserDetails implements UserDetails {

	private final User nativeUser;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return nativeUser.getRoles()
				.stream()
				.flatMap(UserService::unfoldCompositeRoles)
				.map(Role::getName)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return nativeUser.getPassword();
	}

	@Override
	public String getUsername() {
		return nativeUser.getLoginName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
