package ait.a00231910.microservices.dto;

public class ProductDTO {

	private Long id;
	
	private String name;
	private String description;
	private Double price;
	private Long sellerId;

	public ProductDTO()
	{
		
	}

	public ProductDTO(Long id, String name, String description, double price, Long sellerId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.sellerId = sellerId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", sellerId=" + sellerId + "]";
	}

}
