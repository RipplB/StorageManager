package hu.bme.mit.alf.manuel.entityservice.stock.movement;

import hu.bme.mit.alf.manuel.entityservice.product.Product;
import hu.bme.mit.alf.manuel.entityservice.product.ProductRepository;
import hu.bme.mit.alf.manuel.entityservice.stock.Stock;
import hu.bme.mit.alf.manuel.entityservice.stock.StockRepository;
import hu.bme.mit.alf.manuel.entityservice.stock.location.Location;
import hu.bme.mit.alf.manuel.entityservice.stock.location.LocationRepository;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.exception.MovementException;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.exception.NotEnoughStockException;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.exception.NotFoundException;
import hu.bme.mit.alf.manuel.entityservice.users.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementService {

	private static final String PRODUCT_NAME = "Product";
	private static final String LOCATION_NAME = "Location";

	private final StockRepository stockRepository;
	private final StockMovementRepository stockMovementRepository;
	private final LocationRepository locationRepository;
	private final ProductRepository productRepository;

	private Stock getOrCreateStock(Product product, Location location) {
		Optional<Stock> existingStock = stockRepository.findByProductAndLocation(product, location);
		if (existingStock.isPresent())
			return existingStock.get();
		log.info("No existing stock of {} on location {}, creating new", product.getName(), location.getName());
		Stock stock = new Stock();
		stock.setProduct(product);
		stock.setLocation(location);
		return stockRepository.save(stock);
	}

	public Integer receiveStock(MovementDto movementDto, User employee) throws MovementException {
		Product product = productRepository.findById(movementDto.getProduct()).orElseThrow(() -> new NotFoundException(PRODUCT_NAME, movementDto.getProduct()));
		Location location = locationRepository.findById(movementDto.getTargetLocation()).orElseThrow(() -> new NotFoundException(LOCATION_NAME, movementDto.getTargetLocation()));
		Stock persistentStock = getOrCreateStock(product, location);
		int amount = movementDto.getAmount();
		StockMovement stockMovement = new StockMovement();
		stockMovement.setStock(persistentStock);
		stockMovement.setEmployee(employee);
		stockMovement.setType(StockMovement.Type.INBOUND);
		stockMovement.setAmount(amount);
		StockMovement persistentMovement = stockMovementRepository.save(stockMovement);
		persistentStock.setAmount(persistentStock.getAmount() + amount);
		stockRepository.save(persistentStock);
		return persistentMovement.getId();
	}

	public Integer releaseStock(MovementDto movementDto, User employee) throws MovementException {
		Product product = productRepository.findById(movementDto.getProduct()).orElseThrow(() -> new NotFoundException(PRODUCT_NAME, movementDto.getProduct()));
		Location location = locationRepository.findById(movementDto.getSourceLocation()).orElseThrow(() -> new NotFoundException(LOCATION_NAME, movementDto.getSourceLocation()));
		Stock stock = stockRepository.findByProductAndLocation(product, location).orElseThrow(() -> new NotFoundException("Stock", String.format("[ %s - %s ]", product.getName(), location.getName())));
		int amount = movementDto.getAmount();
		if (stock.getAmount() < amount)
			throw new NotEnoughStockException(product.getName(), location.getName());
		StockMovement stockMovement = new StockMovement();
		stockMovement.setStock(stock);
		stockMovement.setEmployee(employee);
		stockMovement.setType(StockMovement.Type.OUTBOUND);
		stockMovement.setAmount(-amount);
		StockMovement persistentMovement = stockMovementRepository.save(stockMovement);
		int newAmount = stock.getAmount() - amount;
		if (newAmount == 0)
			stockRepository.delete(stock);
		else {
			stock.setAmount(newAmount);
			stockRepository.save(stock);
		}
		return persistentMovement.getId();
	}

	public Pair<Integer, Integer> moveStockInternal(MovementDto movementDto, User employee) throws MovementException {
		Product product = productRepository.findById(movementDto.getProduct()).orElseThrow(() -> new NotFoundException(PRODUCT_NAME, movementDto.getProduct()));
		Location sourceLocation = locationRepository.findById(movementDto.getSourceLocation()).orElseThrow(() -> new NotFoundException(LOCATION_NAME, movementDto.getSourceLocation()));
		Location targetLocation = locationRepository.findById(movementDto.getTargetLocation()).orElseThrow(() -> new NotFoundException(LOCATION_NAME, movementDto.getTargetLocation()));
		Stock sourceStock = stockRepository.findByProductAndLocation(product, sourceLocation).orElseThrow(() -> new NotFoundException("Stock", String.format("[ %s - %s ]", product.getName(), sourceLocation.getName())));
		Stock targetStock = getOrCreateStock(product, targetLocation);
		int amount = movementDto.getAmount();
		if (sourceStock.getAmount() < amount)
			throw new NotEnoughStockException(product.getName(), sourceLocation.getName());
		StockMovement sourceStockMovement = new StockMovement();
		sourceStockMovement.setStock(sourceStock);
		sourceStockMovement.setEmployee(employee);
		sourceStockMovement.setType(StockMovement.Type.INTERNAL);
		sourceStockMovement.setAmount(-amount);
		StockMovement persistentSourceMovement = stockMovementRepository.save(sourceStockMovement);
		StockMovement targetStockMovement = new StockMovement();
		targetStockMovement.setStock(targetStock);
		targetStockMovement.setEmployee(employee);
		targetStockMovement.setType(StockMovement.Type.INTERNAL);
		targetStockMovement.setAmount(amount);
		StockMovement persistentTargetMovement = stockMovementRepository.save(targetStockMovement);
		int newAmount = sourceStock.getAmount() - amount;
		if (newAmount == 0)
			stockRepository.delete(sourceStock);
		else {
			sourceStock.setAmount(newAmount);
			stockRepository.save(sourceStock);
		}
		targetStock.setAmount(targetStock.getAmount() + amount);
		stockRepository.save(targetStock);
		return Pair.of(persistentSourceMovement.getId(), persistentTargetMovement.getId());
	}

}
