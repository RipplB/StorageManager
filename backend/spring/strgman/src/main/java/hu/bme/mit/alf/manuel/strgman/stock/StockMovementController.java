package hu.bme.mit.alf.manuel.strgman.stock;

import hu.bme.mit.alf.manuel.entityservice.EntityService;
import hu.bme.mit.alf.manuel.entityservice.product.Product;
import hu.bme.mit.alf.manuel.entityservice.stock.Stock;
import hu.bme.mit.alf.manuel.entityservice.stock.location.Location;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.MovementService;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.StockMovement;
import hu.bme.mit.alf.manuel.entityservice.users.User;
import hu.bme.mit.alf.manuel.entityservice.users.UserService;
import hu.bme.mit.alf.manuel.strgman.ValidatorBaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/${endpoints.stocks}")
public class StockMovementController extends ValidatorBaseController {

	private final EntityService entityService;
	private final MovementService movementService;
	private final UserService userService;

	@PostMapping("/receive")
	public ResponseEntity<String> receiveStock(@Valid @RequestBody MovementDto movementDto, Principal principal) {
		Optional<Product> productOptional = entityService.getProduct(movementDto.getProduct());
		Optional<Location> locationOptional = entityService.getLocation(movementDto.getLocation());
		if (productOptional.isEmpty()) {
			String msg = String.format("Product with id %d does not exist", movementDto.getProduct());
			log.error(msg);
			return ResponseEntity.badRequest().body(msg);
		}
		if (locationOptional.isEmpty()) {
			String msg = String.format("Location with id %d does not exist", movementDto.getLocation());
			log.error(msg);
			return ResponseEntity.badRequest().body(msg);
		}
		Stock stockDto = new Stock();
		stockDto.setLocation(locationOptional.get());
		stockDto.setProduct(productOptional.get());
		User employee = userService.getByUsername(principal.getName()).orElseThrow();
		try {
			return ResponseEntity.ok(String.valueOf(movementService.receiveStock(stockDto, employee, StockMovement.Type.INBOUND, movementDto.getAmount())));
		} catch (Exception e) {
			log.error("Failed saving stock receive", e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
