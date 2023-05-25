package hu.bme.mit.alf.manuel.strgman.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link hu.bme.mit.alf.manuel.entityservice.stock.location.Location}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LocationDto implements Serializable {
	@Size(max = 20)
	@NotBlank
	private String name;
	@Size(max = 255)
	private String description;
}