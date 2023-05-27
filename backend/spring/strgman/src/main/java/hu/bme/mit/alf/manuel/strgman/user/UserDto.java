package hu.bme.mit.alf.manuel.strgman.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link hu.bme.mit.alf.manuel.entityservice.users.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserDto implements Serializable {
	@Size(max = 255)
	@NotBlank
	private String loginName;
	@Size(min = 6, max = 64)
	@NotBlank
	private String password;
	@Size(max = 255)
	private String fullName;
	private List<String> roleNames;
	@Email
	private String email;
}