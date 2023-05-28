package hu.bme.mit.alf.manuel.entityservice.stock;

import hu.bme.mit.alf.manuel.entityservice.product.Product;
import hu.bme.mit.alf.manuel.entityservice.stock.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    List<Stock> findAllByProduct_Name(String name);
    List<Stock> findAllByLocation_Name(String location);
    List<Stock> findAllByProduct(Product product);
    List<Stock> findAllByLocation(Location location);
    Optional<Stock> findByProductAndLocation(Product product, Location location);

    @Query("SELECT s FROM Stock s WHERE s.product IN (SELECT product FROM Stock GROUP BY product HAVING COUNT(*) > 1)")
    List<Stock> getMultipleProducts();


}