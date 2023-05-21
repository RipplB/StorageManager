package hu.bme.mit.alf.manuel.entityservice.stock.movement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "movement_type")
public class MovementType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private Type name;

	public enum Type {
		INBOUND, OUTBOUND
	}

}