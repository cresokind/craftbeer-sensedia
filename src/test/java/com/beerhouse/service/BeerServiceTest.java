package com.beerhouse.service;

import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.entities.Beer;
import com.beerhouse.repositories.BeerRepository;
import com.beerhouse.service.BeerService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BeerServiceTest {
	
	@MockBean
	private BeerRepository beerRepository;
	
	@Autowired
	private BeerService beerService;
	
	@Before
	public void setUp() throws Exception{
		BDDMockito.given(this.beerRepository.findById(Mockito.anyLong())).willReturn(new Beer());
		BDDMockito.given(this.beerRepository.findByName(Mockito.anyString())).willReturn(new Beer());
		BDDMockito.given(this.beerRepository.findAll(Mockito.any(PageRequest.class))).willReturn(new PageImpl<Beer>(new ArrayList<Beer>()));
		BDDMockito.given(this.beerRepository.findOne(Mockito.anyLong())).willReturn(new Beer());
		BDDMockito.given(this.beerRepository.save(Mockito.any(Beer.class))).willReturn(new Beer());
	}
	
	@Test
	public void testBuscaBeerById() {
		Optional<Beer> beer = this.beerService.buscarPorId(1L);
		assertTrue(beer.isPresent());
	}
	
	@Test
	public void testCadastrarBeer() {
		Beer beer = this.beerService.cadastrar(new Beer());
		assertNotNull(beer);
	}	
	
	@Test
	public void testBuscaCervejaByDescricao() {
		Optional<Beer> cerveja = this.beerService.buscarCervejaPorNome("Roleta Russa");

		assertTrue(cerveja.isPresent());
	}
	
	@Test
	public void testBuscarTodasCervejas() {
		Page<Beer> cerveja = this.beerRepository.findAll(new PageRequest(0, 10));

		assertNotNull(cerveja);
	}

}
