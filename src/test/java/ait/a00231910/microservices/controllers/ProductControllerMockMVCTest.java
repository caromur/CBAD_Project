package ait.a00231910.microservices.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		MvcResult result = this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andReturn();
		String expected = "Hello World fail test";
		assertEquals(expected, result.getResponse().getContentAsString());
	}
	
	@Test
	public void addProductControllerTest() throws Exception
	{
		Product product = buildProduct();
		ObjectMapper map =new ObjectMapper();
		String jsonString = map.writeValueAsString(product);
		System.out.println(jsonString);
//		when(libraryService.buildId(book.getIsbn(),book.getAisle())).thenReturn(book.getId());
//		when(libraryService.checkBookAlreadyExist(book.getId())).thenReturn(false);
		this.mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andDo(print()).andExpect(status().isCreated());
//		.andExpect(jsonPath("$.id").value(product.getId()));
		verify(productRepo,new Times(1)).save(captor.capture());
		Product savedProduct=captor.getValue();
		System.out.println(savedProduct);
//		assertEquals(product.getName(),savedProduct.getName());
//		assertEquals(product.getId(),savedProduct.getId());
		assertEquals(product.getDescription(),savedProduct.getDescription()); 
		
	}
	
	
	
	public Product buildProduct()
	{
		Product product = new Product(1L, "Product 1", "Desc of Product 1", 25.0, 1L);
		return product;
	}
	
//	@Test
//	void contextLoads() {
//	}

}
