package hu.bme.mit.alf.manuel.entityservice.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role implements Serializable {
	@Id
	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.EAGER)
	private List<Role> compositeRoles;

}