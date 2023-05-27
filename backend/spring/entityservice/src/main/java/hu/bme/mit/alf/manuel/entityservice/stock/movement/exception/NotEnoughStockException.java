package hu.bme.mit.alf.manuel.entityservice.stock.movement.exception;

public class NotEnoughStockException extends MovementException {
	public NotEnoughStockException(String product, String location) {
		super(String.format("There is not enough stock of %s in the location %s", product, location));
	}
}
