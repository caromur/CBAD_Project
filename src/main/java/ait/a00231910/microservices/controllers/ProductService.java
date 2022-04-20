package ait.a00231910.microservices.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ait.a00231910.microservices.dao.ProductRepository;
import ait.a00231910.microservices.dto.Product;

@RestController
@Service
public class ProductService {
	
	Logger log = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	ProductRepository productRepo;
	
//	@Value("${product-manager.helloProperty}")
//	private String helloInstance;
	
	@RequestMapping("/")
	public String returnHello()
	{
		System.out.println("Testing SonarCloud Automatic Analysis");
		return "Hello World";
	}
	
	@GetMapping("/product/{id}")
	ResponseEntity getProductById(@PathVariable("id") Long id) {
		Optional<Product> product = productRepo.findById(id);
		if (product.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(product);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Product with an id of: " + id + " not found");
		}
	}

//	@GetMapping("/products")
//	Iterable<Product> getAllProducts() {
//		return productRepo.findAll();
//	}
	
	@GetMapping("/products")
	List<Product> getAllProductEntities() {
		log.info("product-entities method called");
		Iterable<Product> productIter = productRepo.findAll();
		List<Product> products = new ArrayList<>();
		for(Product product : productIter)
		{
			products.add(product);
		}
		return products;
	}
	
	@GetMapping("/products/{id}")
	List<Product> getAllProductEntitiesById(@PathVariable("id") Long id) {
		log.info("product-entities/{id} method called");
		List<Product> products = productRepo.findBySellerId(id);
		return products;
	}

//	@PostMapping("/products")
//	ResponseEntity<Product> createProduct(@RequestBody Product product) {
//		productRepo.save(product);
//		return ResponseEntity.status(HttpStatus.OK).body(product);
//	}
//
//	@PutMapping("/product/{id}")
//	ResponseEntity updateProductById(@PathVariable("id") Long id, @RequestBody Product product) {
//		product.setId(id);
//		Optional<Product> savedProduct = productRepo.findById(id);
//		if (savedProduct.isPresent()) {
//			if (product.getPrice() == null) {
//				product.setPrice(savedProduct.get().getPrice());
//			}
//			if (product.getName() == null) {
//				product.setName(savedProduct.get().getName());
//			}
//			if (product.getDescription() == null) {
//				product.setDescription(savedProduct.get().getDescription());
//			}
//
//			productRepo.save(product);
//			return ResponseEntity.status(HttpStatus.OK).body(product);
//		} else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body("Seller with an id of: " + id + " not found");
//		}
//	}
//
//	@DeleteMapping("/product/{id}")
//	ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {
//		Optional<Product> savedProduct = productRepo.findById(id);
//		if (savedProduct.isPresent()) {
//			productRepo.deleteById(id);
//			return ResponseEntity.status(HttpStatus.OK).body(savedProduct.get().toString() + " has been deleted");
//		} else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body("Product with an id of: " + id + " not found");
//		}
//	}
}
