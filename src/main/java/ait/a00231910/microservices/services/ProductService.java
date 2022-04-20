package ait.a00231910.microservices.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import ait.a00231910.microservices.dao.ProductRepository;
import ait.a00231910.microservices.dto.Product;

public class ProductService {
	
	@Autowired ProductRepository productRepo;
	
	public boolean checkProductExists(Long id)
	{
		Optional<Product> product = productRepo.findById(id);
		if(product.isPresent())
		{
			return true;
		}
		return false;
	}

}
