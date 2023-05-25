package hu.bme.mit.alf.manuel.entityservice.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "strg_user")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(nullable = false, unique = true)
	private String loginName;

	private String password;

	private String fullName;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles;

}