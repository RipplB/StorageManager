package hu.bme.mit.alf.manuel.entityservice.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
	List<Role> findAllByNameIn(List<String> roleNames);
}