package hu.bme.mit.alf.manuel.entityservice.stock.movement;

import hu.bme.mit.alf.manuel.entityservice.stock.Stock;
import hu.bme.mit.alf.manuel.entityservice.stock.StockRepository;
import hu.bme.mit.alf.manuel.entityservice.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovementService {

	private final StockRepository stockRepository;
	private final StockMovementRepository stockMovementRepository;

	public Integer receiveStock(Stock stock, User employee, StockMovement.Type type, int amount) {
		Optional<Stock> existingStock = stockRepository.findByProductAndLocation(stock.getProduct(), stock.getLocation());
		Stock persistentStock = existingStock.orElseGet(() -> stockRepository.save(stock));
		StockMovement stockMovement = new StockMovement();
		stockMovement.setStock(persistentStock);
		stockMovement.setEmployee(employee);
		stockMovement.setType(type);
		stockMovement.setAmount(amount);
		StockMovement persistentMovement = stockMovementRepository.save(stockMovement);
		persistentStock.setAmount(persistentStock.getAmount() + amount);
		stockRepository.save(persistentStock);
		return persistentMovement.getId();
	}

}
