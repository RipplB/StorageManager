package hu.bme.mit.alf.manuel.entityservice.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findUserByLoginName(String loginName);
	List<User> findUsersByRoles_Name(String name);
}