package hu.bme.mit.alf.manuel.entityservice.stock.movement;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class MovementDto {
	@Positive
	private Integer product;
	@Positive
	private int amount;
	@Positive
	private Integer sourceLocation;
	@Positive
	private Integer targetLocation;
}
