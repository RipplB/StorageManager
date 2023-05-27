package hu.bme.mit.alf.manuel.entityservice.stock.movement;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
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
