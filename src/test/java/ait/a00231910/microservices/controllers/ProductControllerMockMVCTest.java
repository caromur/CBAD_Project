package ait.a00231910.microservices.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import ait.a00231910.microservices.dao.ProductRepository;
import ait.a00231910.microservices.dto.Product;
import ait.a00231910.microservices.services.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerMockMVCTest {
	
	@Autowired
	ProductController productController;
	
	@MockBean
	ProductRepository productRepo;
	
	@MockBean
	ProductService productService;
	
	@Captor
	ArgumentCaptor<Product> captor;
	
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * Returns 'Hello World' from the root URI
	 * @throws Exception
	 */
	@Test
	public void getHelloTest() throws Exception
	{
		when(productService.getHello()).thenReturn("Hello World");
		// Generate the result of a Get request on the root URI
		MvcResult result = this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andReturn();
		
		// Specify the expected output
		String expected = "Hello World";
		
		// Compare the expected with the actual output
		assertEquals(expected, result.getResponse().getContentAsString());
	}
	
	@Test
	public void addProductControllerTest() throws Exception
	{
		// Building a product object
		Product product = buildProduct();
		
		// Getting a String representation of the product
		ObjectMapper map =new ObjectMapper();
		String jsonString = map.writeValueAsString(product);

		// The result is generated by performing a post request on the relevant endpoint
		this.mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andDo(print()).andExpect(status().isCreated());

		// Store the savedProduct as the Product argument provided
		verify(productRepo,new Times(1)).save(captor.capture());
		Product savedProduct=captor.getValue();
		
		// Compare the expected to actual output
		assertEquals(product.getName(),savedProduct.getName());
		assertEquals(product.getId(),savedProduct.getId());
		assertEquals(product.getDescription(),savedProduct.getDescription()); 
		assertEquals(product.getPrice(),savedProduct.getPrice()); 
		assertEquals(product.getSellerId(),savedProduct.getSellerId()); 
	}
	
	@Test
	public void getProductByIdTest() throws Exception
	{
		// Building a product object
		Product product = buildProduct();
		
		// Getting a String representation of the product
		ObjectMapper map =new ObjectMapper();
		String jsonString = map.writeValueAsString(product);
		
		// Creating an Optional product of the product above i.e. isPresent()
		Optional<Product> optProd = Optional.of(product);
		
		/*
		 * When the findById method is run using the productRepo mock 
		 * Then the Optional product is returned
		 */
		when(productRepo.findById(any())).thenReturn(optProd);
		
		// The result is generated by performing a get request on the relevant endpoint
		MvcResult result = this.mockMvc.perform(get("/product/"+product.getId()).contentType(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk()).andReturn();
		
		// The body of the response is stored as a String
		String resultStr = result.getResponse().getContentAsString();

		// Compare the original expected jsonString with the newly created resultStr
		assertEquals(jsonString,resultStr);
	}
	
	@Test
	public void getProductByIdDoesntExistTest() throws Exception
	{
		// Building a product object
		Product product = buildProduct();
		
		// Creating an Optional product of the product above i.e. isPresent()
		Optional<Product> optProd = Optional.empty();
		
		/*
		 * When the findById method is run using the productRepo mock 
		 * Then the Optional product is returned
		 */
		when(productRepo.findById(any())).thenReturn(optProd);
		
		// The result is generated by performing a get request on the relevant endpoint
		this.mockMvc.perform(get("/product/"+product.getId()).contentType(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isNotFound())
		.andExpect(content().string("Product with an id of: " + product.getId() + " not found"));
	}
	
	@Test
	public void getProductsTest() throws Exception
	{
		// Building a product object
		Product product = buildProduct();
		
		// Adding product to list
		List<Product> products = new ArrayList<>();
		products.add(product);
		
		// Getting a String representation of the product
		ObjectMapper map =new ObjectMapper();
		String jsonString = map.writeValueAsString(product);
		
		// Generate the expected list as a String
		String listExpected = "[" + jsonString + "]";
		System.out.println("List expected: " + listExpected);
		
		/*
		 * When the getAllProductEntities method is run using the productController mock 
		 * Then the list of products is returned
		 */
		when(productController.getAllProductEntities()).thenReturn(products);
		
		// The result is generated by performing a get request on the relevant endpoint
		MvcResult result = this.mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk()).andReturn();
		
		// The body of the response is stored as a String
		String resultStr = result.getResponse().getContentAsString();

		// Compare the original expected list String with the newly created resultStr
		assertEquals(listExpected,resultStr);
	}
	
	@Test
	public void getProductsEmptyTest() throws Exception
	{
		
		// Creating product list and not adding
		List<Product> products = new ArrayList<>();
		
		// Generate the expected list as a String
		String listExpected = "[]";
		System.out.println("List expected: " + listExpected);
		
		/*
		 * When the getAllProductEntities method is run using the productController mock 
		 * Then the list of products is returned - which is empty
		 */
		when(productController.getAllProductEntities()).thenReturn(products);
		
		// The result is generated by performing a get request on the relevant endpoint
		MvcResult result = this.mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk()).andReturn();
		
		// The body of the response is stored as a String
		String resultStr = result.getResponse().getContentAsString();

		// Compare the original expected list String with the newly created resultStr
		assertEquals(listExpected,resultStr);
	}
	
	@Test
	public void getProductsForSellerTest() throws Exception
	{
		// Building a product object
		Product product = buildProduct();
		
		// Adding product to list
		List<Product> products = new ArrayList<>();
		products.add(product);
		
		// Getting a String representation of the product
		ObjectMapper map =new ObjectMapper();
		String jsonString = map.writeValueAsString(product);
		
		// Generate the expected list as a String
		String listExpected = "[" + jsonString + "]";
		System.out.println("List expected: " + listExpected);
		
		/*
		 * When the getAllProductEntitiesById method is run using the productController mock 
		 * Then the list of products is returned
		 */
		when(productController.getAllProductEntitiesById(product.getSellerId())).thenReturn(products);
		
		// The result is generated by performing a get request on the relevant endpoint
		MvcResult result = this.mockMvc.perform(get("/products/"+product.getSellerId()).contentType(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk()).andReturn();
		
		// The body of the response is stored as a String
		String resultStr = result.getResponse().getContentAsString();

		// Compare the original expected list String with the newly created resultStr
		assertEquals(listExpected,resultStr);
	}
	
	@Test
	public void deleteProductByIdTest() throws Exception
	{
		// Building a product object
		Product product = buildProduct();
		
		// Creating an Optional product of the product above i.e. isPresent()
		Optional<Product> optProd = Optional.of(product);
		
		// When productRepo calls findById - return the Optional Product
		when(productRepo.findById(any())).thenReturn(optProd);
		
		// Void return type when productRepo calls deleteById
		doNothing().when(productRepo).deleteById(product.getId());
		
		// The result is generated by performing a deletes request on the relevant endpoint
		this.mockMvc.perform(delete("/product/" + product.getId()).contentType(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk()).andExpect(content().string(product.toString() + " has been deleted")); 
	}
	
	@Test
	public void deleteProductByIdDoesntExistTest() throws Exception
	{
		// Building a product object
		Product product = buildProduct();
		
		// Creating an Optional product which is empty
		Optional<Product> optProd = Optional.empty();
		
		// When productRepo calls findById - return the Optional Product
		when(productRepo.findById(any())).thenReturn(optProd);
		
		// Void return type when productRepo calls deleteById
		doNothing().when(productRepo).deleteById(product.getId());
		
		// The result is generated by performing a deletes request on the relevant endpoint
		this.mockMvc.perform(delete("/product/" + product.getId()).contentType(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isNotFound()).andExpect(content().string("Product with an id of: " + product.getId() + " not found")); 
	}
	
	@Test
	public void updateProductByIdTest() throws Exception
	{
		// Building a product object
		Product product = updateProduct();

		// Creating an Optional product of the product above i.e. isPresent()
		Optional<Product> optProd = Optional.of(product);

		// Getting a String representation of the product
		ObjectMapper map =new ObjectMapper();
		String jsonString = map.writeValueAsString(product);

		// When productRepo calls findById - return the Optional Product
		when(productRepo.findById(any())).thenReturn(optProd);

		// The result is generated by performing a deletes request on the relevant endpoint
		MvcResult result = this.mockMvc.perform(put("/product/" + product.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonString))
		.andDo(print()).andExpect(status().isOk()).andReturn();

		// The body of the response is stored as a String
		String resultStr = result.getResponse().getContentAsString();

		assertEquals(jsonString, resultStr);
	}
	
	@Test
	public void updateProductByIdDoesntExistTest() throws Exception
	{
		// Building a product object
		Product product = updateProduct();

		// Creating an Optional product of the product above i.e. isPresent()
		Optional<Product> optProd = Optional.empty();

		// Getting a String representation of the product
		ObjectMapper map =new ObjectMapper();
		String jsonString = map.writeValueAsString(product);

		// When productRepo calls findById - return the Optional Product
		when(productRepo.findById(any())).thenReturn(optProd);

		// The result is generated by performing a deletes request on the relevant endpoint
		this.mockMvc.perform(put("/product/" + product.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonString))
		.andDo(print()).andExpect(status().isNotFound()).andExpect(content().string("Product with an id of: " + product.getId() + " not found"));
	}
	
	public Product buildProduct()
	{
		Product product = new Product(1L, "Product 1", "Desc of Product 1", 25.0, 1L);
		return product;
	}
	
	public Product updateProduct()
	{
		Product product = new Product(1L, "Product 1", "UPDATED Desc of Product 1", 25.0, 1L);
		return product;
	}

	
//	@Test
//	void contextLoads() {
//	}

}
