package hu.bme.mit.alf.manuel.strgman.product;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProductDto {

	@Length(max = 20)
	private String name;

	@Length(max = 1000)
	private String description;

	@Length(max = 10)
	private String unit;

}
