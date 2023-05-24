package hu.bme.mit.alf.manuel.entityservice.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "strg_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(nullable = false)
	private String loginName;

	private String password;

	private String fullName;

	@ManyToMany
	private List<Role> roles;

}