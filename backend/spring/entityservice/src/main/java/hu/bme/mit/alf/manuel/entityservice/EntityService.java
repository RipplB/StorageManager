package hu.bme.mit.alf.manuel.entityservice;

import hu.bme.mit.alf.manuel.entityservice.product.Product;
import hu.bme.mit.alf.manuel.entityservice.product.ProductRepository;
import hu.bme.mit.alf.manuel.entityservice.stock.location.Location;
import hu.bme.mit.alf.manuel.entityservice.stock.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntityService {
	private final ProductRepository productRepository;

	private final LocationRepository locationRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Optional<Product> getProduct(Integer id) {
		return productRepository.findById(id);
	}

	public Integer saveProduct(Product product) {
		return productRepository.save(product).getId();
	}

	public List<Location> getAllLocations() {
		return locationRepository.findAll();
	}

	public Optional<Location> getLocation(Integer id) {
		return locationRepository.findById(id);
	}

	public Integer saveLocation(Location location) {
		return locationRepository.save(location).getId();
	}

}
