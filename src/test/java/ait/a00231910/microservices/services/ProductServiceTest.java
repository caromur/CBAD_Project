package ait.a00231910.microservices.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceTest {
	
	@Mock
	ProductService productService;
	
	@Test
	public void getHelloTest()
	{
		Mockito.when(productService.getHello()).thenReturn("Hello World");
		String expected = "Hello World";
		String actual = productService.getHello();
		assertEquals(expected, actual);
	}

}
