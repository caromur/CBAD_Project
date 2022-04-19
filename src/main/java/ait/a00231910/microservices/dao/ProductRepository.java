package ait.a00231910.microservices.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ait.a00231910.microservices.dto.Product;


@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{
	List<Product> findBySellerId(Long id);

}
