package hu.bme.mit.alf.manuel.strgman.stock;

import hu.bme.mit.alf.manuel.entityservice.stock.movement.MovementDto;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.MovementService;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.exception.MovementException;
import hu.bme.mit.alf.manuel.entityservice.users.User;
import hu.bme.mit.alf.manuel.entityservice.users.UserService;
import hu.bme.mit.alf.manuel.strgman.ValidatorBaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/${endpoints.stocks}")
public class StockMovementController extends ValidatorBaseController {

	private final MovementService movementService;
	private final UserService userService;

	@ExceptionHandler({MovementException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String notFound(MovementException exception) {
		log.error(exception.getMessage());
		return exception.getMessage();
	}

	@PostMapping("/receive")
	public ResponseEntity<String> receiveStock(@Valid @RequestBody MovementDto movementDto, Principal principal) throws MovementException {
		User employee = userService.getByUsername(principal.getName()).orElseThrow();
		try {
			return ResponseEntity.ok(String.valueOf(movementService.receiveStock(movementDto, employee)));
		} catch (MovementException mve) {
			throw mve;
		} catch (Exception e) {
			log.error("Failed saving stock receive", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@PostMapping("/release")
	public ResponseEntity<String> releaseStock(@Valid @RequestBody MovementDto movementDto, Principal principal) throws MovementException {
		User employee = userService.getByUsername(principal.getName()).orElseThrow();
		try {
			return ResponseEntity.ok(String.valueOf(movementService.releaseStock(movementDto, employee)));
		} catch (MovementException mve) {
			throw mve;
		} catch (Exception e) {
			log.error("Failed saving stock release", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@PostMapping("/internal")
	public ResponseEntity<String> moveStockInternally(@Valid @RequestBody MovementDto movementDto, Principal principal) throws MovementException {
		User employee = userService.getByUsername(principal.getName()).orElseThrow();
		try {
			return ResponseEntity.ok(String.valueOf(movementService.moveStockInternal(movementDto, employee)));
		} catch (MovementException mve) {
			throw mve;
		} catch (Exception e) {
			log.error("Failed saving internal stock movement", e);
			return ResponseEntity.internalServerError().build();
		}
	}

}
