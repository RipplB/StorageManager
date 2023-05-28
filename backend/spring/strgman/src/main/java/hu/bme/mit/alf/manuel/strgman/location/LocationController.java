package hu.bme.mit.alf.manuel.strgman.location;

import hu.bme.mit.alf.manuel.entityservice.EntityService;
import hu.bme.mit.alf.manuel.entityservice.stock.location.Location;
import hu.bme.mit.alf.manuel.strgman.GenericDto;
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
@RequestMapping("/${endpoints.location}")
@RequiredArgsConstructor
public class LocationController extends ValidatorBaseController {

	@Value("${endpoints.location}")
	private String endpoint;

	private final EntityService entityService;
	private final ModelMapper modelMapper = new ModelMapper();

	@GetMapping
	public List<Location> getAllLocations() {
		return entityService.getAllLocations();
	}

	@GetMapping("/{id}")
	public Location getLocation(@PathVariable Integer id) {
		return entityService.getLocation(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	@Secured("STORAGE")
	public ResponseEntity<GenericDto<Integer>> createLocation(@RequestBody @Valid LocationDto locationDto) {
		Integer id = entityService.saveLocation(modelMapper.map(locationDto, Location.class));
		log.info("New location added: "+locationDto.getName());
		return ResponseEntity.created(URI.create(String.format("%s/%d", endpoint, id))).body(new GenericDto<>(id));
	}

	@PutMapping("/{id}")
	@Secured("STORAGE")
	public void updateLocation(@PathVariable Integer id, @RequestBody @Valid LocationDto locationDto) {
		if (entityService.getLocation(id).isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		Location location = modelMapper.map(locationDto, Location.class);
		location.setId(id);
		entityService.saveLocation(location);
		log.info("New location added: "+locationDto.getName());
	}

}
