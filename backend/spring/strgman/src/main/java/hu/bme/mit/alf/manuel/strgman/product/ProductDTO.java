package hu.bme.mit.alf.manuel.strgman.product;

import hu.bme.mit.alf.manuel.entityservice.product.Product;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProductDTO {

	@Length(max = 20)
	private String name;

	@Length(max = 1000)
	private String description;

	@Length(max = 10)
	private String unit;

	public static Product convertToProduct(ProductDTO productDTO) {
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setUnit(productDTO.getUnit());
		product.setDescription(productDTO.getDescription());
		return product;
	}

}
