package ait.a00231910.microservices.controllers;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
//@AutoConfigureTestDatabase
public class ProductControllerITintegrationTest {
	
	@Test
	public void getProductsIntegrationTest()
	{
		String expected = "[\r\n"
				+ "    {\r\n"
				+ "        \"id\": 1,\r\n"
				+ "        \"name\": \"Test Product\",\r\n"
				+ "        \"description\": \"Description for test product\",\r\n"
				+ "        \"price\": 59.99,\r\n"
				+ "        \"sellerId\": 3\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "        \"id\": 2,\r\n"
				+ "        \"name\": \"Some other prodcut\",\r\n"
				+ "        \"description\": \"Description for some other product\",\r\n"
				+ "        \"price\": 100.0,\r\n"
				+ "        \"sellerId\": 1\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "        \"id\": 3,\r\n"
				+ "        \"name\": \"Another product\",\r\n"
				+ "        \"description\": \"Description for another product\",\r\n"
				+ "        \"price\": 25.5,\r\n"
				+ "        \"sellerId\": 2\r\n"
				+ "    }\r\n"
				+ "]";
		
		TestRestTemplate restTemplate =new TestRestTemplate();
		ResponseEntity<String>	response =restTemplate.getForEntity("http://localhost:8084/products", String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

}
