package hu.bme.mit.alf.manuel.entityservice.stock;

import hu.bme.mit.alf.manuel.entityservice.stock.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    List<Stock> findAllByProduct_Name(String name);
    List<Stock> findAllByLocation_Name(String location);


}