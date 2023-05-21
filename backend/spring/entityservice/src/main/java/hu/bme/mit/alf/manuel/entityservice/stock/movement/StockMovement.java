package hu.bme.mit.alf.manuel.entityservice.stock.movement;

import hu.bme.mit.alf.manuel.entityservice.stock.Stock;
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

	@ManyToOne
	private MovementType type;

	private Date timestamp;

	private int amount;

	private String employee;

	@ManyToOne
	private Stock stock;

}