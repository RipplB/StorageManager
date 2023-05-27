package hu.bme.mit.alf.manuel.strgman.user;

import hu.bme.mit.alf.manuel.entityservice.users.Role;
import hu.bme.mit.alf.manuel.entityservice.users.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialRolesCreation {

	private final RoleRepository roleRepository;

	@EventListener(ApplicationReadyEvent.class)
	public void createAdminUser() {
		if (roleRepository.count() != 0)
			return;
		log.info("No roles present in database. Creating initial roles, MANAGER, STORAGE, OFFICE. MANAGER composes the latter two");
		Role storage = new Role();
		Role office = new Role();
		Role manager = new Role();
		storage.setName("STORAGE");
		office.setName("OFFICE");
		manager.setName("MANAGER");
		manager.setCompositeRoles(List.of(storage, office));
		roleRepository.saveAll(List.of(storage, office, manager));
	}

}
