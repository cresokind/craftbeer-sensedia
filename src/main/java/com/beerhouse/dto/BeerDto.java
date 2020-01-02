package com.beerhouse.dto;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.Length;	
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "category", "ingredients", "price", "alcoholContent" })
public class BeerDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;	
	private String ingredients;
	private Double alcoholContent;
	private Double price;
	private String category;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty(message = "Nome da cerveja não pode ser vazio.")
	@Length(min = 3, max = 100, message = "Nome da cerveja dever ter no minino 3 a 100 caracteres")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotEmpty(message = "Descricao dos ingredientes não pode ser vazia.")
	@Length(min = 3, max = 250, message = "Descricao dos ingredientes dever ter no minino 3 a 250 caracteres")
	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	
	@DecimalMin(value = "0.1", message = "Teor alcoolico não pode ser menor que 0")
	public Double getAlcoholContent() {
		return alcoholContent;
	}

	public void setAlcoholContent(Double alcoholContent) {
		this.alcoholContent = alcoholContent;
	}
	
	@DecimalMin(value = "0.1", message = "Preço não pode ser menor que 0")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	
	@NotEmpty(message = "Categoria não pode ser vazio. Ex: IPA, WEISS, WITBIER")
	@Length(min = 3, max = 50, message = "Categoria dever ter no minino 3 a 50 caracteres")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "BeerDto [id=" + id + ", name=" + name + ", ingredients=" + ingredients + ", alcoholContent="
				+ alcoholContent + ", price=" + price + ", category=" + category + "]";
	}
	

}
