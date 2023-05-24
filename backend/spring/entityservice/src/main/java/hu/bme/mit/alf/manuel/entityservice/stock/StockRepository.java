package hu.bme.mit.alf.manuel.entityservice.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    List<Stock> findByProductId(Integer ID);

    @Query("SELECT DISTINCT s.id FROM Stock s")
    List<Integer> findAllProductIds();

    @Query("SELECT DISTINCT s.id FROM Stock s WHERE s.product.name = :name")
    List<Integer> findAllProductIdsByName(@Param("name") String name);

}