package hu.bme.mit.alf.manuel.entityservice.stock.movement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Integer> {

    @Query("SELECT sm FROM StockMovement sm " +
            "WHERE sm.timestamp IN (" +
            "    SELECT MAX(sm2.timestamp) " +
            "    FROM StockMovement sm2 " +
            "    WHERE sm2.stock = sm.stock " +
            "    GROUP BY sm2.stock" +
            ")")
    List<StockMovement> findLatestStockMovementsForEachStock();
}