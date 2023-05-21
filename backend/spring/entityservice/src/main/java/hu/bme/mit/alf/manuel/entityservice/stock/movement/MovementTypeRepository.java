package hu.bme.mit.alf.manuel.entityservice.stock.movement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementTypeRepository extends JpaRepository<MovementType, Integer> {
}