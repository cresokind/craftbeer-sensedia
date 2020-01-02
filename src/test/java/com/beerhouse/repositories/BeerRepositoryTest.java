package com.beerhouse.repositories;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.aspectj.lang.annotation.AfterReturning;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.entities.Beer;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BeerRepositoryTest {
	
	@Autowired
	private BeerRepository beerRepository;
	
	@Before
	public void setup() throws Exception {
		
		Beer beer = new Beer();
		beer.setName("Roleta Russa Session IPA 500ml");
		beer.setIngredients("Água, malte de cevada, lúpulos importados e levedura.");
		beer.setCategory("IPA");
		beer.setPrice(24.00);
		beer.setAlcoholContent(4.5);
		
		this.beerRepository.save(beer);
	}
	
	@Test
	public void shouldFindBeerById() {
		Beer beer = this.beerRepository.findById(1L);

		assertEquals("IPA", beer.getCategory());
	}
	
	@Test
	public void shouldFindByName() {
		
		final String name = "Roleta Russa Session IPA 500ml";
		
		Beer beer = this.beerRepository.findByName(name);
		assertEquals(name, beer.getName());
	}
	
	@AfterReturning
	public final void tearDown() throws Exception {
		this.beerRepository.deleteAll();
	}

}
