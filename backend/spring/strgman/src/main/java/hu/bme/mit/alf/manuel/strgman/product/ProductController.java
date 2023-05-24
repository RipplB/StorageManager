package hu.bme.mit.alf.manuel.strgman.product;

import hu.bme.mit.alf.manuel.entityservice.EntityService;
import hu.bme.mit.alf.manuel.entityservice.product.Product;
import hu.bme.mit.alf.manuel.strgman.ValidatorBaseController;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/${endpoints.product}")
@AllArgsConstructor
public class ProductController extends ValidatorBaseController {

	private final EntityService entityService;

	@Value("${endpoints.product}")
	private String endpoint;

	@GetMapping
	public List<Product> getAllProducts() {
		return entityService.getAllProducts();
	}

	@GetMapping("/{id}")
	public Product getProduct(@PathVariable Integer id) {
		return entityService.getProduct(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<Integer> createProduct(@RequestBody @Valid ProductDTO productDTO) {
		Integer id = entityService.saveProduct(ProductDTO.convertToProduct(productDTO));
		return ResponseEntity.created(URI.create(String.format("%s/%d", endpoint, id))).body(id);
	}

	@PutMapping("/{id}")
	public void updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductDTO productDTO) {
		entityService.getProduct(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Product product = ProductDTO.convertToProduct(productDTO);
		product.setId(id);
		entityService.saveProduct(product);
	}

}
