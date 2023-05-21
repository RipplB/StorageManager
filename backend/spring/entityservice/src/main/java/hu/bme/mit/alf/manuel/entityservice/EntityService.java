package hu.bme.mit.alf.manuel.entityservice;

import hu.bme.mit.alf.manuel.entityservice.product.Product;
import hu.bme.mit.alf.manuel.entityservice.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityService {
	private final ProductRepository productRepository;

	@Autowired
	public EntityService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
}
