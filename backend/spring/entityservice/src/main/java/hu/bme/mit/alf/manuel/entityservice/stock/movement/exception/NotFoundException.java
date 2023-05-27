package hu.bme.mit.alf.manuel.entityservice.stock.movement.exception;

public class NotFoundException extends MovementException {
	public NotFoundException(String name, Object id) {
		super(String.format("%s with id %s does not exist", name, id));
	}
}
