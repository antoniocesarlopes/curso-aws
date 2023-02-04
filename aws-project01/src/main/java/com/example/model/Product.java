package com.example.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Table(name = "products", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "model" }) })
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(length = 32, nullable = false)
	private String name;

	@NotEmpty
	@Column(length = 24, nullable = false)
	private String model;

	@NotEmpty
	@Column(length = 8, nullable = false)
	private String code;

	@NotNull
	@Column(nullable = false)
	private Double price;

	@Column(length = 12, nullable = true)
	private String color;

	public Product() {
		super();
	}

	public Product(Long id, String name, String model, String code, Double price, String color) {
		super();
		this.id = id;
		this.name = name;
		this.model = model;
		this.code = code;
		this.price = price;
		this.color = color;
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, color, id, model, name, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(code, other.code) && Objects.equals(color, other.color) && Objects.equals(id, other.id)
				&& Objects.equals(model, other.model) && Objects.equals(name, other.name)
				&& Objects.equals(price, other.price);
	}

}
