package hu.bme.mit.alf.manuel.entityservice.stock;

import hu.bme.mit.alf.manuel.entityservice.product.Product;
import hu.bme.mit.alf.manuel.entityservice.stock.location.Location;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.StockMovement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "stock")
public class Stock {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	@ManyToOne
	private Product product;

	private int amount;

	@ManyToOne
	private Location location;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "stock_id")
	private List<StockMovement> movements;

}