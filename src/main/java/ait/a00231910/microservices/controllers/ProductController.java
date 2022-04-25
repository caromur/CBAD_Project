package ait.a00231910.microservices.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ait.a00231910.microservices.dao.ProductRepository;
import ait.a00231910.microservices.dto.Product;
import ait.a00231910.microservices.dto.ProductDTO;
import ait.a00231910.microservices.services.ProductService;

@RestController
@Service
public class ProductController {
	
	Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	ProductService productService;
	
//	@Value("${product-manager.helloProperty}")
//	private String helloInstance;
	
	@GetMapping("/")
	public String returnHello()
	{
		System.out.println("Testing Full Pipeline6");
		return productService.getHello();
	}
	
//	@GetMapping("/hello2")
//	public String returnHello2()
//	{
//		return "Added Hello Statement";
//	}
	
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

	@PostMapping("/products")
	ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
		Product product = new Product(productDTO);
		productRepo.save(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
	}
//
	@PutMapping("/product/{id}")
	ResponseEntity updateProductById(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO) {
		Product product = new Product(productDTO);
		product.setId(id);
		Optional<Product> savedProduct = productRepo.findById(id);
		if (savedProduct.isPresent()) {
			if (product.getPrice() == null) {
				product.setPrice(savedProduct.get().getPrice());
			}
			if (product.getName() == null) {
				product.setName(savedProduct.get().getName());
			}
			if (product.getDescription() == null) {
				product.setDescription(savedProduct.get().getDescription());
			}
			if (product.getSellerId() == null) {
				product.setSellerId(savedProduct.get().getSellerId());
			}

			productRepo.save(product);
			return ResponseEntity.status(HttpStatus.OK).body(product);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Product with an id of: " + id + " not found");
		}
	}

	@DeleteMapping("/product/{id}")
	ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {
		Optional<Product> savedProduct = productRepo.findById(id);
		if (savedProduct.isPresent()) {
			productRepo.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(savedProduct.get().toString() + " has been deleted");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Product with an id of: " + id + " not found");
		}
	}
}
