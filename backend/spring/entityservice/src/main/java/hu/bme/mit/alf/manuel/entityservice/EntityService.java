package hu.bme.mit.alf.manuel.entityservice;

import hu.bme.mit.alf.manuel.entityservice.product.Product;
import hu.bme.mit.alf.manuel.entityservice.product.ProductRepository;
import hu.bme.mit.alf.manuel.entityservice.stock.Stock;
import hu.bme.mit.alf.manuel.entityservice.stock.StockRepository;
import hu.bme.mit.alf.manuel.entityservice.stock.location.Location;
import hu.bme.mit.alf.manuel.entityservice.stock.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntityService {
	private final ProductRepository productRepository;

	private final LocationRepository locationRepository;
	private final StockRepository stockRepository;

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

	public List<Stock> allStocksFilteredByProductAndLocation(Product product, Location location) {
		if (product == null && location == null) {
			log.info("No filter, returning all stocks");
			return stockRepository.findAll();
		}
		if (product != null) {
			if (location == null)
				return stockRepository.findAllByProduct(product);
			log.info("Filter for bot location and product");
			Optional<Stock> stockOptional = stockRepository.findByProductAndLocation(product, location);
			return stockOptional.map(List::of).orElseGet(LinkedList::new);
		}
		return stockRepository.findAllByLocation(location);
	}

}
