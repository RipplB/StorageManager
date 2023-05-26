package hu.bme.mit.alf.manuel.entityservice.stock.movement;

import hu.bme.mit.alf.manuel.entityservice.stock.Stock;
import hu.bme.mit.alf.manuel.entityservice.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "stock_movement")
public class StockMovement {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	private Date timestamp;

	private int amount;

	@ManyToOne
	private User employee;

	@ManyToOne
	private Stock stock;

	@Enumerated(EnumType.STRING)
	private Type type;

	public enum Type {
		INBOUND, OUTBOUND, INTERNAL
	}

}