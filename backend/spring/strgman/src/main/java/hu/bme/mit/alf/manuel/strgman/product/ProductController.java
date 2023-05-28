package hu.bme.mit.alf.manuel.strgman.product;

import hu.bme.mit.alf.manuel.entityservice.EntityService;
import hu.bme.mit.alf.manuel.entityservice.product.Product;
import hu.bme.mit.alf.manuel.strgman.ValidatorBaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/${endpoints.product}")
@RequiredArgsConstructor
public class ProductController extends ValidatorBaseController {

	@Value("${endpoints.product}")
	private String endpoint;

	private final ModelMapper modelMapper = new ModelMapper();

	private final EntityService entityService;

	@GetMapping
	public List<Product> getAllProducts() {
		return entityService.getAllProducts();
	}

	@GetMapping("/{id}")
	public Product getProduct(@PathVariable Integer id) {
		return entityService.getProduct(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	@Secured("OFFICE")
	public ResponseEntity<Integer> createProduct(@RequestBody @Valid ProductDto productDTO) {
		Integer id = entityService.saveProduct(modelMapper.map(productDTO, Product.class));
		log.info("New product added: "+productDTO.getName());
		return ResponseEntity.created(URI.create(String.format("%s/%d", endpoint, id))).body(id);
	}

	@PutMapping("/{id}")
	@Secured("OFFICE")
	public void updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductDto productDTO) {
		if (entityService.getProduct(id).isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		Product product = modelMapper.map(productDTO, Product.class);
		product.setId(id);
		entityService.saveProduct(product);
		log.info("Product updated: " + productDTO.getName());
	}

}
