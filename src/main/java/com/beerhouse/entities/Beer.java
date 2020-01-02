package com.beerhouse.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "beer")
public class Beer implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;	
	private String ingredients;
	private Double alcoholContent;
	private Double price;
	private String category;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length = 100, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(length = 250, nullable = false)
	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	@Column(name =  "alcohol_content", nullable = false)
	public Double getAlcoholContent() {
		return alcoholContent;
	}

	public void setAlcoholContent(Double alcoholContent) {
		this.alcoholContent = alcoholContent;
	}

	@Column(nullable = false)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}
	
	@Column(length = 50, nullable = false)
	public void setCategory(String category) {
		this.category = category;
	}

}
